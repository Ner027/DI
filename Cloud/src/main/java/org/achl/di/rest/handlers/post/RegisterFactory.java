package org.achl.di.rest.handlers.post;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Factory;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class RegisterFactory implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.POST;
    }

    @Override
    public String getPath()
    {
        return "/factory/register";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String factoryName = context.formParam("factoryName");
        String coordLong = context.formParam("coordLong");
        String coordLat = context.formParam("coordLat");

        Util.validateString(factoryName, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_NAME);
        Util.validateString(coordLong, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_NAME);
        Util.validateString(coordLat, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_NAME);

        Factory newFactory = new Factory();

        newFactory.load("factory_name", factoryName);
        if (newFactory.getId() > 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_EXISTS);
        }

        if (!newFactory.setName(factoryName))
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_NAME);
        }

        newFactory.setCoord(coordLong, coordLat);

        try
        {
            newFactory.insert();
            newFactory.load("factory_name", factoryName);
            if (newFactory.getId() < 0)
                throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

            JSONObject jObj = new JSONObject();
            jObj.put("factoryId", newFactory.getId());

            context.json(jObj.toString());
            context.status(HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
