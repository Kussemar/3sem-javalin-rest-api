package dk.lyngby.handler;

import com.google.gson.JsonObject;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.UserDao;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.AuthorizationException;
import dk.lyngby.model.User;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

public class UserHandler {

    private final UserDao USER_DAO;
    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    public UserHandler() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        USER_DAO = UserDao.getInstance(emf);
    }

    public void login(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, true); // throws ApiException
        User user = getVerfiedOrCreaetUser(userInfos[0], userInfos[1], "", false); // throws AuthorizationException
        String token = getToken(userInfos[0], user.getRolesAsStrings()); // throws ApiException

        // Create response
        ctx.status(200);
        ctx.result(createResponse(userInfos[0], token));
    }

    public void register(Context ctx) throws ApiException, AuthorizationException {
        String[] userInfos = getUserInfos(ctx, false); // throws ApiException
        User user = getVerfiedOrCreaetUser(userInfos[0], userInfos[1], userInfos[2], true); // throws AuthorizationException
        String token = getToken(userInfos[0], user.getRolesAsStrings()); // throws ApiException

        // Create response
        ctx.res().setStatus(201);
        ctx.result(createResponse(userInfos[0], token));
    }

    private String createResponse(String username, String token) {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("username", username);
        responseJson.addProperty("token", token);
        return responseJson.toString();
    }

    private String[] getUserInfos(Context ctx, boolean tryLogin) throws ApiException {
        String request = ctx.body();
        return TOKEN_FACTORY.parseJsonObject(request, tryLogin);
    }

    private User getVerfiedOrCreaetUser(String username, String password, String role, boolean isCreate) throws AuthorizationException {
        return isCreate ? USER_DAO.createUser(username, password, role) : USER_DAO.getVerifiedUser(username, password);
    }

    private String getToken(String username, Set<String> userRoles) throws ApiException {
        return TOKEN_FACTORY.createToken(username, userRoles);
    }
}
