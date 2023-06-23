package dk.lyngby.handler;

import dk.lyngby.dto.UserDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.routes.RouteRoles;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AccessManagerHandler {

    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    // Roles Allowed for routes
    public void accessManagerHandler(Handler handler, Context ctx, Set<? extends RouteRole> permittedRoles) throws Exception {
        String path = ctx.path();
        boolean isAuthorized = false;

        if (path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/register") || path.equals("/api/v1/routes") || permittedRoles.contains(RouteRoles.ANYONE)) {
            handler.handle(ctx);
            return;
        } else {
            RouteRole[] userRole = getUserRole(ctx);
            for (RouteRole role : userRole) {
                if (permittedRoles.contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (isAuthorized) {
            handler.handle(ctx);
        } else {
            throw new NotAuthorizedException(401, "Not authorized");
        }
    }

    public RouteRole[] getUserRole(Context ctx) throws NotAuthorizedException, ApiException {
        String token = ctx.header("Authorization").split(" ")[1];
        UserDTO userDTO = TOKEN_FACTORY.verifyToken(token);

        if (userDTO == null) {
            throw new ApiException(401, "Invalid token");
        }

        return userDTO.getRoles().stream().map(r -> RouteRoles.valueOf(r.toUpperCase())).toArray(RouteRole[]::new);
    }

    // this handler can be used in connection with before() to authenticate a user
    public void beforeAuthenticateHandler(Context ctx) throws NotAuthorizedException, ApiException {

        try {
            String token = ctx.header("Authorization").split(" ")[1];
            UserDTO userDTO = TOKEN_FACTORY.verifyToken(token);

            if (userDTO == null) {
                throw new ApiException(401, "Invalid token");
            }

            ctx.attribute("user", userDTO);

        } catch (NullPointerException e) {
            throw new NotAuthorizedException(401, "No token provided");
        }
    }
}
