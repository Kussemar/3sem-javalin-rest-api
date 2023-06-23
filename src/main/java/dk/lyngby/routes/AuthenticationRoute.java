package dk.lyngby.routes;

import dk.lyngby.handler.LoginHandler;
import dk.lyngby.handler.RegisterHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationRoute {
    private final LoginHandler loginHandler = new LoginHandler();
    private final RegisterHandler registerHandler = new RegisterHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", loginHandler::login);
                post("/register", registerHandler::register);
            });
        };
    }
}
