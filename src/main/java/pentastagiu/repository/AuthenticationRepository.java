package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;

import java.util.Optional;

/**
 * This class is the repository class
 * for Authentication model class.
 */
@Repository
public interface AuthenticationRepository extends CrudRepository<Authentication,Long> {
    Optional<Authentication> findByToken(String token);

    Optional<Authentication> findByUser(User user);

    boolean existsByToken(String token);
}
