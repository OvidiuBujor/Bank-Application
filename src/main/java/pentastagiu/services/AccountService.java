package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pentastagiu.model.Account;
import pentastagiu.model.Authentication;
import pentastagiu.model.User;
import pentastagiu.repository.AccountRepository;
import pentastagiu.util.AccountType;
import pentastagiu.util.CustomException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

     public List<Account> getAccountsByToken(String token) throws CustomException {
        Optional<Authentication> authentication = autheticationService.findByToken(token);
        if (authentication.isPresent()) {
            User user = authentication.get().getUser();
            return accountRepository.getAccountByUser(user);
        }else
            throw new CustomException("User not logged in!", HttpStatus.NOT_FOUND);
    }

    public Account createAccount(AccountType accountType, String token) throws CustomException {
        Optional<Authentication> authentication = autheticationService.findByToken(token);
        if(authentication.isPresent()) {
            Account accountToBeAdded = new Account();
            accountToBeAdded.setBalance(BigDecimal.valueOf(0));
            accountToBeAdded.setAccountNumber(generateAccountNumber());
            accountToBeAdded.setAccountType(accountType);
            User owner = authentication.get().getUser();
            accountToBeAdded.setUser(owner);
            return accountRepository.save(accountToBeAdded);
        }else
            throw new CustomException("Token doesn't exists.", HttpStatus.NOT_FOUND);
    }

    private String generateAccountNumber(){
        return String.format("%016d", accountRepository.count());
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }
}
