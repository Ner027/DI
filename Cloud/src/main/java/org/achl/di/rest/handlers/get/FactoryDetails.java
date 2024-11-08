package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.Factory;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;

public class FactoryDetails implements IHandler
{

    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/factory/details";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String factoryId = context.queryParam("factoryId");
        Util.validateString(factoryId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_ID);

        Factory factory = new Factory();
        factory.load("factory_id", Long.parseLong(factoryId));

        if (factory.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_FACTORY_ID);
        }

        context.json(factory.dumpToJson().toString());
    }
}
