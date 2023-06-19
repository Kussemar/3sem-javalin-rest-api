package dk.lyngby.config;

import dk.lyngby.model.Person;
import dk.lyngby.model.Role;
import dk.lyngby.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateConfig {
    private static SessionFactory sessionConfigFactory;
    private static Boolean isTest = false;

    private static SessionFactory buildSessionConfigFactoryDev() {
        try {
            Configuration configuration = new Configuration();


            Properties props = new Properties();

            boolean isDeployed = (System.getenv("DEPLOYED") != null);

            if(isDeployed) {
                String DB_USERNAME = System.getenv("DB_USERNAME");
                String DB_PASSWORD = System.getenv("DB_PASSWORD");
                String DB_NAME = getDBName();
                String CONNECTION_STR = System.getenv("CONNECTION_STR") + DB_NAME;
                props.setProperty("hibernate.connection.url", CONNECTION_STR);
                props.setProperty("hibernate.connection.username", DB_USERNAME);
                props.setProperty("hibernate.connection.password", DB_PASSWORD);
            } else {
                props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/projectdb");
                props.put("hibernate.connection.username", "dev");
                props.put("hibernate.connection.password", "ax2");
            }
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            props.put("hibernate.archive.autodetection", "class");
            props.put("hibernate.current_session_context_class", "thread");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.hbm2ddl.auto", "update");

            // Hibernate Default Pool Configuration
            // https://www.mastertheboss.com/hibernate-jpa/hibernate-configuration/configure-a-connection-pool-with-hibernate/
            props.put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            // Maximum waiting time for a connection from the pool
            props.put("hibernate.hikari.connectionTimeout", "10000");
            // Minimum number of ideal connections in the pool
            props.put("hibernate.hikari.minimumIdle", "5");
            // Maximum number of actual connection in the pool
            props.put("hibernate.hikari.maximumPoolSize", "20");
            // Maximum time that a connection is allowed to sit ideal in the pool
            props.put("hibernate.hikari.idleTimeout", "200000");

            props.put("hibernate.format_sql", "true");
            props.put("hibernate.use_sql_comments", "true");

            configuration.setProperties(props);

            getAnnotationConfiguration(configuration);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionConfigFactoryTest() {
        try {
            Configuration configuration = new Configuration();

            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
            props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "postgres");
            props.put("hibernate.archive.autodetection", "class");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.hbm2ddl.auto", "create-drop");

            configuration.setProperties(props);

            getAnnotationConfiguration(configuration);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Role.class);
    }

    public static SessionFactory getSessionConfigFactoryDev() {
        if (sessionConfigFactory == null) sessionConfigFactory = buildSessionConfigFactoryDev();
        return sessionConfigFactory;
    }

    public static SessionFactory getSessionConfigFactoryTest() {
        if (sessionConfigFactory == null) sessionConfigFactory = buildSessionConfigFactoryTest();
        return sessionConfigFactory;
    }

    public static SessionFactory getSessionConfigFactory() {
        if (isTest) return getSessionConfigFactoryTest();
        return getSessionConfigFactoryDev();
    }

    public static void setTest(Boolean test) {
        isTest = test;
    }

    public static Boolean getTest() {
        return isTest;
    }

    private static String getDBName() {
        Properties pomProperties;
        InputStream is = HibernateConfig.class.getClassLoader().getResourceAsStream("properties-from-pom.properties");
        pomProperties = new Properties();
        try {
            pomProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pomProperties.getProperty("db.name");
    }
}
