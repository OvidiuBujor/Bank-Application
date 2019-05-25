package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AccountRepository;
import pentastagiu.util.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class handles the operations for accounts:
 * deposit and transfer between 2 valid accounts.
 */
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AutheticationService autheticationService;

     public List<Account> getAccountsByToken(String token){
        Authentication authentication = autheticationService.findByToken(token);
        User user = authentication.getUser();
        return accountRepository.getAccountByUser(user);
    }

    public Account createAccount(AccountType accountType, String token) {
         Account accountToBeAdded = new Account();
         accountToBeAdded.setBalance(BigDecimal.valueOf(0));
         accountToBeAdded.setAccountNumber(generateAccountNumber());
         accountToBeAdded.setAccountType(accountType);
         accountToBeAdded.setUser(autheticationService.findByToken(token).getUser());
         accountToBeAdded.setCreatedTime(LocalDateTime.now());
         accountToBeAdded.setUpdatedTime(LocalDateTime.now());
         return accountRepository.save(accountToBeAdded);
    }

    private String generateAccountNumber(){
        return String.format("%016d", accountRepository.count());
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }
}
