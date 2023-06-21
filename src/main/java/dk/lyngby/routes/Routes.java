package dk.lyngby.routes;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PersonRoutes personRoutes = new PersonRoutes();
    private final AuthenticationRoute authenticationRoutes = new AuthenticationRoute();

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.routes(() -> {
                path("/", authenticationRoutes.getRoutes());
                path("/", personRoutes.getRoutes());
            });
            app.exception(ApiException.class, (e, ctx) -> {
                ctx.res().setStatus(e.getStatusCode());
                ctx.json(new ApiException( e.getStatusCode(), e.getMessage()));
            });
            app.exception(NotAuthorizedException.class, (e, ctx) -> {
                ctx.res().setStatus(e.getStatusCode());
                ctx.json(new NotAuthorizedException(e.getStatusCode(), e.getMessage()));
            });
            app.exception(Exception.class, (e, ctx) -> {
                ctx.res().setStatus(500);
                ctx.json(new Exception(e.getMessage()));
            });
            app.after(ctx -> {
                // TODO: Add logging
                System.out.println("After");
            });
        };
    }
}
