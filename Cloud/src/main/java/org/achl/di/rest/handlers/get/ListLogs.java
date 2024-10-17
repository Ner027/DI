package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Sensor;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.LogSerializer;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

public class ListLogs implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/sens/logs";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String sensId = context.queryParam("sensId");
        String startTime = context.queryParam("startTime");
        String endTime = context.queryParam("endTime");

        Util.validateString(sensId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);
        Util.validateString(startTime, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_START_TIME);
        Util.validateString(endTime, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_END_TIME);

        Sensor parentSensor = new Sensor();
        parentSensor.load("sens_id", Long.parseLong(sensId));
        if (parentSensor.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);
        }

        try
        {
            JSONArray jArr = Util.dumpListToJson("GetLogList",
                    new LogSerializer(),
                    parentSensor.getId(),
                    Long.parseLong(startTime),
                    Long.parseLong(endTime));

            context.json(jArr.toString());
            context.status(HttpStatus.OK);

        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
