package org.achl.di.rest.handlers.post;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Sensor;
import org.achl.di.entities.types.SensorLog;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AddSensorLog implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.POST;
    }

    @Override
    public String getPath()
    {
        return "/sens/data";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String sensId = context.formParam("sensId");
        String logVal = context.formParam("logVal");

        Util.validateString(sensId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);
        Util.validateString(logVal, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LOG_VAL);

        Sensor parentSensor = new Sensor();
        parentSensor.load("sens_id", Long.parseLong(sensId));
        if (parentSensor.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);
        }

        SensorLog newLog = new SensorLog();
        newLog.setParentId(parentSensor.getId());
        newLog.setValue(Float.parseFloat(logVal));
        newLog.refreshTimestamp();
        newLog.insert();

        JSONObject jObj = new JSONObject();
        jObj.put("RFU", 0);
        context.json(jObj.toString());
        context.status(HttpStatus.OK);
    }
}
