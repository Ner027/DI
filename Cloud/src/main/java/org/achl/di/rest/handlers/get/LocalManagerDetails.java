package org.achl.di.rest.handlers.get;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import org.achl.di.entities.types.LocalManager;
import org.achl.di.enums.RequestErrorCause;
import org.achl.di.rest.exceptions.RequestException;
import org.achl.di.rest.handlers.IHandler;
import org.achl.di.util.Util;
import org.jetbrains.annotations.NotNull;

public class LocalManagerDetails implements IHandler
{
    @Override
    public HandlerType getType()
    {
        return HandlerType.GET;
    }

    @Override
    public String getPath()
    {
        return "/lm/details";
    }

    @Override
    public void handle(@NotNull Context context) throws Exception
    {
        String lmId = context.queryParam("lmId");
        Util.validateString(lmId, HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_ID);

        LocalManager localManager = new LocalManager();
        localManager.load("lm_id", Long.parseLong(lmId));

        if (localManager.getId() < 0)
        {
            throw new RequestException(HttpStatus.FORBIDDEN, RequestErrorCause.INVALID_LM_ID);
        }

        context.json(localManager.dumpToJson().toString());
    }
}
