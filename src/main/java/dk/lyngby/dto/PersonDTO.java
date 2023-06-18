package dk.lyngby.dto;

import dk.lyngby.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    private String firstName;
    private String lastName;
    private int age;
    private String email;

    public PersonDTO() {}

    public PersonDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.age = person.getAge();
        this.email = person.getEmail();
    }

    public PersonDTO(String firstName, String lastName, int age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public static List<PersonDTO> toPersonDTOs(List<Person> personList) {
        List<PersonDTO> personDTOList =  new ArrayList<>();
        for (Person person : personList) {
            personDTOList.add(new PersonDTO(person));
        }
        return personDTOList;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {return age;}

    public String getEmail() {
        return email;
    }

    public Person toPerson() {
        return new Person(firstName, lastName, age, email);
    }

}
