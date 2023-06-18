package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.routes.Routes;
import io.javalin.Javalin;

public class Main {

    public static Routes routes = new Routes();

    public static void main(String[] args) {

        Javalin app = Javalin.create(ApplicationConfig::configurations);

        app.routes(routes.getRoutes(app));

        ApplicationConfig.startServer(app, args.length > 0 ? Integer.parseInt(args[0]) : 7070);

    }
}