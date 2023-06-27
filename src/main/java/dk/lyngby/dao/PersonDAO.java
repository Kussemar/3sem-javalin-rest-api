package dk.lyngby.dao;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonDAO {

    private static PersonDAO instance;

    private static SessionFactory sessionFactory;

    private PersonDAO() {
    }

    public static PersonDAO getInstance(SessionFactory _sessionFactory) {
        if (instance == null) {
            sessionFactory = _sessionFactory;
            instance = new PersonDAO();
        }
        return instance;
    }

    public Person create(Person person) throws ApiException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(500, "Could not create person");
        }
        return person;
    }

    public List<Person> readAll() throws ApiException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Person> persons = session.createQuery("from Person", Person.class).list();
            session.getTransaction().commit();
            return persons;
        } catch (Exception e) {
            throw new ApiException(500, "Could not read persons");
        }
    }

    public Person read(int id) throws ApiException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.getTransaction().commit();
            return person;
        } catch (Exception e) {
            throw new ApiException(500, "Could not read person");
        }
    }

    public Person update(int id, Person person) throws ApiException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person _person = session.get(Person.class, id);
            _person.setFirstName(person.getFirstName());
            _person.setLastName(person.getLastName());
            _person.setAge(person.getAge());
            session.getTransaction().commit();
            return session.merge(person);
        } catch (Exception e) {
            throw new ApiException(500, "Could not update person");
        }
    }

    public void delete(int id) throws ApiException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.remove(person);
            session.getTransaction().commit();
        } catch (Exception e) {
           throw new ApiException(500, "Could not delete person");
        }
    }

    public boolean validateID(int number) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, number);
            session.getTransaction().commit();
            return person != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
