package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.convertor.AccountType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class stores the information regarding an
 * account.
 */
@Entity
@Table(name = "account")
public class Account {

    @Transient
    private Logger LOGGER = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    /**
     * The account number of the account
     */
    @Column(name = "account_number", unique = true)
    @Size(min = 24, max = 24)
    private String accountNumber;
    /**
     * Current balance of the account
     */
    @Column(name = "balance")
    private BigDecimal balance;
    /**
     * The type of the account. Type can be RON or EUR.
     */
    @Column(name = "account_type")
    private AccountType accountType;
    /**
     * Time when the account was created
     */
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    /**
     * Time when the account was last updated
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    /**
     * The owner of the account. Creates the connection
     * with the User class.
     */
    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonIgnoreProperties("accountList")
    private User user;

    /**
     * The list of transactions for current account.
     * Creates the connection with the Transaction class.
     */
    @OneToMany(mappedBy = "accountID", orphanRemoval = true)
    @JsonIgnoreProperties("accountID")
    private List<Transaction> transactionList = new ArrayList<>();

    /**
     * Empty constructor used for initialization of some accounts
     * that will be generated after some future calculations.
     */
    public Account(){

    }

    public Account(String accountNumber, BigDecimal balance, AccountType accountType){
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    /**
     * This method adds the time when the account was created.
     */
    @PrePersist
    void prePersit(){
        this.createdTime = LocalDateTime.now();
        System.out.println("Account '" + this.getAccountNumber() + "' created.");
        LOGGER.info("Account '" + this.getAccountNumber() + "' created.");
    }

    /**
     * This method adds the time when the account is updated.
     */
    @PreUpdate
    void preUpdate(){
        this.updatedTime = LocalDateTime.now();
        System.out.println("Account '" + this.getAccountNumber() + "' updated.");
        LOGGER.info("Account '" + this.getAccountNumber() + "' updated. New balance is: " + balance + ".");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Override of toString method. Used to add the account to the account database file.
     * @return the string to be added to the database file
     */
    @Override
    public String toString() {
        return  accountNumber.toUpperCase() + " " +  balance + " " + accountType.toString() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber) &&
                accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType);
    }

}
