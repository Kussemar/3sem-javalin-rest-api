package dk.lyngby.daos;

import dk.lyngby.exceptions.ApiException;

import java.util.List;

public interface IDAO<T> {
    T read(int id) throws ApiException;
    List<T> readAll() throws ApiException;
    T create(T t) throws ApiException;
    T update(int id, T t) throws ApiException;
    void delete(int id) throws ApiException;
    boolean validateId(int id);
}
