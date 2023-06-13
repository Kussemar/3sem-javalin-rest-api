package dk.lyngby.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class ApplicationConfig {

    public static void configurations(JavalinConfig config) {
    // plugins

        // logging
        // config.plugins.enableDevLogging(); // enables extensive development logging in terminal

    // http
        config.http.defaultContentType = "application/json"; // default content type for requests
        config.accessManager((handler, ctx, permittedRoles) -> { // access manager for all routes
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            handler.handle(ctx);
        });

    // routing
        config.routing.contextPath = "/api/v1"; // base path for all routes
    }

    public static void startServer(Javalin app, int port) {
        app.start(port);
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}
