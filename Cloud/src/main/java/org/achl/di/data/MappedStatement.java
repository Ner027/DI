package org.achl.di.data;

import kotlin.Pair;
import org.achl.di.util.QueryUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * @apiNote Since most flavours of the JDBC Driver don't support variable name parameters on prepared statements this
 * class implements said functionality by enabling a simple syntax for variable arguments.
 * To use variable arguments on your SQL Query just enclose the parameter name in <> and later on you can obtain a
 * default SQL Prepared Statement while being able to define parameters by their name
 */
public class MappedStatement
{

    /*-----------------------------------------------------------------------------------------------------------------*
     *  Private Variables
     *----------------------------------------------------------------------------------------------------------------*/

    private final PreparedStatement m_preparedStatement;
    private final HashMap<String, Integer> m_map = new HashMap<>();


    /*-----------------------------------------------------------------------------------------------------------------*
     *  Public Methods
     *----------------------------------------------------------------------------------------------------------------*/

    /**
     * @apiNote Creates a MappedStatement from a Query Name
     * @param qName Name of the SQL Query to load the MappedStatement from
     * @throws SQLException If a parameter name on the SQL Query was poorly enclosed
     */
    public MappedStatement(String qName) throws SQLException
    {
        //SQL Parameters use indexes starting at 1
        int paramCount = 1;
        boolean isNamedParameter = false;

        String sqlQuery = QueryUtil.getQueryRaw(qName);

        //Using StringBuilders since they are more optimal for sequential appending of characters than using plain Strings
        StringBuilder parameterName = new StringBuilder();

        //Parse the SQL Query
        for (char c : sqlQuery.toCharArray())
        {
            //If a '<' was found it means a named parameter starts here
            if (c == '<')
            {
                isNamedParameter = true;
                //Clear the StringBuilder
                parameterName.setLength(0);
                continue;
            }
            //The '>0 marks the end of a named parameters
            else if (c == '>')
            {
                isNamedParameter = false;
                //Map the named parameter accordingly
                m_map.put(parameterName.toString(), paramCount++);
            }

            //If currently parsing a named parameter, push it to the StringBuilder
            if (isNamedParameter)
                parameterName.append(c);
        }

        //If isNamedParameter is true, it means one of the named parameters wasn't encapsulated properly
        if (isNamedParameter)
            throw new SQLException("Invalid named parameter query!");

        //Get the raw prepared statement without any parameters set
        m_preparedStatement = DataManager.getRawPreparedStatement(qName);
    }

    /**
     * @apiNote Retrieves a prepared statement with parameters defined by their names
     * @param params List of a Pair of type String, Object, where the pair key is the parameter name, and the value is
     * to the value to assign to the parameter
     * @return Returns a prepared statement created from the Query Name supplied on the constructor and parameters
     * defined by params
     * @throws SQLException If a parameter name was not found on the query
     */
    public PreparedStatement getPreparedStatement(List<Pair<String, Object>> params) throws SQLException
    {
        for (Pair<String, Object> param : params)
        {
            String key = param.getFirst();

            if (!m_map.containsKey(key))
                throw new SQLException("Parameter not found!");

            m_preparedStatement.setObject(m_map.get(key), param.getSecond());
        }

        return m_preparedStatement;
    }
}
