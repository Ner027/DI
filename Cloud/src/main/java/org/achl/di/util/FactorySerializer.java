package org.achl.di.util;

import org.achl.di.entities.types.Factory;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FactorySerializer implements JSONSerializer
{
    @Override
    public JSONObject toJson(ResultSet resultSet) throws SQLException, IllegalAccessException, JSONException
    {
        Factory tempFactory = new Factory();
        tempFactory.loadFromSet(resultSet);
        return tempFactory.dumpToJson();
    }
}
