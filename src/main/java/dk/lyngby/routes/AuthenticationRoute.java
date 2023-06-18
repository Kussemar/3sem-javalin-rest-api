package dk.lyngby.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationRoute {

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/", () -> {
                post("/register", ctx -> {
                    // TODO: Add register
                    //throw new RuntimeException("Not implemented");
                    ctx.result("Register");
                });
                post("/login", ctx -> {
                    // TODO: Add login
                    ctx.result("Login");
                });
            });
        };
    }
}
