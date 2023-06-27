package dk.lyngby.dao;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.model.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PersonDAO {

    private static PersonDAO instance;
    private static EntityManagerFactory emf;

    public static PersonDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonDAO();
        }
        return instance;
    }

    public Person create(Person person) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(500, "Could not create person");
        }
        return person;
    }

    public List<Person> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            List<Person> persons = em.createQuery("from Person", Person.class).getResultList();
            em.getTransaction().commit();
            return persons;
        } catch (Exception e) {
            throw new ApiException(500, "Could not read persons");
        }
    }

    public Person read(int id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, id);
            em.getTransaction().commit();
            return person;
        } catch (Exception e) {
            throw new ApiException(500, "Could not read person");
        }
    }

    public Person update(int id, Person person) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person _person = em.find(Person.class, id);
            _person.setFirstName(person.getFirstName());
            _person.setLastName(person.getLastName());
            _person.setAge(person.getAge());
            em.getTransaction().commit();
            return em.merge(person);
        } catch (Exception e) {
            throw new ApiException(500, "Could not update person");
        }
    }

    public void delete(int id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, id);
            em.remove(person);
            em.getTransaction().commit();
        } catch (Exception e) {
           throw new ApiException(500, "Could not delete person");
        }
    }

    public boolean validateID(int number) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, number);
            em.getTransaction().commit();
            return person != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
