package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {

    private static PersonDAO personDAO;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        personDAO = new PersonDAO();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        Person person = new Person("John", "Doe", 25, "doe@mail.com");
        Person person1 = personDAO.create(person);
        assertEquals(person, person1);
    }

    @Test
    void readAll() {
    }

    @Test
    void read() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}