package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pentastagiu.convertor.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This class holds information for a transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * The account number of the account where the amount is transferred
     */
    @Column(name = "account")
    private String account;
    /**
     * The amount transferred
     */
    @Column(name = "amount")
    private BigDecimal amount;
    /**
     * Details of the transaction
     */
    @Column(name = "details")
    private String details;
    /**
     * Time when the transaction was created
     */
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    /**
     * Type of transaction:
     * INCOMING or OUTGOING
     */
    @Column(name = "type")
    private TransactionType type;

    /**
     * Id of the account from where the amount is transferred
     */
    @ManyToOne
    @JoinColumn(name = "accountID")
    @JsonIgnoreProperties("transactionList")
    private Account accountID;

    /**
     * This method adds the time when the transaction was created.
     */
    @PrePersist
    void prePersist(){
        this.createdTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
