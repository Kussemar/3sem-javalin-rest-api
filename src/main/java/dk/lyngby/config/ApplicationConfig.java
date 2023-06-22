package dk.lyngby.config;

import dk.lyngby.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

public class ApplicationConfig {

    public static void configurations(JavalinConfig config) {
        // logging
        if (System.getenv("DEPLOYED") == null) {
            config.plugins.enableDevLogging(); // enables extensive development logging in terminal
        }

        // http
        config.http.defaultContentType = "application/json"; // default content type for requests

        // cors
        config.accessManager((handler, ctx, permittedRoles) -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.header("Access-Control-Allow-Credentials", "true");
            handler.handle(ctx);
        });

        // routing
        config.routing.contextPath = "/api/v1"; // base path for all routes

        // Route overview
        config.plugins.register(new RouteOverviewPlugin("/routes")); // enables route overview at /routes
    }

    public static void startServer(Javalin app, int port) {
        Routes routes = new Routes();
        app.updateConfig(ApplicationConfig::configurations);
        app.routes(routes.getRoutes(app));
        HibernateConfig.setTest(false);
        app.start(port);
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}
