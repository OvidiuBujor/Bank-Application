package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pentastagiu.model.Account;
import pentastagiu.model.Transaction;

import java.util.List;

/**
 * This class is the repository class
 * for Transaction model class.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {

    List<Transaction> getTransactionByAccountID(Account account);
}
