package dk.lyngby.handler;

import com.google.gson.JsonObject;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.UserDao;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.model.User;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Handler;
import org.hibernate.SessionFactory;

public class RegisterHandler {

    private UserDao USER_DAO;
    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    public RegisterHandler() {
        SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactory();
        USER_DAO = UserDao.getInstance(sessionFactory);
    }

    public Handler register = ctx -> {
        try {
            String request = ctx.body();
            System.out.println("request: " + request);
            String[] userInfos = TOKEN_FACTORY.parseJsonObject(request, false);

            User user = USER_DAO.createUser(userInfos[0], userInfos[1], userInfos[2]);
            System.out.println("user: " + user);
            String token = TOKEN_FACTORY.createToken(userInfos[0], user.getRolesAsStrings());
            System.out.println("token: " + token);
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", userInfos[0]);
            responseJson.addProperty("token", token);
            ctx.status(201);
            ctx.result(responseJson.toString());
        } catch (ApiException e) {
            ctx.status(e.getStatusCode());
            System.out.println("ApiException: " + e.getStatusCode());
            ctx.json(new ApiException(e.getStatusCode(), e.getMessage()));
        } catch (NotAuthorizedException e) {
            ctx.status(e.getStatusCode());
            System.out.println("NotAuthorizedException: " + e.getStatusCode());
            ctx.json(new NotAuthorizedException(e.getStatusCode(), e.getMessage()));
        }
    };
}
