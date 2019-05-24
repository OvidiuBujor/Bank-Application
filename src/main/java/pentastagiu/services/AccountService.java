package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Account;
import pentastagiu.repository.AccountRepository;

import java.util.List;

/**
 * This class handles the operations for accounts:
 * deposit and transfer between 2 valid accounts.
 */
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAccounts(Integer userID){
            return accountRepository.getAccountByuserID(userID);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }
}
