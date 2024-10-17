package org.achl.di.entities.types;

import org.achl.di.entities.JsonField;
import org.achl.di.entities.SerializableField;
import org.achl.di.entities.SerializableObject;
import org.achl.di.enums.SerializableFieldType;

public class Factory extends SerializableObject
{
    @JsonField(name = "factoryId")
    @SerializableField(name = "factory_id", type = SerializableFieldType.ENCODE)
    private final long m_factoryId;

    @JsonField(name = "factoryName")
    @SerializableField(name = "factory_name", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_factoryName;

    @JsonField(name = "coordLong")
    @SerializableField(name = "coord_long", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_coordLong;

    @JsonField(name = "coordLat")
    @SerializableField(name = "coord_lat", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_coordLat;

    public Factory()
    {
        m_factoryId = -1;
    }

    public long getId()
    {
        return m_factoryId;
    }

    public String getName()
    {
        return m_factoryName;
    }

    @Override
    public String getInsertionReadBackQueryName(String readBackField)
    {
        return "";
    }

    public boolean setName(String newName)
    {
        if (newName.length() < 4)
            return false;

        m_factoryName = newName;
        return true;
    }

    public void setCoord(String coordLong, String coordLat)
    {
        m_coordLong = coordLong;
        m_coordLat = coordLat;
    }

    @Override
    public String getInsertionQueryName()
    {
        return "InsertFactory";
    }

    @Override
    public String getUpdateQueryName()
    {
        return "UpdateFactory";
    }

    @Override
    public String getLoadQueryName(String fieldName)
    {
        return "GetFactoryBy_" + fieldName;
    }

    @Override
    public String getDeleteQueryName(String fieldName)
    {
        return "DeleteFactoryBy_" + fieldName;
    }
}
