package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.AccountType;
import pentastagiu.convertor.AccountTypeConvertor;
import pentastagiu.model.Account;
import pentastagiu.services.AccountService;
import pentastagiu.util.CustomException;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/account/{token}")
    public ResponseEntity<List<Account>> getAccountsByToken(@PathVariable(value = "token") String token) throws CustomException {
        return new ResponseEntity<>(accountService.getAccountsByToken(token),HttpStatus.OK);
    }

    @PostMapping("/account/{token}/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> saveAccount(@PathVariable(value = "token") String token,
                                               @PathVariable(value = "accountType") AccountType accountType) throws CustomException{
        return new ResponseEntity<>(accountService.saveAccount(accountType,token),HttpStatus.OK);
    }

    @PutMapping("/account/{id}/{amount}/{details}/{deposit}")
    public ResponseEntity<Account> updateBalanceAccount(@PathVariable(value = "id") Long id,
                                                        @PathVariable(value = "amount") BigDecimal amount,
                                                        @PathVariable(value = "deposit") Boolean deposit) throws CustomException{
        return new ResponseEntity<>(accountService.updateBalanceAccount(id, amount, deposit),HttpStatus.OK);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(AccountType.class, new AccountTypeConvertor());
    }

}
