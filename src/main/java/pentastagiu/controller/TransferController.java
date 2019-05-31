package pentastagiu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pentastagiu.convertor.NotificationConvertor;
import pentastagiu.convertor.NotificationDTO;
import pentastagiu.convertor.TransactionConvertor;
import pentastagiu.convertor.TransactionDTO;
import pentastagiu.model.*;
import pentastagiu.services.AuthenticationService;
import pentastagiu.services.TransactionService;

import java.util.List;

@RestController
public class TransferController {

    private AuthenticationService authenticationService;

    private TransactionService transactionService;

    private NotificationConvertor notificationConvertor;

    private TransactionConvertor transactionConvertor;

    @Autowired
    public TransferController(AuthenticationService authenticationService,
                              TransactionService transactionService,
                              NotificationConvertor notificationConvertor,
                              TransactionConvertor transactionConvertor){
        this.authenticationService = authenticationService;
        this.transactionService = transactionService;
        this.notificationConvertor = notificationConvertor;
        this.transactionConvertor = transactionConvertor;
    }

    /**
     * This method returns the notification created for
     * the transfer that will be executed using the transfer
     * details passed as parameter.
     * @param token used for validating the user
     * @param transferDetails are the details of the transfer
     * @return the notification created for this transfer
     */
    @PostMapping("/transaction/{token}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NotificationDTO> saveTransfer(@PathVariable(value = "token") String token,
                                                        @RequestBody TransferDetails transferDetails){
        if(authenticationService.existsByToken(token)) {
            Notification notificationToReturn = transactionService.saveTransfer(transferDetails.getAccountNumberFrom(),
                    transferDetails.getAccountNumberTo(),
                    transferDetails.getAmount(),
                    transferDetails.getDetails());
            return  new ResponseEntity<>(notificationConvertor.convertToNotificationDTO(notificationToReturn),
                    HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * This method returns all the transactions for the
     * user with the token provided as parameter.
     * @param token used to validate user
     * @return list of transactions for user with the token
     * provided as parameter
     */
    @GetMapping("/transaction/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable(value = "token") String token){
        if(authenticationService.existsByToken(token)){
            Authentication resultedAuthentication = authenticationService.findByToken(token);
            return new ResponseEntity<>(transactionConvertor.convertToTransactionDTOList(transactionService.getTransactions(resultedAuthentication)),
                    HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
