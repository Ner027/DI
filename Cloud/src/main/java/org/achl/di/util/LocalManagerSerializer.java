package org.achl.di.util;

import org.achl.di.entities.types.LocalManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalManagerSerializer implements JSONSerializer
{
    @Override
    public JSONObject toJson(ResultSet resultSet) throws SQLException, IllegalAccessException, JSONException
    {
        LocalManager tempManager = new LocalManager();
        tempManager.loadFromSet(resultSet);
        return tempManager.dumpToJson();
    }
}
