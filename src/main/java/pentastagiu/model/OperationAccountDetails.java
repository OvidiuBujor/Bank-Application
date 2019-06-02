package pentastagiu.model;

import pentastagiu.util.OperationType;

import java.math.BigDecimal;

/**
 * This class holds all the information
 * that is needed to make a deposit or
 * a withdraw.
 */
public class OperationAccountDetails {

    private String accountNumber;

    private BigDecimal amount;

    /**
     * Type of operation can be:
     * DEPOSIT or WITHDRAW
     */
    private OperationType operation;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }
}
