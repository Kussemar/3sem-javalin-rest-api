package dk.lyngby.service.dto;

import dk.lyngby.model.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDTO {

    private int id;
    private final String firstName;
    private final String lastName;
    private final String email;

    public PersonDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
    }

    public PersonDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }

}
