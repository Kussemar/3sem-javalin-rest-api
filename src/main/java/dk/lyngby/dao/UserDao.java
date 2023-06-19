package dk.lyngby.dao;

import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import org.hibernate.SessionFactory;

public class UserDao {

    private static UserDao instance;

    private static SessionFactory sessionFactory;

    private UserDao() {}

    public static UserDao getInstance(SessionFactory _sessionFactory) {
        if (instance == null) {
            sessionFactory = _sessionFactory;
            instance = new UserDao();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws NotAuthorizedException{

        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            var user = session.get(User.class, username);
            if(user == null || !user.verifyPassword(password)){
                throw new NotAuthorizedException(401, "Invalid user name or password");
            }
            transaction.commit();
            return user;
        }
    }

    public User createUser(String username, String password, String user_role) throws NotAuthorizedException {
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            var user = new User(username, password);
            var role = session.get(Role.class, user_role);

            if(role == null){
                role = createRole(user_role);
            }

            user.addRole(role);
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            throw new NotAuthorizedException(400, "Username already exists");
        }
    }

    public Role createRole(String role){
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            var newRole = new Role(role);
            session.persist(newRole);
            transaction.commit();
            return newRole;
        }
    }
}
