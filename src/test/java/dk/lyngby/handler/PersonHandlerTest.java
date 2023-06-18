package dk.lyngby.handler;

import dk.lyngby.Main;
import dk.lyngby.config.HibernateConfig;
import dk.lyngby.dao.PersonDAO;
import dk.lyngby.model.Person;
import io.javalin.Javalin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

class PersonHandlerTest {
    private static final SessionFactory sessionFactory = HibernateConfig.getSessionConfigFactoryTest();

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        Main.main(new String[]{"7777"});
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
        Javalin.create().stop();
    }

    @BeforeEach
    void setUp() {
        Person p1 = new Person("John", "Doe", 25, "doe@mail.com");
        Person p2 = new Person("Michelle", "Schmidt", 25, "schmidt@mail.com");
        Person p3 = new Person("Hans", "Hansen", 25, "hansen@mail.com");
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNamedQuery("Person.deleteAllRows").executeUpdate();
            session.persist(p1);
            session.persist(p2);
            session.persist(p3);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPerson() {
    }

    @Test
    void getAllPersons() {

        var responds = given()
                .when()
                .get("http://localhost:7777/api/v1/persons/")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.equalTo(3));

        responds.log().body();
    }

    @Test
    void getPersonById() {
    }

    @Test
    void updatePersonById() {
    }

    @Test
    void deletePersonById() {
    }
}