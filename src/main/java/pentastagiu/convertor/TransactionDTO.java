package pentastagiu.convertor;

import java.math.BigDecimal;

public class TransactionDTO {
    private String accountNumberFrom;
    private String getAccountNumberTo;
    private BigDecimal amount;
    private String details;
    private TransactionType transactionType;

    public TransactionDTO(String accountNumberFrom, String getAccountNumberTo, BigDecimal amount, String details, TransactionType transactionType) {
        this.accountNumberFrom = accountNumberFrom;
        this.getAccountNumberTo = getAccountNumberTo;
        this.amount = amount;
        this.details = details;
        this.transactionType = transactionType;
    }

    public String getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(String accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public String getGetAccountNumberTo() {
        return getAccountNumberTo;
    }

    public void setGetAccountNumberTo(String getAccountNumberTo) {
        this.getAccountNumberTo = getAccountNumberTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
