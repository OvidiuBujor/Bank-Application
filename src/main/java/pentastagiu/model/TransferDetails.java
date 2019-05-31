package pentastagiu.model;

import java.math.BigDecimal;

/**
 * This class contains all the details for
 * a transfer between 2 accounts
 */
public class TransferDetails {
    /**
     * Account number of the account from which the amount is transferred
     */
    private String accountNumberFrom;

    /**
     * Account number of the account where the amount is transferred
     */
    private String accountNumberTo;

    /**
     * The amount to be transferred
     */
    private BigDecimal amount;

    /**
     * The details of the transfer
     */
    private String details;

    public String getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(String accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public String getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(String accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
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
}
