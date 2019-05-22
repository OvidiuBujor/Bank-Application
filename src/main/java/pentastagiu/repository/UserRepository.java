package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    @Nullable
    User findByUsername(String Username);

    @Nullable
    User findByUsernameAndPassword(String username,String password);

    List<Account> findByUserId(int ID);

    void delete(Long id);

    User getById(Long id);
}
