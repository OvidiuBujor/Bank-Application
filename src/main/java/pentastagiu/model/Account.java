package pentastagiu.model;

import pentastagiu.files.Database;
import pentastagiu.util.Menu;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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
     * stores the total number of accounts and
     * it's loaded when the application starts
     */
    private static long nrOfAccounts;

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
        nrOfAccounts++;
    }

    /**
     * This method generates an account number based on the total number of accounts
     * for every new account created.
     * @return the account number created
     */
    private String generateAccountNumber(){
        return String.format("%016d",nrOfAccounts);
    }

    public static boolean validateAccount(String[] accountDetails){
        return accountDetails.length == 4 &&
                Account.validateAccountNumber(accountDetails[ACCOUNT_NUMBER].toUpperCase()) &&
                Account.validateAccountBalance(accountDetails[BALANCE]) &&
                Account.validateAccountType(accountDetails[ACCOUNT_TYPE].toUpperCase());
    }
    /**
     * This method validates account number entered from console.
     * @param accountNumber the account number to be validated
     * @return true if the account number is valid; false otherwise
     */
    private static boolean validateAccountNumber(String accountNumber){
        return Pattern.matches("RO[0-9]{2}[A-Z]{4}[0-9]{16}", accountNumber.toUpperCase());
    }

    /**
     * This method validates the balance of the acount
     * @param balance the string that will be validated
     * @return true if the string is a BigDecimal; false otherwise
     */
    private static boolean validateAccountBalance(String balance){
        try {
            new BigDecimal(balance);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * This method validates the account type entered from console.
     * Valid types of account are RON or EUR.
     * @param accountType the account type entered from console
     * @return true if account type is correct; false otherwise
     */
    private static boolean validateAccountType(String accountType){
        return accountType.toUpperCase().equals("RON") || accountType.toUpperCase().equals("EUR");
    }

    /**
     * This method updates the balance of the account with the amount entered
     * from console(it can be also negative for withdraw).
     * @param currentUser the owner of the account
     */
    public static void depositAmount(User currentUser){

        int opt;
        Account accountToDeposit;
        BigDecimal amount;

        List<Account> allAccounts = currentUser.getAccountsList();

        try {
            if (allAccounts.size() == 1) {
                accountToDeposit = allAccounts.get(0);
            }
            else {
                System.out.println("\nList of Accounts:");
                Menu.printAccounts(allAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to deposit in:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if (opt < 1 || opt > allAccounts.size())
                            System.out.println("Please enter a number between 1 and " + allAccounts.size());
                        else {
                            accountToDeposit = allAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
            System.out.println("\nAccount To Deposit: {Account Number: " + accountToDeposit.getAccountNumber() +
                    " Balance: " + accountToDeposit.getBalance() + " " +
                    accountToDeposit.getAccountType().toString() + "}");

            while (true) {
                System.out.print("Insert the amount that will update the current balance:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    break;
                }
            }

            accountToDeposit.updateBalanceAccount(amount);
            System.out.println("Updated Account: {Account Number: " + accountToDeposit.getAccountNumber() +
                    " New Balance: " + accountToDeposit.getBalance().toString() + " " +
                    accountToDeposit.getAccountType().toString() +"}");
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    /**
     * This method transfers an amount between 2 accounts owned by the user.
     * The accounts need to be of the same currency type and at least 1 of them
     * needs to have balance greater then 0.
     * @param currentUser the current logged in user
     */
    public static void transferAmount(User currentUser) {
        int opt;
        Account accountFrom, accountTo;
        BigDecimal amount;
        List<Account> validTransferAccounts = currentUser.getValidTransferAccounts();
        try {
            if (validTransferAccounts.size() == 1) {
                accountFrom = validTransferAccounts.get(0);
            }
            else {
                System.out.println("\nList of Accounts to transfer FROM:");
                Menu.printAccounts(validTransferAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to transfer from:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if (opt < 1 || opt > validTransferAccounts.size())
                            System.out.println("Please enter a number between 1 and " + validTransferAccounts.size());
                        else {
                            accountFrom = validTransferAccounts.get(opt - 1);

                            break;
                        }
                    }
                }
            }

            System.out.println("From Account: {Account Number: " + accountFrom.getAccountNumber() +
                    " Balance: " + accountFrom.getBalance() + " " +
                    accountFrom.getAccountType().toString() + "}");

            while (true) {
                System.out.print("Insert the amount that will transferred between accounts:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    if(amount.compareTo(accountFrom.getBalance()) > 0)
                        System.out.println("Please enter an amount less then or equal with the current balance:" +
                                accountFrom.getBalance().toString() + " " +
                                accountFrom.getAccountType().toString());
                    else
                        break;
                }
            }

            List<Account> filteredAccounts = currentUser.getFilteredAccounts(accountFrom);

            if (filteredAccounts.size() == 1) {
                accountTo = filteredAccounts.get(0);
            }
            else {
                System.out.println("\nList of accounts to transfer TO:");
                Menu.printAccounts(filteredAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to transfer to:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if(opt < 1 || opt > filteredAccounts.size())
                            System.out.println("Please enter a number between 1 and " + filteredAccounts.size());
                        else {
                            accountTo = filteredAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
            accountFrom.updateBalanceAccount(amount.negate());
            accountTo.updateBalanceAccount(amount);
            System.out.println("Transfer to Account: {Account Number: " + accountTo.getAccountNumber() +
                    " New Balance: " + accountTo.getBalance().toString() + " " +
                    accountTo.getAccountType().toString() +"}");
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    /**
     * This method updates the balance  of the account.
     * Updates the database of accounts by invoking
     * {@link Database#updateBalanceAccountInDatabase(BigDecimal,Account) updateBalanceAccountInDatabase} static method.
     * @param amount the amount entered from console
     */
    private void updateBalanceAccount(BigDecimal amount){
        balance = balance.add(amount);
        Database.updateBalanceAccountInDatabase(balance,this);
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

    public static long getNrOfAccounts() {
        return nrOfAccounts;
    }

    public static void setNrOfAccounts(long nrOfAccounts) {
        Account.nrOfAccounts = nrOfAccounts;
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
