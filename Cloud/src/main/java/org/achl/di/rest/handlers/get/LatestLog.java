package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.data.DataManager;
import org.achl.di.entities.types.SensorLog;
import org.achl.di.rest.handlers.IHandler;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LatestLog implements IHandler
{

    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/sens/latest";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String sensId = context.queryParam("sensId");

        try
        {
            SensorLog tempLog = new SensorLog();
            PreparedStatement pStat = DataManager.prepareStatement("GetLatestLog", sensId);
            ResultSet resultSet = pStat.executeQuery();
            if (resultSet.next())
            {
                tempLog.loadFromSet(resultSet);
            }
            context.json(tempLog.dumpToJson());
        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }

    }
}