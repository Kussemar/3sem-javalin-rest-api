package dk.lyngby.dao;

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

    public Person create(Person person) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }

    public List<Person> readAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Person> persons = session.createQuery("from Person", Person.class).list();
            session.getTransaction().commit();
            return persons;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person read(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.getTransaction().commit();
            return person;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person update(Person updatedPerson, int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            person.setFirstName(updatedPerson.getFirstName());
            person.setLastName(updatedPerson.getLastName());
            person.setAge(updatedPerson.getAge());
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedPerson;
    }

    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Person person = session.get(Person.class, id);
            session.remove(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
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
