package dk.lyngby.util;

import dk.lyngby.model.Person;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TestData {
    public static int[] createPersonTestData(SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();

        Person p1 = new Person("Mike", "Durham", 25, "durham@mail.com");
        Person p2 = new Person("Steve", "Michell", 55, "michell@mail.com");
        Person p3 = new Person("Petra", "Schmidt", 41, "schmidt@mail.com");
        Person p4 = new Person("Anita", "Hansen", 18, "hansen@mail.com");

        try (session; Session session1 = session) {
            session1.getTransaction().begin();
            session1.createNamedQuery("Person.deleteAllRows").executeUpdate();
            session1.persist(p1);
            session1.persist(p2);
            session1.persist(p3);
            session1.persist(p4);
            session1.getTransaction().commit();
        }
        return new int[]{p1.getId(), p2.getId(), p3.getId(), p4.getId()};
    }

    public static void createUserTestData(SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();

        User user = new User("user", "user123");
        User admin = new User("admin", "admin123");
        User superuser = new User("superuser", "superuser123");

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        user.addRole(userRole);
        admin.addRole(adminRole);
        superuser.addRole(userRole);
        superuser.addRole(adminRole);

        try (session; Session session1 = session) {
            session1.beginTransaction();
            session1.createQuery("DELETE FROM User").executeUpdate();
            session1.createQuery("DELETE FROM Role").executeUpdate();
            session1.persist(userRole);
            session1.persist(adminRole);
            session1.persist(user);
            session1.persist(admin);
            session1.persist(superuser);
            session1.getTransaction().commit();
        }
    }
}