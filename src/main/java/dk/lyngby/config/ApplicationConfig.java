package dk.lyngby.config;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class ApplicationConfig {

    public static void configurations(JavalinConfig config) {
    // plugins

        //cors
        config.plugins.enableCors(cors -> {
            cors.add(CorsPluginConfig::anyHost);
            cors.corsConfigs();
        });
        config.plugins.enableDevLogging(); // enables extensive development logging in terminal

    // http
        config.http.defaultContentType = "application/json"; // default content type for requests

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
