package org.achl.di.entities;

import kotlin.Pair;
import org.achl.di.data.DataManager;
import org.achl.di.data.MappedStatement;
import org.achl.di.enums.SerializableFieldType;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * @apiNote Objects that need to be serialized have to extend this class
 */
public abstract class SerializableObject implements DBObject
{
    public int loadFromJson(JSONObject jObject) throws IllegalAccessException, JSONException
    {
        int serializableFields = 0;

        for (Field field : getClass().getDeclaredFields())
        {
            if (!field.isAnnotationPresent(JsonField.class))
                continue;

            JsonField jf = field.getAnnotationsByType(JsonField.class)[0];
            serializableFields++;

            if (!jObject.has(jf.name()))
                continue;

            if (!field.canAccess(this))
                field.setAccessible(true);

            field.set(this, jObject.get(jf.name()));

            serializableFields--;
        }

        return serializableFields;
    }

    public JSONObject dumpToJson() throws IllegalAccessException, JSONException
    {
        JSONObject jObject = new JSONObject();

        for (Field field : getClass().getDeclaredFields())
        {
            if (!field.isAnnotationPresent(JsonField.class))
                continue;

            JsonField jf = field.getAnnotationsByType(JsonField.class)[0];

            if (!field.canAccess(this))
                field.setAccessible(true);

            jObject.put(jf.name(), field.get(this));
        }

        return jObject;
    }

    /**
     * @apiNote Inserts this object into the database
     * @throws IllegalAccessException If illegal access to a field occurs this exception is thrown
     * @throws SQLException If an SQL error occurs this exception is thrown
     */
    public void insert() throws IllegalAccessException, SQLException
    {
        MappedStatement mStat = new MappedStatement(getInsertionQueryName());
        mStat.getPreparedStatement(getFields(true)).execute();
    }

