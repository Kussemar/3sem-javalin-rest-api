package dk.lyngby.dtos;

import dk.lyngby.model.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonDTO {

    private String firstName;
    private String lastName;
    private int age;
    private String email;

    public PersonDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.age = person.getAge();
        this.email = person.getEmail();
    }

    public Person toPerson() {
        return new Person(firstName, lastName, age, email);
    }

}
