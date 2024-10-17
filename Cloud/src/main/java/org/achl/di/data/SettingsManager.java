package org.achl.di.data;

import org.achl.di.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SettingsManager
{
    /*-----------------------------------------------------------------------------------------------------------------*
     *  Private Variables
     *----------------------------------------------------------------------------------------------------------------*/

    private static SettingsManager m_instance;
    private final HashMap<String,String> m_data = new HashMap<>();
    private final Logger m_logger = Logger.getLogger(SettingsManager.class.getName());

    /*-----------------------------------------------------------------------------------------------------------------*
     *  Public Methods
     *----------------------------------------------------------------------------------------------------------------*/

    /**
     * @apiNote Creates or returns a singleton of type SettingsManager
     * @return Returns a singleton instance of a SettingsManager Object
     */
    public static SettingsManager getInstance()
    {
        if (m_instance == null)
            m_instance = new SettingsManager();

        return m_instance;
    }

    /**
     * @apiNote Returns a property loaded from the config file
     * @param key Name of the property to retrieve
     * @return String containing the loaded property
     * @throws RuntimeException If a property with name "key" is not found
     */
    public String getProperty(String key)
    {
        String obj = m_data.get(key);

        if (obj == null)
            throw new RuntimeException("Key not found!");

        return obj;
    }

    /**
     * @apiNote Constructor for the SettingsManager Class
     * @implNote The config file is loaded when the object is created, the Singleton mechanism already ensures this
     * only runs once
     */
    private SettingsManager()
    {
        File file = new File("/home/andre/config.json");

        //If the config file does not exist yet, try to create it and load the default values
        if (!file.exists())
        {
            try
            {
                //Try to create a new file to write the default config file to
                if (!file.createNewFile())
                {
                    //If unable to create said file, log errors to the user and stop execution
                    m_logger.log(Level.SEVERE,  "Failed to create a new config file! Maybe check file permissions");
                    Util.criticalExit();
                }

                //If able to create the new config file, open a file stream on it
                FileWriter fileWriter = new FileWriter(file);
                InputStream inputStream = getClass().getResourceAsStream("/defaults/defaultConfig.json");

                //Check if the default config file is present on the resources folder
                if (inputStream == null)
                {
                    //If not exit execution
                    m_logger.log(Level.SEVERE,  "Couldn't find default config file!");
                    Util.criticalExit();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                //Write to the newly created config file the default config
                fileWriter.write(Util.stringFromReader(inputStreamReader));
                inputStreamReader.close();
                fileWriter.close();
            }
            catch (IOException e)
            {
                m_logger.log(Level.SEVERE, "Couldn't create a new settings file!", e);
                Util.criticalExit();
            }

            m_logger.log(Level.INFO, "Default configuration file generated! Please change it and restart!");
            System.exit(0);
        }

        //If the system was able to open the settings file, get the JSON data and parse it accordingly
        try
        {
            String content = Util.stringFromReader(new FileReader(file));
            JSONObject jObject = new JSONObject(content);

            JSONArray jArr = jObject.names();
            for (int i = 0; i < jArr.length(); i++)
            {
                String key = jArr.getString(i);
                m_data.put(key, jObject.getString(key));
            }
        }
        catch (JSONException | FileNotFoundException e)
        {
            m_logger.log(Level.WARNING, "Invalid JSON Format!", e);
        }
    }
}
