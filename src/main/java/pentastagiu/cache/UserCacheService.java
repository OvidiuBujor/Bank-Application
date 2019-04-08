package pentastagiu.cache;

import pentastagiu.files.Database;
import pentastagiu.files.OperationFile;
import pentastagiu.model.ACCOUNT_TYPES;
import pentastagiu.model.Account;
import pentastagiu.model.User;
import pentastagiu.util.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import static pentastagiu.util.AccountService.updateBalanceAccount;
import static pentastagiu.util.Constants.*;

/**
 * This class contains all logic for the current logged in user.
 */
public class UserCacheService {

    private User currentUser;
    /**
     * Stores the state if the {@link #currentUser} is logged in
     */
    private boolean isLogged = false;
    /**
     * Stores the state if the {@link #currentUser}  is in account Menu
     */
    private boolean inAccount = false;

    /**
     * Stores the state if the {@link #currentUser}  can make a deposit
     * - true if we have at least 1 account; false otherwise
     */
    private boolean posibleDeposit = false;

    /**
     * Stores the state if the {@link #currentUser}  can make a transfer
     * between 2 of his accounts
     * - true if we have at least 2 accounts of the same currency
     *  and 1 of them has balance greater then 0; false otherwise
     */
    private boolean posibleTransfer = false;

    /**
     * This method loads the current user into the memory and
     * marks him if he can deposit or if he can transfer between
     * his accounts.
     */
    private void loadUser() {
        setAccountsList();
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        if (isUserAbleToTransfer())
            posibleTransfer = true;
    }

    /**
     * This method removes the current logged in user from memory.
     */
    private void disposeUser() {
        currentUser = null;
        isLogged = false;
        inAccount = false;
        posibleDeposit = false;
        posibleTransfer = false;
    }

    /**
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    private void setAccountsList() {
        currentUser.getAccountsList().addAll(OperationFile.readAccountsFromFileForUser(FILE_ACCOUNTS,currentUser));
    }

    /**
     * This method adds a new created account to the current user's.
     * @param account the new account that is added to the current user
     */
    private void addAccountToUserAccountsList(Account account){
        currentUser.getAccountsList().add(account);
    }

    /**
     * This method returns the list of filtered accounts
     * based on the currency of the account received as parameter
     * except this account.
     * @param accountFrom the account that we filter by
     * @return the list of accounts with the same currency excluding it
     */
    private List<Account> getFilteredAccounts(Account accountFrom) {
        List<Account> filteredAccounts = new ArrayList<>();
        for(Account account: currentUser.getAccountsList()){
            if(account.getAccountType() == accountFrom.getAccountType() &&
                    !account.getAccountNumber().equals(accountFrom.getAccountNumber()))
                filteredAccounts.add(account);
        }
        return filteredAccounts;
    }

