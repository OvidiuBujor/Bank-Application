package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Authentication;
import pentastagiu.model.Transaction;
import pentastagiu.services.AutheticationService;
import pentastagiu.services.TransactionService;
import pentastagiu.util.CustomException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class TransferController {

    @Autowired
    AutheticationService autheticationService;

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction/{token}/{accountFromId}/{accountToId}/{amount}/{details}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransfer(@PathVariable(value = "token") String token,
                             @PathVariable(value = "accountFromId") Long accountFromId,
                             @PathVariable(value = "accountToId") Long accountToId,
                             @PathVariable(value = "amount")BigDecimal amount,
                             @PathVariable(value = "details") String details) throws CustomException{
        if(autheticationService.findByToken(token).isPresent())
            transactionService.saveTransfer(accountFromId,accountToId,amount,details);
        else throw new CustomException("Token for transfer doesn't exists.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/transaction/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable(value = "token") String token){
        Optional<Authentication> resultedAuthentication = autheticationService.findByToken(token);
        if(resultedAuthentication.isPresent()){
            Authentication authentication = resultedAuthentication.get();
            return new ResponseEntity<>(transactionService.getTransactions(authentication),HttpStatus.OK);
        }else throw new CustomException("Token to get transactions not found.", HttpStatus.NOT_FOUND);
    }
}
