package dk.lyngby.routes;

import dk.lyngby.handler.LoginHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationRoute {
    private final LoginHandler loginHandler = new LoginHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/", () -> {
                post("/login", loginHandler::login);
                post("/register", ctx -> {
                    // TODO: Add register
                    //throw new RuntimeException("Not implemented");
                    ctx.result("Register");
                });
            });
        };
    }
}
