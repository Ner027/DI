package org.achl.di.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryUtil
{
    private static final Logger m_logger = Logger.getLogger(QueryUtil.class.getName());

    /**
     * Retrieves an SQL Query present on the resources folder by its file name but removes the @ character if present
     * @param qName SQL Query name
     * @return Pre Processed SQL Query String
     */
    public static String getQuery(String qName)
    {
        return getQueryRaw(qName).replaceAll("<", "").replaceAll(">", "");
    }

    /**
     * Retrieves an SQL Query present on the resources folder by its file name
     * @param qName SQL Query name
     * @return SQL Query String
     */
    public static String getQueryRaw(String qName)
    {
        InputStream is = QueryUtil.class.getResourceAsStream("/sql/" + qName + ".sql");

        if (is == null)
        {
            m_logger.log(Level.SEVERE, "SQL Query not found! Path to query: " + qName);
            System.exit(-1);
        }

        InputStreamReader isr = new InputStreamReader(is);
        String temp = Util.stringFromReader(isr);

        try
        {
            is.close();
            isr.close();
        }
        catch (IOException e)
        {
            m_logger.log(Level.WARNING, "Failed to close a resource!", e);
        }

        return temp;
    }
}
