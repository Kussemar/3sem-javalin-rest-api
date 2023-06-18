package dk.lyngby.routes;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    PersonRoutes personRoutes = new PersonRoutes();
    AuthenticationRoute authenticationRoutes = new AuthenticationRoute();

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.before(ctx -> {
                // TODO: Authenticate
                System.out.println("Before");
            });
            app.routes(() -> {
                path("/", authenticationRoutes.getRoutes());
                path("/persons", personRoutes.getRoutes());
            });
            app.exception(Exception.class, (e, ctx) -> {
                ctx.result("Something went wrong: " + e.getMessage());
            });
            app.after(ctx -> {
                // TODO: Add logging
                System.out.println("After");
            });
        };
    }
}
