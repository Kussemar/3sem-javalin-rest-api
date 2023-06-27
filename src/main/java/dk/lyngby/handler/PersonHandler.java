package dk.lyngby.handler;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.PersonDAO;
import dk.lyngby.dto.PersonIdDTO;
import dk.lyngby.dto.PersonDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.Message;
import dk.lyngby.model.Person;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PersonHandler {

    private final PersonDAO personDao;

    public PersonHandler() {
        EntityManagerFactory emf= HibernateConfig.getEntityManagerFactory();
        personDao = PersonDAO.getInstance(emf);
    }

    public void createPerson(Context ctx) throws ApiException {
        // request
        PersonDTO jsonRequest = validatePerson(ctx);
        // entity
        Person person = personDao.create(jsonRequest.toPerson());
        // dto
        PersonDTO personDTO = new PersonDTO(person);
        // response
        ctx.res().setStatus(201);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void getAllPersons(Context ctx) throws ApiException {
        // entity
        List<Person> persons = personDao.readAll();
        // dto
        List<PersonIdDTO> personDTOS = PersonIdDTO.toPersonIdDTOList(persons);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTOS, PersonDTO.class);
    }

    public void getPersonById(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        // entity
        Person person = personDao.read(id);
        // dto
        PersonDTO personDTO = new PersonDTO(person);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void updatePersonById(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        // entity
        Person update = personDao.update(id, validatePerson(ctx).toPerson());
        // dto
        PersonDTO personDTO = new PersonDTO(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void deletePersonById(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        // entity
        personDao.delete(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(new Message(200, "Person with id " + id + " deleted"), Message.class);
    }

    private boolean validateID(int number) {
        return personDao.validateID(number);
    }

    private PersonDTO validatePerson(Context ctx) {
        return ctx.bodyValidator(PersonDTO.class)
                .check(p -> p.getAge() > 0 && p.getAge() < 120, "Age must be between 0 and 120")
                .check(p -> p.getFirstName().length() > 0, "First name must be longer than 0 characters")
                .check(p -> p.getLastName().length() > 0, "Last name must be longer than 0 characters")
                .check(p -> p.getEmail().matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$"), "Email must be valid")
                .get();
    }
}
