package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import pentastagiu.model.Person;
import pentastagiu.model.User;

/**
 * This class is the repository class
 * for Person model class.
 */
public interface PersonRepository extends CrudRepository<Person, Long> {

    boolean existsByUser(User user);
}
