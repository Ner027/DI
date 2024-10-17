package org.achl.di.data;


import org.achl.di.util.QueryUtil;
import org.achl.di.util.Util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @apiNote Provides an interface to the database
 */
public class DataManager
{
    /*-----------------------------------------------------------------------------------------------------------------*
     *  Private Variables
     *----------------------------------------------------------------------------------------------------------------*/

    private static Connection m_connection;
    private static final Logger m_logger = Logger.getLogger(DataManager.class.getName());

    /*-----------------------------------------------------------------------------------------------------------------*
     * Public Methods
     *----------------------------------------------------------------------------------------------------------------*/

    /**
     * @apiNote Initializes the database
     */
    public static void initDatabase()
    {
        insertData("InitTables");
    }

    /**
     * @apiNote Executes an SQL Query but returns no data
     * @param qName Name of the SQL Query to execute
     * @param params Parameters to be set on the query
     * @return true if SQL Query was executed correctly, otherwise returns false
     */
    public static boolean insertData(String qName, Object... params)
    {
        try
        {
            PreparedStatement pStat = prepareStatement(qName, params);
            return pStat.execute();
        }
        catch (SQLException e)
        {
            m_logger.log(Level.WARNING, "Unable to execute an SQL Request!", e);
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T getData(Class<T> type, String qName, String colName, Object... params)
    {
        try
        {
            PreparedStatement pStat = prepareStatement(qName, params);

            ResultSet set = pStat.executeQuery();

            while (set.next())
            {
                Object obj = set.getObject(colName);

                if (type.isInstance(obj))
                    return (T) obj;
            }
        }
        catch (SQLException e)
        {
            m_logger.log(Level.WARNING, "Unable to execute query!", e);
        }

        return null;
    }

    /**
     * @param qName Name of the SQL Query to prepare
     * @return Returns true if SQL Statement was successfully created, otherwise returns false
     * @throws SQLException If an SQL related runtime error occurs
     * @apiNote Creates an SQL Prepared Statment based on a query name
     */
    @SuppressWarnings("all") //The linter isn't smart enough to reliaze that string propagation doesn't cause problems
    public static PreparedStatement getRawPreparedStatement(String qName) throws SQLException
    {
        return getConnection().prepareStatement(QueryUtil.getQuery(qName));
    }

    /**
     * @apiNote Created a prepared statement with defined parameters
     * @param qName Name of the SQL Query to prepare
     * @param params Variable argument parameters to set on the prepared statement
     * @return Returns a prepared statement created from qName and configured with params
     * @throws SQLException If an error occurs while trying to create the statement
     */
    public static PreparedStatement prepareStatement(String qName, Object... params) throws SQLException
    {
        PreparedStatement pStat = getRawPreparedStatement(qName);

        for (int i = 0; i < params.length; i++)
            pStat.setObject(i + 1, params[i]);

        return pStat;
    }

    /*-----------------------------------------------------------------------------------------------------------------*
     * Private Methods
     *----------------------------------------------------------------------------------------------------------------*/

    /**
     * @return New connection object created
     * @apiNote Tries to acquire a connection object to a database, if connection to database fails, execution is killed
     */
    private static Connection getConnection()
    {
        //To optimize number of connections, a connection is saved, if said saved connection is alive this one is return
        //Otherwise an attempt is made to create a new connection to the database
        SettingsManager settings = SettingsManager.getInstance();
        try
        {
            if ((m_connection != null) && (!m_connection.isClosed()))
                return m_connection;

            //Connection parameters are acquired from the config file
            m_connection = DriverManager.getConnection(
                    settings.getProperty("postgres_url"),
                    settings.getProperty("postgres_user"),
                    settings.getProperty("postgres_password"));
        }
        catch (SQLException | RuntimeException e)
        {
            m_logger.log(Level.SEVERE, "Unable to connect to database!", e);
            Util.criticalExit();
        }

        return m_connection;
    }
}
