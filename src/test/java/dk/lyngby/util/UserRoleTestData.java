package dk.lyngby.util;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import org.hibernate.SessionFactory;

public class UserRoleTestData {

    static SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactory();

    public static void main(String[] args) {
        User user = new User("usertest", "user123");
        User admin = new User("admintest", "admin123");
        User superuser = new User("superusertest", "superuser123");

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        user.addRole(userRole);
        admin.addRole(adminRole);
        superuser.addRole(userRole);
        superuser.addRole(adminRole);

        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().createNamedQuery("Role.deleteAllRows", Role.class).executeUpdate();
            sessionFactory.getCurrentSession().createNamedQuery("User.deleteAllRows", User.class).executeUpdate();
            sessionFactory.getCurrentSession().persist(userRole);
            sessionFactory.getCurrentSession().persist(adminRole);
            sessionFactory.getCurrentSession().persist(user);
            sessionFactory.getCurrentSession().persist(admin);
            sessionFactory.getCurrentSession().persist(superuser);
            sessionFactory.getCurrentSession().getTransaction().commit();
        } finally {
            sessionFactory.getCurrentSession().close();
        }
    }
}
