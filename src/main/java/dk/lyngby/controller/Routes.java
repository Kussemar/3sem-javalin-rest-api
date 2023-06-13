package dk.lyngby.controller;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            // Routes
            app.routes(() -> {
                path("/", ()-> {
                    post("/register", ctx -> {
                        // TODO: Add register
                    });
                    post("/login", ctx -> {
                        // TODO: Add login
                    });
                });
                path("persons", () -> {
                    before(ctx -> {
                        // TODO: Add authentication
                    });
                    post("/", PersonController::createPerson);
                    get("/", PersonController::getAllPersons);
                    get("{id}", PersonController::getPersonById);
                    patch("{id}", PersonController::editPersonById);
                    delete("{id}", PersonController::deletePersonById);
                });
                after(ctx -> {
                    // TODO: Add logging
                });
            });
        };
    }
}
