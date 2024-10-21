package org.achl.di.util;

import io.javalin.http.HttpStatus;
import org.achl.di.data.DataManager;
import org.achl.di.entities.SerializableObject;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Util
{
    private static final SecureRandom m_rng = new SecureRandom();
    private static final Logger m_debugLogger = Logger.getLogger("DEBUG");

    /**
     * Returns a string built from lines read from an input reader
     * @param _reader Reader to try and read the string from
     * @return String containing content read from _reader
     */
    public static String stringFromReader(Reader _reader)
    {
        BufferedReader br = new BufferedReader(_reader);
        Stream<String> lines = br.lines();
        StringBuilder strBuilder = new StringBuilder();
        lines.forEach(strBuilder::append);

        try
        {
            br.close();
        }
        catch (IOException e)
        {
            Logger.getLogger(Util.class.getName()).log(Level.WARNING, "Failed to close a buffered reader!", e);
        }

        return strBuilder.toString();
    }

    public static int generateRandomInt(int lowerBound, int upperBound)
    {
        return m_rng.nextInt(lowerBound, upperBound);
    }

    public static void validateString(String src, HttpStatus status, RequestErrorCause cause) throws RequestException
    {
        if ((src == null) || src.isEmpty())
            throw new RequestException(status, cause);
    }

    public static String b64Encode(String src)
    {
        return new String(Base64.getEncoder().encode(src.getBytes()));
    }

    public static String b64Decode(String src)
    {
        return new String(Base64.getDecoder().decode(src));
    }
    public static void criticalExit()
    {
        System.exit(-1);
    }

    public static JSONArray dumpListToJson(String queryName, SerializableObject serializableObject, Object ... queryParams)
            throws SQLException, JSONException, IllegalAccessException
    {
        JSONArray jArr = new JSONArray();
        PreparedStatement pStat = DataManager.prepareStatement(queryName, queryParams);

        ResultSet resultSet = pStat.executeQuery();
        while (resultSet.next())
        {
            serializableObject.loadFromSet(resultSet);
            jArr.put(serializableObject.dumpToJson());
        }

        return jArr;
    }
}


