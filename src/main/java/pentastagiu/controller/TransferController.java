package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.model.Authentication;
import pentastagiu.model.Notification;
import pentastagiu.model.Transaction;
import pentastagiu.model.TransferDetails;
import pentastagiu.services.AuthenticationService;
import pentastagiu.services.TransactionService;

import java.util.List;
import java.util.Optional;

@RestController
public class TransferController {

    private AuthenticationService authenticationService;

    private TransactionService transactionService;

    @Autowired
    public TransferController(AuthenticationService authenticationService, TransactionService transactionService){
        this.authenticationService = authenticationService;
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/{token}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Notification> saveTransfer(@PathVariable(value = "token") String token,
                                                     @RequestBody TransferDetails transferDetails){
        if(authenticationService.findByToken(token).isPresent()) {
            Notification notificationToReturn = transactionService.saveTransfer(transferDetails.getAccountFromId(),
                    transferDetails.getAccountToId(),
                    transferDetails.getAmount(),
                    transferDetails.getDetails());
            return  new ResponseEntity<>(notificationToReturn,HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/transaction/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable(value = "token") String token){
        Optional<Authentication> resultedAuthentication = authenticationService.findByToken(token);
        if(resultedAuthentication.isPresent()){
            Authentication authentication = resultedAuthentication.get();
            return new ResponseEntity<>(transactionService.getTransactions(authentication),HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
