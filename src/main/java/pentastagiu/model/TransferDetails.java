package pentastagiu.model;

import java.math.BigDecimal;

/**
 * This class contains all the details for
 * a transfer
 */
public class TransferDetails {
    /**
     * Id of the account from which the amount is transferred
     */
    private Long accountFromId;

    /**
     * Id of the account where the amount is transferred
     */
    private Long accountToId;

    /**
     * The amount to be transfered
     */
    private BigDecimal amount;

    /**
     * The details of the transfer
     */
    private String details;

    public Long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Long accountToId) {
        this.accountToId = accountToId;
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
