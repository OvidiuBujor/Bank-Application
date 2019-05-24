package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Account;
import pentastagiu.services.AccountService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{id}/{token}")
    public List<Account> getAccounts(@PathParam(value = "id") Long id, @PathParam(value = "token") String token) {
        return accountService.getAccounts(id);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/account")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {

        Account updatedAccount = accountService.updateAccount(account);

        if (updatedAccount != null) return new ResponseEntity<>(accountService.updateAccount(account), HttpStatus.OK);

        return ResponseEntity.badRequest().build();
    }

}
