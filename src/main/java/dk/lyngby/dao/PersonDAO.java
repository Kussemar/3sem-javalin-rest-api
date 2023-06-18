package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonDAO {

    private final SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactory();

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
        // TODO: Get all persons from database
        return List.of(
                new Person("John", "Doe", 25, "doe@mail.com"),
                new Person("Michelle", "Schmidt", 25, "schmidt@mail.com"));
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
