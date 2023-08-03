//package dk.lyngby.dtos;
//
//import dk.lyngby.model.Person;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@NoArgsConstructor
//public class PersonIdDTO {
//
//    private int id, age;
//    private String firstName, lastName, email;
//
//    public PersonIdDTO(Person person) {
//        this.id = person.getId();
//        this.firstName = person.getFirstName();
//        this.lastName = person.getLastName();
//        this.age = person.getAge();
//        this.email = person.getEmail();
//    }
//
//    public static List<PersonIdDTO> toPersonIdDTOList(List<Person> personList) {
//        List<PersonIdDTO> personIdDTOList =  new ArrayList<>();
//        for (Person person : personList) {
//            personIdDTOList.add(new PersonIdDTO(person));
//        }
//        return personIdDTOList;
//    }
//}
