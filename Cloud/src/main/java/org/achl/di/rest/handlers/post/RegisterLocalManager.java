package org.achl.di.rest.handlers.post;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Factory;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class RegisterLocalManager implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.POST;
    }

    @Override
    public String getPath()
    {
        return "/lm/register";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        JSONObject jObj = new JSONObject(context.body());

        String lmName = jObj.getString("lmName");
        String lmHwId = jObj.getString("hwId");

        Util.validateString(lmName, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_NAME);

        Factory parentFactory = new Factory();
        parentFactory.load("factory_id", jObj.getLong("factoryId"));
        if (parentFactory.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_ID);
        }

        LocalManager newManager = new LocalManager();
        newManager.load("hw_id", lmHwId);
        if (newManager.getId() >= 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_EXISTS);
        }

        if (!newManager.setHwId(lmHwId))
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_HWID);
        }

        if (!newManager.setName(lmName))
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_NAME);
        }

        newManager.setParentId(parentFactory.getId());

        try
        {
            newManager.insert();
            newManager.load("hw_id", lmHwId);
            if (newManager.getId() < 0)
            {
                throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
            }

            jObj.clear();
            jObj.put("lmId", newManager.getId());
            context.json(jObj.toString());
            context.status(HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
