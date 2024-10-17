package org.achl.di.rest.exceptions;

import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import org.achl.di.enums.RequestErrorCause;

public class RequestException extends HttpResponseException
{
    public RequestException(HttpStatus status, RequestErrorCause cause)
    {
        super(status.getCode(), "{\"cause\":" + cause.getValue() + "}");
    }
}
