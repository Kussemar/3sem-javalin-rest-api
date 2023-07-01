package dk.lyngby.routes;

import dk.lyngby.handler.UserHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationRoute {
    private final UserHandler userHandler = new UserHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userHandler::login);
                post("/register", userHandler::register);
            });
        };
    }
}
