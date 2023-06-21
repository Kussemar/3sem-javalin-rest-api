package dk.lyngby.routes;

import dk.lyngby.handler.AuthenticationHandler;
import dk.lyngby.handler.PersonHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonHandler personHandler = new PersonHandler();

    AuthenticationHandler authenticationHandler = new AuthenticationHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/person", () -> {
                //before("/", authenticationHandler.authenticate);
                post("/", personHandler.createPerson);
                get("/", personHandler.getAllPersons);
                get("{id}", personHandler.getPersonById);
                put("{id}", personHandler.updatePersonById);
                delete("{id}", personHandler.deletePersonById);
            });
        };
    }
}
