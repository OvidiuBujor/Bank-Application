package pentastagiu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import pentastagiu.convertor.AccountType;

import javax.persistence.*;
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
    private long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private BigDecimal balance;
    /**
     * The type of the account. Type can be RON or EUR.
     */
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonIgnoreProperties("accountList")
    private User user;

    /**
     * The list of transactions for current account
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

    @PrePersist
    void prePersit(){
        this.createdTime = LocalDateTime.now();
        System.out.println("Account " + this.getAccountNumber() + " created.");
    }

    @PreUpdate
    void preUpdate(){
        this.updatedTime = LocalDateTime.now();
        System.out.println("Account " + this.getAccountNumber() + " updated.");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
