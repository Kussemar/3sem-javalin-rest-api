package dk.lyngby.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.dto.UserDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.AuthorizationException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TokenFactory {
    private static TokenFactory instance;
    private static final boolean isDeployed = (System.getenv("DEPLOYED") != null);
    private static final String ISSUER, TOKEN_EXPIRE_TIME, SECRET_KEY;

    static {
        try {
            ISSUER = isDeployed ? System.getenv("ISSUER") : ApplicationConfig.getProperty("issuer");
            TOKEN_EXPIRE_TIME = isDeployed ? System.getenv("TOKEN_EXPIRE_TIME") : ApplicationConfig.getProperty("token.expiration.time");
            SECRET_KEY = isDeployed ? System.getenv("SECRET_KEY") : ApplicationConfig.getProperty("secret.key");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private TokenFactory() {}

    public static TokenFactory getInstance() {
        if (instance == null) {
            instance = new TokenFactory();
        }
        return instance;
    }

    public String createToken(String userName, Set<String> roles) throws ApiException {

        try {
            StringBuilder res = new StringBuilder();
            for (String string : roles) {
                res.append(string);
                res.append(",");
            }

            String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";

            Date date = new Date();
            return signToken(userName, rolesAsString, date);
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

    public UserDTO verifyToken(String token) throws ApiException, AuthorizationException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new AuthorizationException(401, "Invalid token signature");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            if (new Date().after(claimsSet.getExpirationTime()))
                throw new AuthorizationException(401, "Token is expired");

            String username = claimsSet.getClaim("username").toString();
            String roles = claimsSet.getClaim("roles").toString();
            String[] rolesArray = roles.split(",");

            return new UserDTO(username, rolesArray);

        } catch (ParseException | JOSEException e) {
            throw new ApiException(401, e.getMessage());
        }
    }

    private static String signToken(String userName, String rolesAsString, Date date) throws JOSEException {
        // https://dzone.com/articles/using-nimbus-jose-jwt-in-spring-applications-why-a
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer(ISSUER)
                .claim("username", userName)
                .claim("roles", rolesAsString)
                .expirationTime(new Date(date.getTime() + Integer.parseInt(TOKEN_EXPIRE_TIME)))
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Signing failed", e);
        }

        return jwsObject.serialize();
    }
}
