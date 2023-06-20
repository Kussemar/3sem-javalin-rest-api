package dk.lyngby.config;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class ApplicationConfig {

    public static void configurations(JavalinConfig config) {
        // logging
        config.plugins.enableDevLogging(); // enables extensive development logging in terminal

        // http
        config.http.defaultContentType = "application/json"; // default content type for requests

        // cors
        config.accessManager((handler, ctx, permittedRoles) -> {
            System.out.println("Permitted roles: " + permittedRoles);
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.header("Access-Control-Allow-Credentials", "true");
            handler.handle(ctx);
        });

        // routing
        config.routing.contextPath = "/api/v1"; // base path for all routes
    }

    public static void startServer(Javalin app, int port) {
        HibernateConfig.setTest(false);
        app.start(port);
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}
