package pentastagiu.services;

import org.springframework.stereotype.Service;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.repository.PersonRepository;

import java.util.Optional;

@Service
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public Person getPersonDetails(Long id){
        Optional<Person> personDetails = personRepository.findById(id);
        return personDetails.orElseGet(Person::new);
    }
}
