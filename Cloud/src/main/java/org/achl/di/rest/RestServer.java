package org.achl.di.rest;

import io.javalin.Javalin;
import jakarta.annotation.Nonnull;
import org.achl.di.data.SettingsManager;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.rest.handlers.get.ListFactories;
import org.achl.di.rest.handlers.get.ListLocalManagers;
import org.achl.di.rest.handlers.get.ListLogs;
import org.achl.di.rest.handlers.get.ListSensors;
import org.achl.di.rest.handlers.post.AddSensorLog;
import org.achl.di.rest.handlers.post.RegisterFactory;
import org.achl.di.rest.handlers.post.RegisterLocalManager;
import org.achl.di.rest.handlers.post.RegisterSensor;
import org.achl.di.util.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RestServer
{
    private static RestServer m_instance;
    private final Logger m_logger = Logger.getLogger(getClass().getName());
    private Javalin m_server;
    private boolean m_initComplete;
    private RestServer()
    {
        int serverPort;
        m_initComplete = false;

        try
        {
            serverPort = Integer.parseInt(SettingsManager.getInstance().getProperty("rest_port"));
            m_server = Javalin.create().start(serverPort);
        }
        catch (RuntimeException e)
        {
            m_logger.log(Level.SEVERE, "Unable to acquire Rest Port from config file!", e);
            Util.criticalExit();
        }
    }
    public static RestServer getInstance()
    {
        if (m_instance == null)
            m_instance = new RestServer();

        return m_instance;
    }

    private void addHandler(@Nonnull IHandler handler)
    {
        m_server.addHttpHandler(handler.getType(), handler.getPath(), handler);
    }

    public void initHandlers()
    {
        if (m_initComplete)
            return;

        addHandler(new RegisterFactory());
        addHandler(new RegisterLocalManager());
        addHandler(new RegisterSensor());
        addHandler(new AddSensorLog());
        addHandler(new ListLocalManagers());
        addHandler(new ListSensors());
        addHandler(new ListLogs());
        addHandler(new ListFactories());

        m_initComplete = true;
    }
}
