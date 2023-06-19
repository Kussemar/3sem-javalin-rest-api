package dk.lyngby.handler;

import com.google.gson.JsonObject;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.UserDao;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.model.User;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import org.hibernate.SessionFactory;

public class LoginHandler {

    private final UserDao USER_DAO;
    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    public LoginHandler() {
        SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactory();
        USER_DAO = UserDao.getInstance(sessionFactory);
    }

    public void login(Context ctx) {
        try {
            String request = ctx.body();
            String[] userInfos = TOKEN_FACTORY.parseJsonObject(request, true);
            User user = USER_DAO.getVerifiedUser(userInfos[0], userInfos[1]);
            String token = TOKEN_FACTORY.createToken(userInfos[0], user.getRolesAsStrings());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", userInfos[0]);
            responseJson.addProperty("token", token);
            ctx.result(responseJson.toString());
        } catch (ApiException  e) {
            ctx.status(e.getStatusCode());
            ctx.json(new ApiException(e.getStatusCode(), e.getLocalizedMessage()));
        } catch (NotAuthorizedException e) {
            ctx.status(e.getStatusCode());
            ctx.json(new NotAuthorizedException(e.getStatusCode(), e.getMessage()));
        }
    }

}
