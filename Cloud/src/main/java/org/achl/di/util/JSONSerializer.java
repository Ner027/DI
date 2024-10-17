package org.achl.di.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JSONSerializer
{
    JSONObject toJson(ResultSet resultSet) throws SQLException, IllegalAccessException, JSONException;
}
