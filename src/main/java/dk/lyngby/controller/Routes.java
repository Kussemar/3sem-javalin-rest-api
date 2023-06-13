package dk.lyngby.controller;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

public class Routes {

    public static EndpointGroup routes(Javalin app) {
        return () -> {
            // Routes
            app.get("/", ctx -> ctx.result("Hello World"));
        };
    }
}
