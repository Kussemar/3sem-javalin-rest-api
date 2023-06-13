package dk.lyngby.controller;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    public static EndpointGroup routes(Javalin app) {
        return () -> {
            // Routes
            app.routes(() -> {
                path("persons", () -> {
                    post("/", PersonController::createPerson);
                    get("/", PersonController::getAllPersons);
                    get("{id}", PersonController::getPersonById);
                    patch("{id}", PersonController::editPersonById);
                    delete("{id}", PersonController::deletePersonById);
                });
            });
        };
    }
}
