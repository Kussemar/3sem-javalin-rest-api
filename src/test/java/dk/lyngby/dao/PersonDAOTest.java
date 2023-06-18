package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.model.Person;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {

    private static PersonDAO personDAO;

    @BeforeAll
    static void setUpAll() {
        SessionFactory sessionConfigFactoryTest = HibernateConfig.getSessionConfigFactoryTest();
        personDAO = PersonDAO.getInstance(sessionConfigFactoryTest);
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
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