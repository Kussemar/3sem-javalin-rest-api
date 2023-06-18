package dk.lyngby.handler;

import dk.lyngby.model.Person;
import io.javalin.Javalin;
import io.javalin.validation.ValidationError;
import io.javalin.validation.Validator;

import java.util.List;
import java.util.Map;

public class JavalinTest {
    public static void main(String[] args) {


        Javalin app = Javalin.create().start(7071);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            System.out.println("Before");
            System.out.println(ctx.url());
            System.out.println(ctx.scheme());
        });
        app.get("/", ctx -> {
            ctx.result("Hello World");
            ctx.cookieStore().set("username", "jens");
            ctx.cookie("username", "jens", 60 * 60 * 24 * 365);
        });
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            String user = ctx.cookieStore().get("username");
            System.out.println("Cookie: " + user);
        });
        app.get("/hello/{name}", ctx -> {
            ctx.result("Hello " + ctx.pathParam("name"));
            String url = ctx.url();
            System.out.println(url);
            System.out.println(ctx.fullUrl());


        });
        app.get("/number/", ctx -> {
            Integer myValue = ctx.queryParamAsClass("value", Integer.class).getOrDefault(788); // validate value
            ctx.result("Number: " + myValue);
            System.out.println(myValue);

        });
        app.post("/multiply/", ctx -> {
            Validator<String> stringValidator = ctx.queryParamAsClass("word", String.class)
                    .check(n -> !n.contains("-"), "ILLEGAL_CHARACTER");
            System.out.println("stringValidator: " + stringValidator);
            Map<String, List<ValidationError<String>>> errors = stringValidator.errors();
            System.out.println("ERRORS: " + errors);
            stringValidator.get();
        });

        app.post("/person/", ctx -> {
            Person person = ctx.bodyValidator(Person.class)
                    .check(p -> p.getAge() > 0 && p.getAge() < 150, "Age must be between 0 and 150")
                    .get();
            System.out.println(person.getFirstName());
        });
        app.after(ctx -> {
            System.out.println("After");
        });
        app.exception(Exception.class, (e, ctx) -> {
            System.out.println("Exception");
            System.out.println(e.getMessage());
            ctx.status(500);
            ctx.result("Something went wrong");
        });

    }
}
