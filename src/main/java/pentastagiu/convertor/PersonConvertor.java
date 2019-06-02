package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.DTOs.PersonDTO;
import pentastagiu.model.Person;

@Component
public class PersonConvertor {
    public PersonDTO convertToPersonDTO(Person person){
        return new PersonDTO(person.getUser().getUsername(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getAddress());
    }
}
