package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.DTOs.TransactionDTO;
import pentastagiu.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class converts a Transaction to
 * a TransactionDTO that is used for responses of
 * the server
 */
@Component
public class TransactionConvertor {
    private TransactionDTO convertToTransactionDTO(Transaction transaction){
        return new TransactionDTO(transaction.getAccountID().getAccountNumber(),
                transaction.getAccount(),
                transaction.getAmount(),
                transaction.getDetails(),
                transaction.getType());
    }

    public List<TransactionDTO> convertToTransactionDTOList(List<Transaction> transactionList){
        return  transactionList.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());
    }
}
