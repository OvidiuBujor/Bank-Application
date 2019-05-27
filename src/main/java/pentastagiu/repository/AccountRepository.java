package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.util.List;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account.
 */

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

    List<Account> getAccountByUser(User user);


}
