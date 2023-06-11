package dk.lyngby;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Hello World And Welcome to the world of Javalin!"))
                .start(7070);
    }

}