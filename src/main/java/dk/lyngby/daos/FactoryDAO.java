package dk.lyngby.daos;

import dk.lyngby.exceptions.ApiException;;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public abstract class FactoryDAO<T> {

    public T read(int id, Class<T> entityClass, EntityManagerFactory emf) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(entityClass, id);
        } catch (Exception e) {
            throw new ApiException(500, "Could not read object");
        }
    }

    public List<T> readAll(Class<T> entityClass, EntityManagerFactory emf) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
        } catch (Exception e) {
            throw new ApiException(500, "Could not read objects");
        }
    }

    public T create(T t, EntityManagerFactory emf) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(500, "Could not create object");
        }
        return t;
    }

    public T update(int id, T t, Class<T> entityClass, EntityManagerFactory emf) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            T t1 = em.find(entityClass, id);
            em.merge(t);
            em.getTransaction().commit();
            return t1;
        } catch (Exception e) {
            throw new ApiException(500, "Could not update object");
        }
    }

    public void delete(int id, Class<T> entityClass, EntityManagerFactory emf ) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            T t = em.find(entityClass, id);
            em.remove(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(500, "Could not delete person");
        }
    }

    public boolean validateId(int number, Class<T> entityClass, EntityManagerFactory emf ) {
        try (EntityManager em = emf.createEntityManager()) {
            T t = em.find(entityClass, number);
            return t != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
