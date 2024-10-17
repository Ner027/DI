package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.LocalManagerSerializer;
import org.achl.di.util.SensorSerializer;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class ListSensors implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/sens/list";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String lmId = context.queryParam("lmId");
        Util.validateString(lmId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_ID);

        LocalManager parentManager = new LocalManager();
        parentManager.load("lm_id", Long.parseLong(lmId));
        if (parentManager.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_ID);
        }

        try
        {
            JSONArray jArr = Util.dumpListToJson("GetSensorList",
                    new SensorSerializer(),
                    parentManager.getId());

            context.json(jArr.toString());
            context.status(HttpStatus.OK);

        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
