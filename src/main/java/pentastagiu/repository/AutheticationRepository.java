package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;

import java.util.Optional;

@Repository
public interface AutheticationRepository extends CrudRepository<Authentication,Long> {
    Optional<Authentication> findByToken(String token);

    Optional<Authentication> findByUser(User user);
}
