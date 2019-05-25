package pentastagiu.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import pentastagiu.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Nullable
    User findByUsernameAndPassword(String username,String password);

    @Nullable
    User findByUsername(String username);

    @Query(value = "update User u set u.password = :password where u.id = :id", nativeQuery  = true)
    default User updateUser(@Param("password") String password, @Param("id") Long id){
        Optional<User> userToReturn = findById(id);
        userToReturn.ifPresent(user -> user.setPassword(password));
        return userToReturn.orElseGet(User::new);
    }

}
