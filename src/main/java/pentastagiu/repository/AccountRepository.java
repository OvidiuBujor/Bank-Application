package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.util.List;
import java.util.Optional;

/**
 * This class is the repository class
 * for Account model class.
 */

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

    /**
     * This method gets the accounts list for the
     * user.
     * @param user the user for which to return the accounts list
     * @return the list of account for the user
     */
    List<Account> getAccountByUser(User user);

    Optional<Account> findByAccountNumber(String accountNumber);
}
