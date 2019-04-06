package pentastagiu.model;

import pentastagiu.files.Database;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Objects;

import static pentastagiu.util.AccountService.*;
import static pentastagiu.util.Constants.*;

/**
 * This class stores the information regarding an
 * account.
 */
public class Account {
    /**
     * The account name
     */
    private String accountNumber;
    /**
     * The username of the owner
     */
    private String username;
    /**
     * The balance of the account
     */
    private BigDecimal balance;
    /**
     * The type of the account. Type can be RON or EUR.
     */
    private ACCOUNT_TYPES accountType;

    /**
     * Constructor that creates an object of type Account with the information below
     * and it's used for validating already created accounts.
     * @param accountNumber the number of the account
     * @param username the username of the owner
     * @param balance the balance of the account
     * @param accountType the account type
     */
    public Account(String accountNumber, String username, BigDecimal balance, ACCOUNT_TYPES accountType) {
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
        String accountType;
        BigDecimal balance = BigDecimal.valueOf(0);
        try {
            while (true) {
                System.out.println("Please use RON or EUR.");
                System.out.print("Account type:");
                line = SCANNER.nextLine();
                if (validateAccountType(line)) {
                    accountType = line;
                    break;
                } else
                    System.out.println("Incorrect account type. Please use: RON or EUR.");
            }

            accountNumber.append("RO09BCYP").append(generateAccountNumber());
            this.accountNumber = accountNumber.toString();
            this.username = currentUser.getUsername();
            this.balance = balance;
            this.accountType = ACCOUNT_TYPES.fromString(accountType.toUpperCase());
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        Database.nrOfAccounts++;
    }

    /**
     * This method generates an account number based on the total number of accounts
     * for every new account created.
     * @return the account number created
     */
    private String generateAccountNumber(){
        return String.format("%016d", Database.nrOfAccounts);
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

    public ACCOUNT_TYPES getAccountType() {
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
