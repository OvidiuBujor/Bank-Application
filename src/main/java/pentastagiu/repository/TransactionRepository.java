package pentastagiu.repository;

import org.springframework.data.repository.CrudRepository;
import pentastagiu.model.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {

    List<Transaction> getTransactionByUserID(int id);

}
