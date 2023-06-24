package dk.lyngby.dto;

import dk.lyngby.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getPersonWithId(int id, Person person) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("person", new PersonDTO(person));
        return result;
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
