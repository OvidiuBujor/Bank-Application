package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Authentication;
import pentastagiu.model.Transaction;
import pentastagiu.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(Authentication authentication){
        return transactionRepository.getTransactionByUserID(authentication.getUser().getId());
    }
}
