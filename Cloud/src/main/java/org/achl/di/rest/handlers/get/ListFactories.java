package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.FactorySerializer;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

public class ListFactories implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/factory/list";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        try
        {
            JSONArray jArr = Util.dumpListToJson("GetFactoryList",
                    new FactorySerializer());

            context.json(jArr.toString());
            context.status(HttpStatus.OK);
        }
        catch (Exception e)
        {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}
