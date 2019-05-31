package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.convertor.AccountType;
import pentastagiu.convertor.OperationType;
import pentastagiu.exceptions.*;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * This class handles the operations for accounts:
 * deposit and withdraw.
 */

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private AuthenticationService authenticationService;

    @Autowired
    public AccountService(AccountRepository accountRepository, AuthenticationService authenticationService){
        this.accountRepository = accountRepository;
        this.authenticationService = authenticationService;
    }

     public List<Account> getAccountsByToken(String token) {
         if (authenticationService.existsByToken(token)) {
            Authentication authentication = authenticationService.findByToken(token);
            User user = authentication.getUser();
            return accountRepository.getAccountByUser(user);
        }else
            throw new UserNotLoggedInException("User not logged in!");
    }

    public Account saveAccount(AccountType accountType, String token) {
        if(authenticationService.existsByToken(token)) {
            Authentication authentication = authenticationService.findByToken(token);
            Account accountToBeAdded = createAccount(authentication,accountType);
            return accountRepository.save(accountToBeAdded);
        }else
            throw new TokenNotFoundException("Token doesn't exists.");
    }

    private Account createAccount(Authentication authentication, AccountType accountType){
        Account accountToBeAdded = new Account();
        accountToBeAdded.setBalance(BigDecimal.valueOf(0));
        accountToBeAdded.setAccountNumber("RO09BCYP" + generateAccountNumber());
        accountToBeAdded.setAccountType(accountType);
        User owner = authentication.getUser();
        accountToBeAdded.setUser(owner);
        return accountToBeAdded;
    }

    private String generateAccountNumber(){
        return String.format("%016d", accountRepository.count());
    }

    public Account updateBalanceAccount(String accountNumber, BigDecimal amount, OperationType deposit) {
             Account accountToBeUpdated = getAccountByAccountNumber(accountNumber);
             BigDecimal initialBalance = accountToBeUpdated.getBalance();
             if(deposit == OperationType.DEPOSIT){
                 initialBalance = initialBalance.add(amount);
             }else {
                 if(initialBalance.compareTo(amount) >= 0)
                    initialBalance = initialBalance.subtract(amount);
                 else
                    throw new InsufficientFundsException("Insufficient funds.");
             }
             accountToBeUpdated.setBalance(initialBalance);
             return  accountRepository.save(accountToBeUpdated);
    }

    Account getAccountByAccountNumber(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
       if(account.isPresent())
           return account.get();
       throw new AccountNotExistsException("Account does not exists.");
    }

    boolean validateAccountTypes(AccountType accountTypeFrom, AccountType accountTypeTo){
        return accountTypeFrom.equals(accountTypeTo);
    }
}
