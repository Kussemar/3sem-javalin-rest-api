package dk.lyngby.handler;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.PersonDAO;
import dk.lyngby.dto.PersonDTO;
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

    public void createPerson(Context ctx) {
        ctx.res().setStatus(201);
        Person person = personDao.create(validatePerson(ctx).toPerson());
        PersonDTO personDTO = new PersonDTO(person);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void getAllPersons(Context ctx) {
        ctx.res().setStatus(200);
        List<Person> persons = personDao.readAll();
        List<PersonDTO> personDTOS = PersonDTO.toPersonDTOs(persons);
        ctx.json(personDTOS, PersonDTO.class);
    }

    public void getPersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = Integer.parseInt(ctx.pathParam("id"));
        Person person = personDao.read(id);
        PersonDTO personDTO = new PersonDTO(person);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void updatePersonById(Context ctx) {
        ctx.res().setStatus(200);
        int id = ctx.queryParamAsClass("id", Integer.class).check(this::validateID, "Object can not be found.").get();
        Person update = personDao.update(validatePerson(ctx).toPerson());
        PersonDTO personDTO = new PersonDTO(update);
        ctx.json(personDTO, PersonDTO.class);
    }

    public void deletePersonById(Context ctx) {
        ctx.res().setStatus(204);
        int id = ctx.queryParamAsClass("id", Integer.class).check(this::validateID, "Object can not be found.").get();
        personDao.delete(id);
        ctx.result("Not implemented yet ");
    }

    private boolean validateID(int number) {
        // TODO: Validate ID else return false
        return true;
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
