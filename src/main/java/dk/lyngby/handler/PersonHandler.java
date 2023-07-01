package dk.lyngby.handler;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.daos.PersonDAO;
import dk.lyngby.dtos.PersonDTO;
import dk.lyngby.dtos.PersonIdDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.Message;
import dk.lyngby.model.Person;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PersonHandler implements IHandler<PersonDTO>{

    private final PersonDAO personDao;

    public PersonHandler() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        personDao = PersonDAO.getInstance(emf);
    }

    @Override
    public void readEntity(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateId, "Not a valid id").get();
        // entity
        Person person = personDao.read(id);
        // dto
        PersonDTO personDTO = new PersonDTO(person);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, PersonDTO.class);
    }

    @Override
    public void readAllEntities(Context ctx) throws ApiException {
        // entity
        List<Person> persons = personDao.readAll();
        // dto
        List<PersonIdDTO> personDTOS = PersonIdDTO.toPersonIdDTOList(persons);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTOS, PersonDTO.class);
    }

    @Override
    public void createEntity(Context ctx) throws ApiException {
        // request
        PersonDTO jsonRequest = validateEntity(ctx);
        // entity
        Person person = personDao.create(jsonRequest.toPerson());
        // dto
        PersonDTO personDTO = new PersonDTO(person);
        // response
        ctx.res().setStatus(201);
        ctx.json(personDTO, PersonDTO.class);
    }

    @Override
    public void updateEntity(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateId, "Not a valid id").get();
        // entity
        Person update = personDao.update(id, validateEntity(ctx).toPerson());
        // dto
        PersonDTO personDTO = new PersonDTO(update);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, PersonDTO.class);
    }

    @Override
    public void deleteEntity(Context ctx) throws ApiException {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateId, "Not a valid id").get();
        // entity
        personDao.delete(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(new Message(200, "Person with id " + id + " deleted"), Message.class);
    }

    @Override
    public boolean validateId(int id) {
        return personDao.validateId(id);
    }

    @Override
    public PersonDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(PersonDTO.class)
                .check(p -> p.getAge() > 0 && p.getAge() < 120, "Age must be between 0 and 120")
                .check(p -> p.getFirstName().length() > 0, "First name must be longer than 0 characters")
                .check(p -> p.getLastName().length() > 0, "Last name must be longer than 0 characters")
                .check(p -> p.getEmail().matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$"), "Email must be valid")
                .get();
    }
}
