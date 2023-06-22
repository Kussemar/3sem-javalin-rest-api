package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import io.javalin.Javalin;

public class Main {


    public static void main(String[] args) {
        ApplicationConfig.startServer(Javalin.create(), 7070);
    }
}