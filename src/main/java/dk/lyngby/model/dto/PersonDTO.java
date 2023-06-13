package dk.lyngby.model.dto;

import dk.lyngby.model.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    private int id;
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

    public static List<PersonDTO> getPersonDTOs(List<Person> personList) {
        List<PersonDTO> personDTOList =  new ArrayList<>();
        for (Person person : personList) {
            personDTOList.add(new PersonDTO(person));
        }
        return personDTOList;
    }

    public int getId() {return id;}
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

}
