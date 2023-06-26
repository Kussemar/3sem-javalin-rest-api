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
import dk.lyngby.exceptions.AuthorizationException;
import dk.lyngby.util.Variables;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TokenFactory {
    private static TokenFactory instance;
    private static final boolean isDeployed = (System.getenv("DEPLOYED") != null);
    private static final String ISSUER = isDeployed ? System.getenv("ISSUER") : Variables.ISSUER.getValue();
    private static final String TOKEN_EXPIRE_TIME = isDeployed ? System.getenv("TOKEN_EXPIRE_TIME") : Variables.TOKEN_EXPIRE_TIME.getValue();
    private static final String SECRET_KEY = isDeployed ? System.getenv("SECRET_KEY") : Variables.SECRET_KEY.getValue();

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
            return encryptToken(userName, rolesAsString, date);
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
            JWTClaimsSet claimsSet = decryptToken(token);

            if (new Date().after(claimsSet.getExpirationTime()))
                throw new AuthorizationException(401, "Token is expired");

            String username = claimsSet.getClaim("username").toString();
            String roles = claimsSet.getClaim("roles").toString();
            String[] rolesArray = roles.split(",");

            return new UserDTO(username, rolesArray);

        } catch (RuntimeException | ParseException | BadJOSEException | JOSEException e) {
            throw new ApiException(401, e.getMessage());
        }
    }

    private static String encryptToken(String userName, String rolesAsString, Date date) throws JOSEException {
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
        byte[] secretKey = SECRET_KEY.getBytes();
        DirectEncrypter encrypt = new DirectEncrypter(secretKey);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(encrypt);
        return jweObject.serialize();
    }

    private static JWTClaimsSet decryptToken(String token) throws JOSEException, BadJOSEException, ParseException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<>(SECRET_KEY.getBytes());
        JWEKeySelector<SimpleSecurityContext> jweKeySelector = new JWEDecryptionKeySelector<>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
        jwtProcessor.setJWEKeySelector(jweKeySelector);
        return jwtProcessor.process(token, null);
    }

    public static void main(String[] args) throws BadJOSEException, ParseException, JOSEException {
        JWTClaimsSet jwtClaimsSet = decryptToken("eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiZGlyIn0..ayArIv96Oxjr1Q_ImP5Fjw.PPwKemnf7K5uhFxuaTlhZ_-5bSvO2uv8R35c5u8PzNyVtGTx1tiAaRp-3lnMcWqXZvX1dZoNcvkhzuqQizSGC5ZHNkQ4urdM_RNj9k6Q2HQLR1nwslnuW8-WYO768yW7fyFdK7QPMuhDO-_CHDeKMw.SM1Jbm_9BfwmdECEgJvfAw");
        System.out.println(jwtClaimsSet);
    }
}
