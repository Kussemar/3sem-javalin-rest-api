package dk.lyngby.handler;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.PersonDAO;
import dk.lyngby.dto.PersonIdDTO;
import dk.lyngby.dto.PersonDTO;
import dk.lyngby.dto.UserDTO;
import dk.lyngby.exceptions.ApiException;
import dk.lyngby.exceptions.Message;
import dk.lyngby.model.Person;
import io.javalin.http.Context;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonHandler {

    private final PersonDAO personDao;

    public PersonHandler() {
        SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactory();
        personDao = PersonDAO.getInstance(sessionFactory);
    }

    public void createPerson(Context ctx) throws ApiException {
        // request
        ctx.res().setStatus(201);
        // entity
        Person person = personDao.create(validatePerson(ctx).toPerson());
        // dto
        PersonDTO personDTO = new PersonDTO(person);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void getAllPersons(Context ctx) throws ApiException {
        ctx.res().setStatus(200);
        List<Person> persons = personDao.readAll();
        List<PersonIdDTO> personDTOS = PersonIdDTO.toPersonIdDTOList(persons);

        UserDTO user = ctx.attribute("user");
        System.out.println(user);

        ctx.json(personDTOS, PersonDTO.class);
    }

    public void getPersonById(Context ctx) throws ApiException {
        ctx.res().setStatus(200);
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        Person person = personDao.read(id);
        PersonDTO personDTO = new PersonDTO(person);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void updatePersonById(Context ctx) throws ApiException {
        ctx.res().setStatus(200);
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        Person update = personDao.update(id, validatePerson(ctx).toPerson());
        PersonDTO personDTO = new PersonDTO(update);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void deletePersonById(Context ctx) throws ApiException {
        ctx.res().setStatus(200);
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validateID, "Not a valid id").get();
        personDao.delete(id);
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
