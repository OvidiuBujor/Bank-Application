package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import pentastagiu.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    @Nullable
    User findByUsernameAndPassword(String username,String password);
}
