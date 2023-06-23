package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.util.Variables;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.startServer(Javalin.create(), Integer.parseInt(Variables.PORT.getValue()));
    }
}