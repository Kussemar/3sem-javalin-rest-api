package dk.lyngby.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.lyngby.model.entities.Person;
import dk.lyngby.service.dto.PersonDTO;
import io.javalin.http.Context;
import java.util.List;

public class PersonController {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void createPerson(Context ctx) {
        ctx.res().setStatus(201);
        PersonDTO personDTO = GSON.fromJson(ctx.body(), PersonDTO.class);
        Person person = new Person(personDTO.getEmail(), personDTO.getFirstName(), personDTO.getLastName());
        // TODO: Save person to database
        ctx.result(GSON.toJson(new PersonDTO(person)));
    }

    public static void getAllPersons(Context ctx) {
        ctx.res().setStatus(200);
        List<PersonDTO> personDTOS = List.of(
                new PersonDTO("John", "Doe", "doe@mail.com"),
                new PersonDTO("Michelle", "Schmidt", "schmidt@mail.com"));
        ctx.result(GSON.toJson(personDTOS));
    }

    public static void getPersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = Integer.parseInt(ctx.pathParam("id"));
        // TODO: Get person from database
        ctx.result("Not implemented yet");
    }

    public static void editPersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = Integer.parseInt(ctx.pathParam("id"));
        // TODO: Get person from database
        ctx.result("Not implemented yet");
    }

    public static void deletePersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = Integer.parseInt(ctx.pathParam("id"));
        // TODO: Get person from database
        ctx.result("Not implemented yet");
    }
}
