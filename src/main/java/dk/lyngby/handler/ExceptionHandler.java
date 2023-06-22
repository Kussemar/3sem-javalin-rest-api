package dk.lyngby.handler;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.routes.Routes;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    public void exceptionHandlerNotAuthorized(NotAuthorizedException e, Context ctx) {
        ctx.status(e.getStatusCode());
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.json(new NotAuthorizedException(e.getStatusCode(), e.getMessage()));
    }

    public void exceptionHandlerApi(ApiException e, Context ctx) {
        ctx.status(e.getStatusCode());
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.json(new ApiException(e.getStatusCode(), e.getMessage()));
    }
    public void exceptionHandler(Exception e, Context ctx) {
        ctx.status(500);
        LOGGER.error(ctx.attribute("requestInfo") + " " + ctx.res().getStatus() + " " + e.getMessage());
        ctx.json(new Exception(e.getMessage()));
    }
}
