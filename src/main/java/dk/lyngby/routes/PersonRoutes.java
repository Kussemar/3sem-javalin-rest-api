package dk.lyngby.routes;

import dk.lyngby.handler.PersonHandler;
import dk.lyngby.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonHandler personHandler = new PersonHandler();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/person", () -> {
                post("/", personHandler::createEntity, RouteRoles.ADMIN);
                get("/", personHandler::readAllEntities, RouteRoles.ANYONE);
                get("{id}", personHandler::readEntity, RouteRoles.USER, RouteRoles.ADMIN);
                put("{id}", personHandler::updateEntity, RouteRoles.ADMIN);
                delete("{id}", personHandler::deleteEntity, RouteRoles.ADMIN);
            });
        };
    }
}
