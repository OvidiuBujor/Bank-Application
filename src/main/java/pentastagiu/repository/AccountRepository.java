package pentastagiu.repository;

//import org.hibernate.Session;
//import org.hibernate.resource.transaction.spi.TransactionStatus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Account;

import java.util.List;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account,Integer> {

    List<Account> getAccountByuserID(Integer id);

}
