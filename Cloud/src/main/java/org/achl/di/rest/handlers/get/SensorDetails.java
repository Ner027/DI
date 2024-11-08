package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Sensor;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.enums.SensorType;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


public class SensorDetails implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/sens/details";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String sensId = context.queryParam("sensId");
        Util.validateString(sensId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);

        Sensor tempSensor = new Sensor();
        tempSensor.load("sens_id", Long.parseLong(sensId));

        if (tempSensor.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_ID);
        }

        JSONObject jSensor = tempSensor.dumpToJson();
        jSensor.put("sensStatus", (tempSensor.getStatus() == 0) ? "OK" : "NOK");
        SensorType sensorType = SensorType.values()[tempSensor.getType()];
        jSensor.put("sensTypStr", sensorType.toString());

        context.json(jSensor.toString());
    }
}
