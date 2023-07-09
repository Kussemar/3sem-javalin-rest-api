package dk.lyngby.handler;

import dk.lyngby.exceptions.ApiException;
import io.javalin.http.Context;

public interface IEntityHandler<T> {
    void read(Context ctx) throws ApiException;
    void readAll(Context ctx) throws ApiException;
    void create(Context ctx) throws ApiException;
    void update(Context ctx) throws ApiException;
    void delete(Context ctx) throws ApiException;
    boolean validateId(int number);
    T validateEntity(Context ctx);
}
