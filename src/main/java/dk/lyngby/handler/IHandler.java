package dk.lyngby.handler;

import dk.lyngby.exceptions.ApiException;
import io.javalin.http.Context;

public interface IHandler<T> {
    void readEntity(Context ctx) throws ApiException;
    void readAllEntities(Context ctx) throws ApiException;
    void createEntity(Context ctx) throws ApiException;
    void updateEntity(Context ctx) throws ApiException;
    void deleteEntity(Context ctx) throws ApiException;
    boolean validateId(int id);
    T validateEntity(Context ctx);
}
