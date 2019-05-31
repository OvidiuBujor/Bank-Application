package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.convertor.AccountType;
import pentastagiu.convertor.OperationType;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AccountRepository;
import pentastagiu.util.CustomException;

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

     public List<Account> getAccountsByToken(String token) throws CustomException {
         if (authenticationService.existsByToken(token)) {
            Authentication authentication = authenticationService.findByToken(token);
            User user = authentication.getUser();
            return accountRepository.getAccountByUser(user);
        }else
            throw new CustomException("User not logged in!", HttpStatus.NOT_FOUND);
    }

    public Account saveAccount(AccountType accountType, String token) throws CustomException {
        if(authenticationService.existsByToken(token)) {
            Authentication authentication = authenticationService.findByToken(token);
            Account accountToBeAdded = createAccount(authentication,accountType);
            return accountRepository.save(accountToBeAdded);
        }else
            throw new CustomException("Token doesn't exists.", HttpStatus.NOT_FOUND);
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

    public Account updateBalanceAccount(String accountNumber, BigDecimal amount, OperationType deposit) throws CustomException{
             Account accountToBeUpdated = getAccountByAccountNumber(accountNumber);
             BigDecimal initialBalance = accountToBeUpdated.getBalance();
             if(deposit == OperationType.DEPOSIT){
                 initialBalance = initialBalance.add(amount);
             }else {
                 if(initialBalance.compareTo(amount) >= 0)
                    initialBalance = initialBalance.subtract(amount);
                 else
                    throw new CustomException("Insufficient funds.",HttpStatus.BAD_REQUEST);
             }
             accountToBeUpdated.setBalance(initialBalance);
             return  accountRepository.save(accountToBeUpdated);
    }

    Account getAccountByAccountNumber(String accountNumber) throws CustomException{
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
       if(account.isPresent())
           return account.get();
       throw new CustomException("Account doesn't exists.", HttpStatus.NOT_FOUND);
    }

    boolean validateAccountTypes(AccountType accountTypeFrom, AccountType accountTypeTo){
        return accountTypeFrom.equals(accountTypeTo);
    }
}
