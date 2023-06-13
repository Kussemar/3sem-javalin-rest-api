package dk.lyngby.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.lyngby.model.entities.Person;
import dk.lyngby.model.dto.PersonDTO;
import io.javalin.http.Context;
import io.javalin.validation.BodyValidator;

import java.util.List;

public class PersonController {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected static void createPerson(Context ctx) {
        ctx.res().setStatus(201);
        PersonDTO persondto = validatePerson(ctx);
        // TODO: Save person to database
        ctx.result(GSON.toJson(persondto));
    }

    protected static void getAllPersons(Context ctx) {
        ctx.res().setStatus(200);
        List<PersonDTO> personDTOS = List.of(
                new PersonDTO("John", "Doe", 25, "doe@mail.com"),
                new PersonDTO("Michelle", "Schmidt", 25, "schmidt@mail.com"));
        ctx.result(GSON.toJson(personDTOS));
    }

    protected static void getPersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = Integer.parseInt(ctx.pathParam("id"));
        // TODO: Get person from database
        ctx.result("Not implemented yet");
    }

    protected static void editPersonById(Context ctx) {
        ctx.res().setStatus(200);
        Integer id = ctx.queryParamAsClass("id", Integer.class).check(PersonController::validateAge, "ID must be greater than 0").get();
        // TODO: Get person from database
        ctx.result("Not implemented yet");
    }

    protected static void deletePersonById(Context ctx) {
        ctx.res().setStatus(200);
        Integer id = ctx.queryParamAsClass("id", Integer.class).check(PersonController::validateAge, "ID must be greater than 0").get();
        // TODO: Get person from database
        ctx.result("Not implemented yet ");
    }

    private static boolean validateAge(int number) {
        return number < 0 || number > 120;
    }

    private static PersonDTO validatePerson(Context ctx) {
        return ctx.bodyValidator(PersonDTO.class)
                .check(p -> p.getAge() > 0 && p.getAge() < 120, "Age must be between 0 and 120")
                .check(p -> p.getFirstName().length() > 0, "First name must be longer than 0 characters")
                .check(p -> p.getLastName().length() > 0, "Last name must be longer than 0 characters")
                .check(p -> p.getEmail().matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$"), "Email must be valid")
                .get();
    }
}
