package pentastagiu.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.repository.DatabaseOperations;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static pentastagiu.util.Constants.*;

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
    private int id;

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

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The list of transactions for current account
     */
    @OneToMany(mappedBy = "account",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    private List<Transaction> transactionList = new ArrayList<>();

    /**
     * Empty constructor used for initialization of some accounts
     * that will be generated after some future calculations.
     */
    public Account(){

    }

    /**
     * Constructor that creates an object of type Account with the information below
     * and it's used for validating already created accounts.
     * @param accountNumber of the account created
     * @param balance of the account created
     * @param accountType of the account created
     * @param createdTime the time when it was created
     * @param updatedTime the time of the last update for the account
     * @param currentUser the owner of the account
     */
    public Account(String accountNumber, BigDecimal balance, AccountType accountType, LocalDateTime createdTime, LocalDateTime updatedTime,User currentUser) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.user = currentUser;
    }

    /**
     * Constructor that creates a NEW Account with the details
     * entered from console.
     * @param currentUser owner of the account created
     */
    public Account (User currentUser) {
        String line;
        StringBuilder accountNumber = new StringBuilder();
        String accountType = "";
        BigDecimal balance = BigDecimal.valueOf(0);
        try {
            System.out.println("Please use RON or EUR.");
            do{
                System.out.print("Account type:");
                line = SCANNER.nextLine();
                if (validateAccountType(line))
                    accountType = line;
                else
                    System.out.println("Incorrect account type. Please use: RON or EUR.");
            }while(accountType.isEmpty());

            accountNumber.append("RO09BCYP").append(generateAccountNumber());
            this.accountNumber = accountNumber.toString();
            this.balance = balance;
            this.accountType = AccountType.fromString(accountType.toUpperCase());
            this.createdTime = LocalDateTime.now();
            this.updatedTime = LocalDateTime.now();
            this.user = currentUser;
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    /**
     * This method generates an account number based on the total number of accounts
     * for every new account created.
     * @return the account number created
     */
    private String generateAccountNumber(){
        return String.format("%016d", DatabaseOperations.calculateNrAccFromFile());
    }

    /**
     * This method validates the account type entered from console.
     * Valid types of account are RON or EUR.
     * @param accountType the account type entered from console
     * @return true if account type is correct; false otherwise
     */
    private boolean validateAccountType(String accountType){
        return accountType.toUpperCase().equals("RON") || accountType.toUpperCase().equals("EUR");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