    @SuppressWarnings("unchecked")
    public <T> T insertReadBack(String readBackField) throws SQLException, IllegalAccessException
    {
        MappedStatement mStat = new MappedStatement(getInsertionReadBackQueryName(readBackField));

        try
        {
            ResultSet resultSet = mStat.getPreparedStatement(getFields(true)).executeQuery();
            if (!resultSet.next())
                return null;
            return (T) resultSet.getObject(readBackField);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    /**
     * @apiNote Updated this object on the database
     * @throws IllegalAccessException If illegal access to a field occurs this exception is thrown
     * @throws SQLException If an SQL error occurs this exception is thrown
     */
    public void update() throws SQLException, IllegalAccessException
    {
        MappedStatement mStat = new MappedStatement(getUpdateQueryName());
        mStat.getPreparedStatement(getFields(false)).execute();
    }

    public int loadFromSet(ResultSet set) throws SQLException, IllegalAccessException
    {
        int serializedFields = 0;

        //Iterate all the fields in this class
        for (Field field : getClass().getDeclaredFields())
        {
            //If current field doesn't have the "SerializableField" annotation skip it
            if (!field.isAnnotationPresent(SerializableField.class))
                continue;

            //If the field has more than one "SerializableField" annotation only the first is used, the rest are ignored
            SerializableField sf = field.getAnnotationsByType(SerializableField.class)[0];

            //Try to get the field identified by the name provided in the annotation
            Object obj = set.getObject(sf.name());

            //If this field was not retrieved from the database
            if (obj == null)
                continue;

            //Bypass field visibility
            if (!field.canAccess(this))
                field.setAccessible(true);

            //If the field is of type String and the annotation has the "encode" flag set, encode it in B64
            if ((field.getType() == String.class) && (sf.encode()))
                field.set(this, new String(Base64.getDecoder().decode(obj.toString())));
            else
                field.set(this, obj);

            //If the field was correctly loaded increase the number of serialized fields
            serializedFields++;
        }

        return serializedFields;
    }

    /**
     * @apiNote Loads an object from the database using id to identify it
     * @param fieldName Name of the field used to identify the object
     * @param id Value of the field used to identify the object
     * @return Returns the number of serializable fields loaded from the database
     * @throws IllegalAccessException If illegal access to a field occurs this exception is thrown
     * @throws SQLException If an SQL error occurs this exception is thrown
     */
    public int load(String fieldName, Object id) throws SQLException, IllegalAccessException
    {
        //Create a prepared statement using the correct load query
        PreparedStatement pStat = DataManager.getRawPreparedStatement(getLoadQueryName(fieldName));

        //If the field used for identifying the object is encoded make sure the parameter set is also encoded
        if (isFieldEncoded(fieldName))
            pStat.setObject(1, Base64.getEncoder().encodeToString(id.toString().getBytes()));
        else
            pStat.setObject(1, id);

        ResultSet set = pStat.executeQuery();

        //If no fields were retrieved from the database return the number of fields loaded, in this case 0
        if (!set.next())
            return 0;

        return loadFromSet(set);
    }

    /**
     * @apiNote Deletes the current object from the database
     * @param fieldName Name of the field to identify the object
     * @return Returns true if any change was made to the database otherwise returns false
     * @throws SQLException If something goes wrong while preparing the SQL statement
     * @throws IllegalAccessException If the field used to identify the object is not accessible
     */
    public boolean delete(String fieldName) throws SQLException, IllegalAccessException
    {
        Object value = getFieldValueByName(fieldName);

        if (value == null)
            return false;

        //Prepare a statement using the delete query
        PreparedStatement pStat = DataManager.getRawPreparedStatement(getDeleteQueryName(fieldName));
        pStat.setObject(1, value);
        pStat.execute();

        return pStat.getUpdateCount() > 0;
    }

    /*-----------------------------------------------------------------------------------------------------------------*
     * Private Methods
     *----------------------------------------------------------------------------------------------------------------*/

    /**
     * @apiNote Loads the SerializableObject fields on to a list identified by their name set on the annotation
     * @param decOnly If fields that only can be loaded and not dumped should be loaded
     * @return List of Pairs of type String, Object where the pair key is the field name set on the annotation and the
     * value is the field value
     * @throws IllegalAccessException If unable to access a field an exception is thrown
     */
    private List<Pair<String, Object>> getFields(boolean decOnly) throws IllegalAccessException
    {
        List<Pair<String, Object>> fieldList = new LinkedList<>();

        //Iterate all fields of the class
        for (Field field : getClass().getDeclaredFields())
        {
            //If the field doesn't have the "SerializableField" annotation skip it
            if (!field.isAnnotationPresent(SerializableField.class))
                continue;

            //If the field has more than one "SerializableField" Annotation only the first is used, the rest are ignored
            SerializableField sf = field.getAnnotationsByType(SerializableField.class)[0];

            //If the field is of type encode only, and only fields that can be decoded should be loaded skip this field
            if ((sf.type() == SerializableFieldType.ENCODE) && decOnly)
                continue;

            //Bypass visibility of the fields
            if (!field.canAccess(this))
                field.setAccessible(true);

            Object obj = field.get(this);

            //If the field is of type String and the annotation has the "encode" flag set, encode it in B64
            if ((field.getType() == String.class) && (sf.encode()) && (obj != null))
                fieldList.add(new Pair<>(sf.name(), new String(Base64.getEncoder().encode(obj.toString().getBytes()))));
            else
                fieldList.add(new Pair<>(sf.name(), obj));
        }

        return fieldList;
    }

    /**
     * @apiNote Allows to check if a field has a "SerializableField" annotation with the "encoded" flag set
     * @param fieldName Name of the field to check
     * @return Returns true if the field is encoded, otherwise returns false
     */
    private boolean isFieldEncoded(String fieldName)
    {
        for (Field field : getClass().getDeclaredFields())
        {
            //If the field doesn't have the "SerializableField" annotation skit it
            if (!field.isAnnotationPresent(SerializableField.class))
                continue;

            //As previously, if more than on annotations of type "SerializableField" are present only use the first one
            SerializableField sf = field.getAnnotationsByType(SerializableField.class)[0];

            //If the current field name, matches the parameter "fieldName" return its "encode" flag
            if (sf.name().equals(fieldName))
                return sf.encode();
        }

        //If no field with name "fieldName" was found returns false
        return false;
    }

    /**
     * @apiNote Retrieves a field value by its annotation name
     * @param fieldName Name of the field to retrieve
     * @return An object containing the field value if found, or null if something goes wrong or no field was found
     * @throws IllegalAccessException If illegal access to the field occurs an exception is thrown
     */
    private Object getFieldValueByName(String fieldName) throws IllegalAccessException
    {
        for (Field field : getClass().getDeclaredFields())
        {
            if (!field.isAnnotationPresent(SerializableField.class))
                continue;

            if (field.getAnnotationsByType(SerializableField.class)[0].name().equals(fieldName))
            {
                if (!field.canAccess(this))
                    field.setAccessible(true);

                return field.get(this);
            }
        }

        return null;
    }
}
