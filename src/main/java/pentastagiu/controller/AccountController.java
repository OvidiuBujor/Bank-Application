package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Account;
import pentastagiu.services.AccountService;
import pentastagiu.util.AccountType;
import pentastagiu.util.CustomException;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{token}")
    public List<Account> getAccounts(@PathVariable(value = "token") String token) throws CustomException {
        return accountService.getAccountsByToken(token);
    }

    @PostMapping("/account/{token}/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> createAccount(@PathVariable(value = "token") String token,
                                 @PathVariable(value = "type") AccountType accountType) throws CustomException{
        return new ResponseEntity<>(accountService.createAccount(accountType,token),HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {

        Account updatedAccount = accountService.updateAccount(account);

        if (updatedAccount != null) return new ResponseEntity<>(accountService.updateAccount(account), HttpStatus.OK);

        return ResponseEntity.badRequest().build();
    }

}
