package org.achl.di;

import org.achl.di.data.DataManager;
import org.achl.di.rest.RestServer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    private static final Logger m_logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args)
    {
        m_logger.log(Level.INFO, "Starting DI Cloud System!");

        long startTime = System.currentTimeMillis();

        DataManager.initDatabase();
        RestServer.getInstance().initHandlers();

        long endTime = System.currentTimeMillis();

        m_logger.log(Level.INFO, "Start up finished! Took " + (endTime - startTime) + " ms");
    }
}