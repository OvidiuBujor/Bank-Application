package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.convertor.OperationType;
import pentastagiu.convertor.TransactionType;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.Notification;
import pentastagiu.model.Transaction;
import pentastagiu.repository.TransactionRepository;
import pentastagiu.exceptions.AccountTypesMismatchException;
import pentastagiu.exceptions.CustomException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all the transaction
 * operations
 */
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

    /**
     * This method returns the corresponding notification
     * of a transfer operation between 2 accounts.
     * @param accountNumberFrom the id of the account from which
     *                      the amount is transferred
     * @param accountNumberTo the id of the account where the amount
     *                    is deposited
     * @param amount that is transferred
     * @param details of the transfer
     * @return the corresponding notification of the transfer
     * @throws CustomException in case account types don't match
     */
    public Notification saveTransfer(String accountNumberFrom, String accountNumberTo, BigDecimal amount, String details){
        Account accountFrom = accountService.getAccountByAccountNumber(accountNumberFrom);
        Account accountTo = accountService.getAccountByAccountNumber(accountNumberTo);
        if (accountService.validateAccountTypes(accountFrom.getAccountType(),accountTo.getAccountType())) {

            accountService.updateBalanceAccount(accountFrom.getAccountNumber(), amount, OperationType.WITHDRAW);
            accountService.updateBalanceAccount(accountTo.getAccountNumber(), amount, OperationType.DEPOSIT);

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
            throw new AccountTypesMismatchException("Account types doesn't match. Transfer can't be executed.");
    }

    /**
     * This method returns all transactions corresponding to
     * the token of the notification passed as parameter
     * @param authentication that contains the token
     * @return the list of the transactions that are linked
     * to the token
     */
    public List<Transaction> getTransactions(Authentication authentication){
        List<Account> accountList = accountService.getAccountsByToken(authentication.getToken());
        List<Transaction> transactionList = new ArrayList<>();
        for (Account account : accountList)
            transactionList.addAll(transactionRepository.getTransactionByAccountID(account));
        return transactionList;
    }

    /**
     * This method saves a transaction that has all the
     * details passed as parameters.
     * @param accountFrom the account from which the amount
     *                    is transferred
     * @param amount to be transferred
     * @param accountTo the account to which the amount
     *                  is transferred to
     * @param details of the transaction
     * @param transactionType the type of the transaction:
     *                        incoming or outgoing
     */
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
