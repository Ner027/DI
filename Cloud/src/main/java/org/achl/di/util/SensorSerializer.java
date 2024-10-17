package org.achl.di.util;

import org.achl.di.entities.types.Sensor;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SensorSerializer implements JSONSerializer
{
    @Override
    public JSONObject toJson(ResultSet resultSet) throws SQLException, IllegalAccessException, JSONException
    {
        Sensor tempSensor = new Sensor();
        tempSensor.loadFromSet(resultSet);
        return tempSensor.dumpToJson();
    }
}
