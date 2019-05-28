package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.convertor.TransactionType;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.Transaction;
import pentastagiu.model.User;
import pentastagiu.repository.TransactionRepository;
import pentastagiu.util.CustomException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    NotificationService notificationService;

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public void saveTransfer(Long accountFromId, Long accountToId, BigDecimal amount, String details){
        Account accountFrom = accountService.getAccountById(accountFromId);
        Account accountTo = accountService.getAccountById(accountToId);

        accountService.updateBalanceAccount(accountFrom.getId(), amount,false);
        accountService.updateBalanceAccount(accountTo.getId(), amount,true);

        saveTransaction(accountFrom, amount, accountTo, details, TransactionType.outgoing);
        saveTransaction(accountTo, amount, accountFrom, details, TransactionType.incoming);

        String transactionDetails = "From account: " + accountFrom.getAccountNumber() +
                " To account: " + accountTo.getAccountNumber() +
                ", amount : " + amount + " , details : " + details;

        notificationService.addNotification(accountFrom.getUser(),transactionDetails);

        System.out.println("Transfer to Account {Account Number: " + accountTo.getAccountNumber() +
                " New Balance: " + accountTo.getBalance().toString() + " " +
                accountTo.getAccountType().toString() +"}");
    }

    public List<Transaction> getTransactions(Authentication authentication){
        List<Account> accountList = accountService.getAccountsByToken(authentication.getToken());
        List<Transaction> transactionList = new ArrayList<>();
        for (Account account : accountList)
            transactionList.addAll(transactionRepository.getTransactionByAccountID(account));
        return transactionList;
    }

    public void saveTransaction(Account accountFrom, BigDecimal amount, Account accountTo, String details, TransactionType transactionType) {
        Transaction transactionToBeSaved = new Transaction();

        transactionToBeSaved.setAccount(accountTo.getAccountNumber());
        transactionToBeSaved.setAmount(amount);
        transactionToBeSaved.setDetails(details);
        transactionToBeSaved.setAccountID(accountFrom);
        transactionToBeSaved.setType(transactionType);

        transactionRepository.save(transactionToBeSaved);
    }



}
