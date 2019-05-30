package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import pentastagiu.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
