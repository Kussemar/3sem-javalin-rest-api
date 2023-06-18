package dk.lyngby.model;

import io.javalin.http.Context;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPersonPK", nullable = false)
    private Integer id;


    @Column(name = "firstName", length = 45)
    private String firstName;

    @Column(name = "lastName", length = 45)
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", length = 45)
    private String email;

    public Person() {}

    public Person(String firstName, String lastName, Integer age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

     public Person(Context ctx) {
         this.firstName = ctx.formParam("firstName");
         this.lastName = ctx.formParam("lastName");
         this.age = Integer.parseInt(Objects.requireNonNull(ctx.formParam("age")));
         this.email = ctx.formParam("email");
     }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}