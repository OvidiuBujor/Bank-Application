package pentastagiu.model;

import pentastagiu.operations.DatabaseOperations;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Objects;

import static pentastagiu.validators.AccountValidations.*;
import static pentastagiu.util.Constants.*;

/**
 * This class stores the information regarding an
 * account.
 */
public class Account {

    private String accountNumber;
    /**
     * The username of the owner
     */
    private String username;
    private BigDecimal balance;
    /**
     * The type of the account. Type can be RON or EUR.
     */
    private AccountType accountType;

    /**
     * Empty constructor used for initialization of some accounts
     * that will be generated after some future calculations.
     */
    public Account(){

    }

    /**
     * Constructor that creates an object of type Account with the information below
     * and it's used for validating already created accounts.
     * @param accountNumber the number of the account
     * @param username the username of the owner
     * @param balance the balance of the account
     * @param accountType the account type
     */
    public Account(String accountNumber, String username, BigDecimal balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
        this.accountType = accountType;
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
            this.username = currentUser.getUsername();
            this.balance = balance;
            this.accountType = AccountType.fromString(accountType.toUpperCase());
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        DatabaseOperations.increaseTotalNrOfAccounts();
    }



    public String getAccountNumber() {
        return accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return  accountNumber.toUpperCase() + " " +  username + " " + balance + " " + accountType.toString() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber) &&
                username.equals(account.username) &&
                accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, username, accountType);
    }
}
