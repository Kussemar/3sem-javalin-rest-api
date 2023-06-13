package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.controller.Routes;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(ApplicationConfig::configurations);

        app.routes(Routes.routes(app));

        ApplicationConfig.startServer(app, args.length > 0 ? Integer.parseInt(args[0]) : 7070);

    }
}