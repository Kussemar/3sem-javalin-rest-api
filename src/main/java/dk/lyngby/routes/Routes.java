package dk.lyngby.routes;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.handler.ExceptionHandler;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PersonRoutes personRoutes = new PersonRoutes();
    private final AuthenticationRoute authenticationRoutes = new AuthenticationRoute();
    private final Logger LOGGER = LoggerFactory.getLogger(Routes.class);
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();
    private int count = 0;

    public void getRequestInfo(Context ctx) {
        String requestInfo = ctx.req().getMethod() + " " + ctx.req().getRequestURI();
        ctx.attribute("requestInfo", requestInfo);
    }

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.before(this::getRequestInfo);
            app.routes(() -> {
                path("/", authenticationRoutes.getRoutes());
                path("/", personRoutes.getRoutes());
            });
            app.exception(ApiException.class, exceptionHandler::exceptionHandlerApi);
            app.exception(NotAuthorizedException.class, exceptionHandler::exceptionHandlerNotAuthorized);
            app.exception(Exception.class, exceptionHandler::exceptionHandler);
            app.after(ctx -> {
                LOGGER.info("{}. Request {} was handled with status code {}", count++, ctx.attribute("requestInfo"), ctx.status());
            });
        };
    }
}
