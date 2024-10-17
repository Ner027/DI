package org.achl.di.util;

import org.achl.di.entities.types.SensorLog;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogSerializer implements JSONSerializer
{
    @Override
    public JSONObject toJson(ResultSet resultSet) throws SQLException, IllegalAccessException, JSONException
    {
        SensorLog sensorLog = new SensorLog();
        sensorLog.loadFromSet(resultSet);
        return sensorLog.dumpToJson();
    }
}
