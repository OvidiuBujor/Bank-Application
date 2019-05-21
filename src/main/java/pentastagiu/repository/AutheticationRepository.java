package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import pentastagiu.model.Authentication;

public interface AutheticationRepository extends CrudRepository<Authentication,Integer> {

}
