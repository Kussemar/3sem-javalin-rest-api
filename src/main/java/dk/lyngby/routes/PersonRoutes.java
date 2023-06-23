package dk.lyngby.routes;

import dk.lyngby.handler.PersonHandler;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonHandler personHandler = new PersonHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/person", () -> {
                post("/", personHandler.createPerson, RouteRoles.ADMIN);
                get("/", personHandler.getAllPersons, RouteRoles.ANYONE);
                get("{id}", personHandler.getPersonById, RouteRoles.USER, RouteRoles.ADMIN, RouteRoles.ANYONE);
                put("{id}", personHandler.updatePersonById, RouteRoles.ADMIN);
                delete("{id}", personHandler.deletePersonById, RouteRoles.ADMIN);
            });
        };
    }
}
