package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Transaction;
import pentastagiu.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

//    public List<Transaction> getTransactions(Authentication authentication){
//        return transactionRepository.getTransactionByAccountID(authentication.getUser().getId());
//    }
}
