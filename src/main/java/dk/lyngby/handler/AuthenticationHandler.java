package dk.lyngby.handler;

import dk.lyngby.dto.UserDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.security.TokenFactory;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

public class AuthenticationHandler {

    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

        public Handler authenticate = ctx -> {

            try{
                String token = ctx.header("Authorization").split(" ")[1];

                UserDTO userDTO = TOKEN_FACTORY.verifyToken(token);

                if (userDTO == null) {
                    throw new ApiException(401, "Invalid token");
                }

                ctx.attribute("user", userDTO);

            } catch (NullPointerException e) {
                throw new NotAuthorizedException(401, "No token provided");
            }
    };
}
