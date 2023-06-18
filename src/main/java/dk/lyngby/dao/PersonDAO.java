package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonDAO {

    private static PersonDAO instance;
    private static SessionFactory sessionFactory;

    private PersonDAO() {}

    public static PersonDAO getInstance(SessionFactory _sessionFactory) {
        if (instance == null) {
            sessionFactory = _sessionFactory;
            instance = new PersonDAO();
        }
        return instance;
    }

    public Person create(Person person) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }

    public List<Person> readAll() {
        try(Session session = sessionFactory.openSession()) {
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
        // TODO: Get person from database
        return new Person("John", "Doe", 25, "test@mail.com");
    }

    public Person update(Person updatedPerson) {
        // TODO: Update person in database
        return updatedPerson;
    }

    public void delete(int id) {
        // TODO: Delete person from database
    }
}
