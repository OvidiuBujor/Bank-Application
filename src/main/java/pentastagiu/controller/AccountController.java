package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.AccountConvertor;
import pentastagiu.convertor.AccountDTO;
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

    private AccountConvertor accountConvertor;

    @Autowired
    public AccountController(AccountService accountService,
                             AuthenticationService authenticationService,
                             AccountConvertor accountConvertor){
        this.accountService = accountService;
        this.authenticationService = authenticationService;
        this.accountConvertor =  accountConvertor;
    }

    /**
     * This method returns all accounts based on the token
     * provided as parameter.
     * @param token for which we return the accounts
     * @return List of accounts for user with the token
     * @throws CustomException In case token doesn't exists
     * this exception is thrown
     */
    @GetMapping("/account/{token}")
    public ResponseEntity<List<AccountDTO>> getAccountsByToken(@PathVariable(value = "token") String token)
            throws CustomException {
        return new ResponseEntity<>(accountConvertor.convertToAccountDTOList(accountService.getAccountsByToken(token)),
                HttpStatus.OK);
    }

    /**
     * This method creates a new account for the user which
     * token is provided as parameter.
     * @param token of the owner of the account
     * @param accountType of the created account can be ron or eur
     * @return the new created account
     * @throws CustomException in case token doesn't exists
     */
    @PostMapping("/account/{token}/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDTO> saveAccount(@PathVariable(value = "token") String token,
                                                  @PathVariable(value = "accountType") AccountType accountType)
            throws CustomException{
        return new ResponseEntity<>(accountConvertor.convertToAccountDTO(accountService.saveAccount(accountType,token)),
                HttpStatus.OK);
    }

    /**
     * This method handles the operations for an account:
     * deposit or withdraw.
     * @param token of the owner of the account
     * @param operationAccountDetails contains all the details
     *                                of the operation
     * @return the updated account
     */
    @PutMapping("/account/{token}")
    public ResponseEntity<AccountDTO> updateBalanceAccount(@PathVariable(value = "token") String token,
                                                        @RequestBody OperationAccountDetails operationAccountDetails) {
        if(authenticationService.existsByToken(token)) {
            Account accountUpdated = accountService.updateBalanceAccount(operationAccountDetails.getAccountNumber(),
                    operationAccountDetails.getAmount(),
                    operationAccountDetails.getOperation());
            return new ResponseEntity<>(accountConvertor.convertToAccountDTO(accountUpdated), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @InitBinder
    public void initBinderAccountType(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(AccountType.class, new AccountTypeConvertor());
    }
}
