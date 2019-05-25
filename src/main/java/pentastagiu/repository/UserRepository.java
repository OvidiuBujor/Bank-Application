package pentastagiu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pentastagiu.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Nullable
    User findByUsernameAndPassword(String username,String password);

    @Nullable
    User findByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update User u set u.password = :password where u.id = :id", nativeQuery  = true)
    void updateUser(@Param("password") String password, @Param("id") Long id);
}
