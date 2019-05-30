package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.AccountType;
import pentastagiu.convertor.AccountTypeConvertor;
import pentastagiu.model.Account;
import pentastagiu.model.OperationAccountDetails;
import pentastagiu.services.AccountService;
import pentastagiu.services.AuthenticationService;
import pentastagiu.util.CustomException;

import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;

    private AuthenticationService authenticationService;

    @Autowired
    public AccountController(AccountService accountService, AuthenticationService authenticationService){
        this.accountService = accountService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/account/{token}")
    public ResponseEntity<List<Account>> getAccountsByToken(@PathVariable(value = "token") String token)
            throws CustomException {
        return new ResponseEntity<>(accountService.getAccountsByToken(token),HttpStatus.OK);
    }

    @PostMapping("/account/{token}/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> saveAccount(@PathVariable(value = "token") String token,
                                               @PathVariable(value = "accountType") AccountType accountType)
            throws CustomException{
        return new ResponseEntity<>(accountService.saveAccount(accountType,token),HttpStatus.OK);
    }

    @PutMapping("/account/{token}")
    public ResponseEntity<Account> updateBalanceAccount(@PathVariable(value = "token") String token,
                                                        @RequestBody OperationAccountDetails operationAccountDetails){
        if(authenticationService.findByToken(token).isPresent()) {
            Account accountUpdated = accountService.updateBalanceAccount(operationAccountDetails.getId(),
                    operationAccountDetails.getAmount(),
                    operationAccountDetails.getOperation());
            return new ResponseEntity<>(accountUpdated, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @InitBinder
    public void initBinderAccountType(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(AccountType.class, new AccountTypeConvertor());
    }
}
