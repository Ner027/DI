package org.achl.di.rest.handlers.post;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.entities.types.Sensor;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class RegisterSensor implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.POST;
    }

    @Override
    public String getPath()
    {
        return "/sens/register";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String sensName = context.formParam("sensName");
        String sensType = context.formParam("sensType");
        String lmId = context.formParam("lmId");

        Util.validateString(sensName, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_NAME);
        Util.validateString(sensType, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_SENS_TYPE);
        Util.validateString(lmId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_EXISTS);

        LocalManager parentManager = new LocalManager();
        parentManager.load("lm_id", Long.parseLong(lmId));
        if (parentManager.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_ID);
        }

        Sensor newSensor = new Sensor();
        newSensor.setName(sensName);
        newSensor.setParentId(parentManager.getId());
        newSensor.setSensorStatus((short)0);
        newSensor.setSensorType(Short.parseShort(sensType));

        long newId = newSensor.insertReadBack("sens_id");
        if (newId < 0)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }

        JSONObject jObj = new JSONObject();
        jObj.put("sensId", newId);
        context.json(jObj.toString());
        context.status(HttpStatus.CREATED);
    }
}