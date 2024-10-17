package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.data.DataManager;
import org.achl.di.entities.types.Factory;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.LocalManagerSerializer;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.achl.di.util.Util.dumpListToJson;

public class ListLocalManagers implements IHandler
{

    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/lm/list";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String factoryId = context.queryParam("factoryId");

        Util.validateString(factoryId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_ID);

        Factory parentFactory = new Factory();
        parentFactory.load("factory_id", Long.parseLong(factoryId));
        if (parentFactory.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_ID);
        }

        try
        {
            JSONArray jArr = Util.dumpListToJson("GetLocalManagersList",
                    new LocalManagerSerializer(),
                    parentFactory.getId());

            context.json(jArr.toString());
            context.status(HttpStatus.OK);

        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
