package pentastagiu.services;

import org.springframework.stereotype.Service;
import pentastagiu.exceptions.EmailAddressNotValidException;
import pentastagiu.model.Authentication;
import pentastagiu.model.Person;
import pentastagiu.model.User;
import pentastagiu.repository.PersonRepository;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
        if(person.getEmail() != null) {
            try {
                InternetAddress internetAddress = new InternetAddress(person.getEmail());
                internetAddress.validate();
                personToBeSaved.setEmail(person.getEmail());
            } catch (AddressException e) {
                throw new EmailAddressNotValidException("Email address is not valid.");
            }
        }
        if(person.getFirstName() != null)
            personToBeSaved.setFirstName(person.getFirstName());
        if(person.getLastName() != null)
        personToBeSaved.setLastName(person.getLastName());
        personToBeSaved.setUser(user);
        return personRepository.save(personToBeSaved);
    }
}
