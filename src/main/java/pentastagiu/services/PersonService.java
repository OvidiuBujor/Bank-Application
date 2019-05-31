package pentastagiu.services;

import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.repository.PersonRepository;

import java.util.Optional;

/**
 * This class handles the Person operations
 */
@Service
public class PersonService {

    private PersonRepository personRepository;

    private AuthenticationService authenticationService;

    public PersonService(PersonRepository personRepository,
                         AuthenticationService authenticationService){
        this.personRepository = personRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * This method returns the Person corresponding
     * to the passed id as parameter
     * @param id of the Person to be returned
     * @return the corresponding Person if the person exists,
     * an empty Person object otherwise
     */
    public Person getPersonDetails(Long id){
        Optional<Person> personDetails = personRepository.findById(id);
        return personDetails.orElseGet(Person::new);
    }

    public Person savePersonDetails(Person person,String token){
        Person personToBeSaved = new Person();
        Authentication authentication = authenticationService.findByToken(token);
        User user = authentication.getUser();
        if(personRepository.existsByUser(user))
            personToBeSaved = personRepository.findById(user.getDetails().getId()).get();

        if(person.getAddress() != null)
            personToBeSaved.setAddress(person.getAddress());
        if(person.getEmail() != null)
            personToBeSaved.setEmail(person.getEmail());
        if(person.getFirstName() != null)
            personToBeSaved.setFirstName(person.getFirstName());
        if(person.getLastName() != null)
        personToBeSaved.setLastName(person.getLastName());
        personToBeSaved.setUser(user);
        return personRepository.save(personToBeSaved);
    }
}
