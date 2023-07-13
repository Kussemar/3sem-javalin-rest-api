package dk.lyngby.daos;

import dk.lyngby.exceptions.ApiException;
import dk.lyngby.model.Person;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PersonDAO extends FactoryDAO<Person, Integer> implements IDAO<Person, Integer> {

    private static PersonDAO instance;
    private static EntityManagerFactory emf;

    public static PersonDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonDAO();
        }
        return instance;
    }

    @Override
    public Person read(Integer id) throws ApiException {
        return super.read(id, Person.class, emf);
    }

    @Override
    public List<Person> readAll() throws ApiException {
        return super.readAll(Person.class, emf);
    }

    @Override
    public Person create(Person person) throws ApiException {
        return super.create(person, emf);
    }

    @Override
    public Person update(Integer id, Person person) throws ApiException {
        return super.update(id, person, Person.class, emf);
    }

    @Override
    public void delete(Integer id) throws ApiException {
        super.delete(id, Person.class, emf);
    }

    @Override
    public boolean validatePrimaryKey(Integer number) {
        return super.validatePrimaryKey(number, Person.class, emf);
    }

}
