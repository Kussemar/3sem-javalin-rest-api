package dk.lyngby.routes;

import dk.lyngby.handler.PersonHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonHandler personHandler = new PersonHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/", () -> {
                post("/", personHandler::createPerson);
                get("/", personHandler::getAllPersons);
                get("{id}", personHandler::getPersonById);
                patch("{id}", personHandler::updatePersonById);
                delete("{id}", personHandler::deletePersonById);
            });
        };
    }
}