    /**
     * @return the list of accounts with balance greater then 0.
     */
    private List<Account> getValidTransferAccounts() {
        List<Account> validTransferedAccounts = new ArrayList<>();
        for(Account account: currentUser.getAccountsList()){
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0)
                validTransferedAccounts.add(account);
        }
        return validTransferedAccounts;
    }

    /**
     * This method returns the number of accounts with the currency
     * specified by the parameter account_type
     * @param account_type specifies the currency that we filter by
     * @return the list of accounts with that currency
     */
    private int getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES account_type){
        int totalNumber = 0;
        for(Account account: currentUser.getAccountsList())
            if(account.getAccountType() == account_type)
                totalNumber++;
        return totalNumber;
    }

    /**
     * This method returns the number of accounts that can be used to transfer from
     * with the currency specified by the parameter account_type
     * @param account_type the curency we filter by
     * @return number of accounts that satisfy these filters
     */
    private int getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES account_type){
        int totalNumber = 0;
        List<Account> validTransferedAccounts = getValidTransferAccounts();
        for(Account account: validTransferedAccounts)
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
                    account.getAccountType() == account_type)
                totalNumber++;
        return totalNumber;
    }

    /**
     * This method checks to see if the user is able to transfer between his accounts.
     * The user needs to have 2 accounts with the same currency and at least 1 of them
     * should have balance greater then 0.
     * @return true if user is able to transfer; false otherwise
     */
    private boolean isUserAbleToTransfer(){
        return (getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.EUR) > 1 &&
                getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES.EUR) > 0) ||
                (getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.RON) > 1 &&
                        getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES.RON) > 0);
    }

    /**
     * This method creates a new account for the current logged in user.
     * Also marks the user as being able to deposit amounts and if he qualifies
     * also marks the user as being able to transfer.
     */
    public void createNewAccount(){
        Account accountCreated = new Account(currentUser);
        Database.addAccountToDatabase(accountCreated);
        addAccountToUserAccountsList(accountCreated);
        posibleDeposit = true;
        if (isUserAbleToTransfer())
            posibleTransfer = true;
    }

    /**
     * This method checks the user for his credentials against the
     * database. If his credentials are ok it loads the user in the memory by
     * invoking {@link #loadUser()} method.
     */
    public void checkUserCredentials(){
        currentUser = new User();
        isLogged = OperationFile.validateUserFromFile(currentUser, USERS_FILE);
        if (isLogged())
            loadUser();
    }

    /**
     * This method displays all the accounts for the current logged in user.
     */
    public void displayAccounts(){
        if (isPosibleDeposit()) {
            System.out.println("\nList of Accounts:");
            Menu.printAccounts(currentUser.getAccountsList());
        }else
            setInAccount(false);
    }

    /**
     * This method removes the current logged in user from memory
     * by invoking {@link #disposeUser()} method.
     */
    public void logoutUser(){
        System.out.println("Successfully logout.");
        disposeUser();
    }

    /**
     * This method handles the deposit to an account logic by invoking the
     * {@link #depositAmount()} method. Also after the deposit it checks
     * if the user is able to transfer between his accounts and marks him.
     */
    public void depositAmountToAcc(){
        if(isPosibleDeposit()) {
            depositAmount();
            if (isUserAbleToTransfer())
                posibleTransfer = true;
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method handles the logic for transferring between 2 accounts by
     * invoking the {@link #transferAmount()} method.
     */
    public void transferAmountBetweenAcc(){
        if (isPosibleTransfer())
            transferAmount();
        else if (isPosibleDeposit())
            setInAccount(false);
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method transfers an amount between 2 accounts owned by the user.
     * The accounts need to be of the same currency type and at least 1 of them
     * needs to have balance greater then 0.
     */
    public void transferAmount() {
        int opt;
        Account accountFrom, accountTo;
        BigDecimal amount;
        List<Account> validTransferAccounts = getValidTransferAccounts();
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

            List<Account> filteredAccounts = getFilteredAccounts(accountFrom);

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
            updateBalanceAccount(amount.negate(),accountFrom);
            updateBalanceAccount(amount,accountTo);
            System.out.println("Transfer to Account: {Account Number: " + accountTo.getAccountNumber() +
                    " New Balance: " + accountTo.getBalance().toString() + " " +
                    accountTo.getAccountType().toString() +"}");
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    /**
     * This method updates the balance of the account with the amount entered
     * from console(it can be also negative for withdraw).
     */
    private void depositAmount(){

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

            updateBalanceAccount(amount,accountToDeposit);
            System.out.println("Updated Account: {Account Number: " + accountToDeposit.getAccountNumber() +
                    " New Balance: " + accountToDeposit.getBalance().toString() + " " +
                    accountToDeposit.getAccountType().toString() +"}");
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    /**
     * This method handles the logic for the menu option "Back to previous menu"
     */
    public void goToPreviousMenu(){
        if(isPosibleDeposit() && isPosibleTransfer())
            setInAccount(false);
        else if(isPosibleDeposit())
            System.out.println("Please enter a valid option(1, 2, 3 or 4).");
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }


    public boolean isLogged() {
        return isLogged;
    }

    public boolean inAccount() {
        return inAccount;
    }

    public void setInAccount(boolean inAccount) {
        this.inAccount = inAccount;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isPosibleTransfer() {
        return posibleTransfer;
    }


    public boolean isPosibleDeposit() {
        return posibleDeposit;
    }
}
