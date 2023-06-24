package dk.lyngby.util;

public enum Variables {

    // JWT Token
    SECRET_KEY("841D8A6C80CBA4FCAD32D5367C18C53B"),
    ISSUER("cphbusiness.dk"),
    TOKEN_EXPIRE_TIME("1800000"),


    DB_USERNAME("dev"),
    DB_PASSWORD("ax2"),
    DB_NAME("projectdb"),
    DB_CONNECTION_STRING("jdbc:postgresql://localhost:5432/"),

    // Javalin config
    PORT("7070");

    private final String value;

    Variables(String value) {this.value = value;}

    public String getValue() {
        return value;
    }

}
