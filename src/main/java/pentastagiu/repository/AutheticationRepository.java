package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Authentication;

@Repository
public interface AutheticationRepository extends CrudRepository<Authentication,Long> {

}
