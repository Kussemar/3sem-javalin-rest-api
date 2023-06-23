package dk.lyngby.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import dk.lyngby.dto.UserDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.NotAuthorizedException;
import dk.lyngby.util.Variables;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TokenFactory {
    private static TokenFactory instance;

    private TokenFactory() {}

    public static TokenFactory getInstance() {
        if (instance == null) {
            instance = new TokenFactory();
        }
        return instance;
    }

    private String returnSecretKey() {
        boolean isDeployed = (System.getenv("DEPLOYED") != null);

        if(isDeployed) {
            return System.getenv("SECRET_KEY");
        }
        return Variables.SECRET_KEY.getValue();
    }

    public String createToken(String userName, Set<String> roles) throws ApiException {

        String ISSUER;
        String SECRET_KEY = returnSecretKey();
        String TOKEN_EXPIRE_TIME;

        boolean isDeployed = (System.getenv("DEPLOYED") != null);

        if (isDeployed) {
            ISSUER = System.getenv("ISSUER");
            TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
        } else {
            ISSUER = Variables.ISSUER.getValue();
            TOKEN_EXPIRE_TIME = Variables.TOKEN_EXPIRE_TIME.getValue();
        }

        try {
            StringBuilder res = new StringBuilder();
            for (String string : roles) {
                res.append(string);
                res.append(",");
            }

            String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";

            Date date = new Date();
            // https://dzone.com/articles/using-nimbus-jose-jwt-in-spring-applications-why-a
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userName)
                    .issuer(ISSUER)
                    .claim("username", userName)
                    .claim("roles", rolesAsString)
                    .expirationTime(new Date(date.getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                    .build();

            Payload payload = new Payload(claims.toJSONObject());
            JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

            DirectEncrypter encrypter = new DirectEncrypter(SECRET_KEY.getBytes());

            JWEObject jweObject = new JWEObject(header, payload);
            jweObject.encrypt(encrypter);

            return jweObject.serialize();
        } catch (JOSEException e) {
            throw new ApiException(500, "Could not create token");
        }
    }

    public String[] parseJsonObject(String jsonString, Boolean tryLogin) throws ApiException {
        try {
            List<String> roles = Arrays.asList("user", "admin");
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            String role = "";

            if (!tryLogin) {
                role = json.get("role").getAsString();
                if (!roles.contains(role)) throw new ApiException(400, "Role not valid");
            }

            return new String[]{username, password, role};

        } catch (JsonSyntaxException | NullPointerException e) {
            throw new ApiException(400, "Malformed JSON Supplied");
        }
    }

    public UserDTO verifyToken(String token) throws ApiException, NotAuthorizedException {

        String SECRET_KEY = returnSecretKey();

        try {
            ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<>(SECRET_KEY.getBytes());
            JWEKeySelector<SimpleSecurityContext> jweKeySelector = new JWEDecryptionKeySelector<>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
            jwtProcessor.setJWEKeySelector(jweKeySelector);
            JWTClaimsSet claimsSet = jwtProcessor.process(token, null);

            if (new Date().after(claimsSet.getExpirationTime())) throw new NotAuthorizedException(401, "Token is expired");

            String username = claimsSet.getClaim("username").toString();
            String roles = claimsSet.getClaim("roles").toString();

            String[] rolesArray = roles.split(",");

            return new UserDTO(username, rolesArray);

        } catch (RuntimeException | ParseException | BadJOSEException | JOSEException e) {
            throw new ApiException(401, e.getMessage());
        }
    }
}
