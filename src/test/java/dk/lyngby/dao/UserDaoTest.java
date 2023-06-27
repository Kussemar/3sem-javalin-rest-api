package dk.lyngby.dao;

import dk.lyngby.config.HibernateConfig;
import dk.lyngby.exceptions.AuthorizationException;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import dk.lyngby.util.TestData;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private static UserDao userDao;

    private static SessionFactory sessionConfigFactoryTest;

    @BeforeEach
    void setUp() {
        TestData.createUserTestData(sessionConfigFactoryTest);
    }

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);
        sessionConfigFactoryTest = HibernateConfig.getSessionConfigFactory();
        userDao = UserDao.getInstance(sessionConfigFactoryTest);
    }

    @AfterAll
    static void tearDownAll() {
        HibernateConfig.setTest(false);
    }

    @Test
    @DisplayName("Get instance of UserDao")
    void getInstance() {

        // given
        UserDao expected = UserDao.getInstance(sessionConfigFactoryTest);

        // when
        var actual = userDao;

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "user={0}, password={1}")
    @CsvSource(value = {"user, user123", "admin, admin123"})
    @DisplayName("Verify user with correct credentials")
    void getVerifiedUser(String user, String password) throws AuthorizationException {

        // given
        User expected = userDao.getVerifiedUser(user, password);

        // then
        assertNotNull(expected);
    }

    @Test
    @DisplayName("Verify user with incorrect credentials throws AuthorizationException")
    void getVerifiedUserWithIncorrectCredentials() {
        // when
        String user = "wrong";
        String password = "wrong";

        // then
        assertThrows(AuthorizationException.class, () -> userDao.getVerifiedUser(user, password));
    }

    @Test
    @DisplayName("Create user with a existing role")
    void createUser() throws AuthorizationException {

        // given
        User expected = userDao.createUser("test", "test123", "user");

        // when
        boolean actual = expected.getRoleList().contains(new Role("user"));

        // then
        assertTrue(actual);
    }

    @Test
    @DisplayName("Create user with a new role")
    void createUserWithNewRole() throws AuthorizationException {

        // given
        User expected = userDao.createUser("test", "test123", "manager");

        // when
        boolean actual = expected.getRoleList().contains(new Role("manager"));

        // then
        assertTrue(actual);
    }

    @Test
    @DisplayName("Create user with a existing username throws AuthorizationException")
    void createUserWithExistingUsername() {

        // when
        String username = "user";
        String password = "user123";
        String role = "user";

        // then
        assertThrows(AuthorizationException.class, () -> userDao.createUser(username, password, role));
    }

    @Test
    @DisplayName("Create a new role")
    void createRole() {

        // given
        Role actual = userDao.createRole("test");

        // when
        String expected = "test";

        // then
        assertEquals(expected, actual.getRoleName());

    }
}