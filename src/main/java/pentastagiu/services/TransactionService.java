package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.convertor.OperationType;
import pentastagiu.convertor.TransactionType;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.Notification;
import pentastagiu.model.Transaction;
import pentastagiu.repository.TransactionRepository;
import pentastagiu.util.CustomException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private NotificationService notificationService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService,
                              NotificationService notificationService){
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.notificationService = notificationService;
    }

    public Notification saveTransfer(Long accountFromId, Long accountToId, BigDecimal amount, String details){
        Account accountFrom = accountService.getAccountById(accountFromId);
        Account accountTo = accountService.getAccountById(accountToId);
        if (accountService.validateAccountTypes(accountFrom.getAccountType(),accountTo.getAccountType())) {
            accountService.updateBalanceAccount(accountFrom.getId(), amount, OperationType.WITHDRAW);
            accountService.updateBalanceAccount(accountTo.getId(), amount, OperationType.DEPOSIT);

            saveTransaction(accountFrom, amount, accountTo, details, TransactionType.OUTGOING);
            saveTransaction(accountTo, amount, accountFrom, details, TransactionType.INCOMING);

            String transactionDetails = "From account: " + accountFrom.getAccountNumber() +
                    " To account: " + accountTo.getAccountNumber() +
                    ", amount : " + amount + " , details : " + details;

            System.out.println("Transfer From Account {Account Number: " + accountFrom.getAccountNumber() +
                    " New Balance: " + accountFrom.getBalance().toString() + " " +
                    accountFrom.getAccountType().toString() + "}");

            System.out.println("Transfer to Account {Account Number: " + accountTo.getAccountNumber() +
                    " New Balance: " + accountTo.getBalance().toString() + " " +
                    accountTo.getAccountType().toString() + "}");

            return notificationService.addNotification(accountFrom.getUser(), transactionDetails);
        }
            else
                throw new CustomException("Account types doesn't match. Transfer can't be executed.", HttpStatus.BAD_REQUEST);
    }

    public List<Transaction> getTransactions(Authentication authentication){
        List<Account> accountList = accountService.getAccountsByToken(authentication.getToken());
        List<Transaction> transactionList = new ArrayList<>();
        for (Account account : accountList)
            transactionList.addAll(transactionRepository.getTransactionByAccountID(account));
        return transactionList;
    }

    private void saveTransaction(Account accountFrom,
                                 BigDecimal amount,
                                 Account accountTo,
                                 String details,
                                 TransactionType transactionType) {
        Transaction transactionToBeSaved = new Transaction();

        transactionToBeSaved.setAccount(accountTo.getAccountNumber());
        transactionToBeSaved.setAmount(amount);
        transactionToBeSaved.setDetails(details);
        transactionToBeSaved.setAccountID(accountFrom);
        transactionToBeSaved.setType(transactionType);

        transactionRepository.save(transactionToBeSaved);
    }



}
