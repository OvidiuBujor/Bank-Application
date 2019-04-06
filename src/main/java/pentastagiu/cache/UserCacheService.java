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

import static pentastagiu.files.Database.USERS_LIST;
import static pentastagiu.util.AccountService.updateBalanceAccount;
import static pentastagiu.util.Constants.*;

public class UserCacheService {

    private User currentUser;
    /**
     * Stores the state if the #currentUser is logged in
     */
    private boolean isLogged = false;
    /**
     * Stores the state if the #currentUser is in account Menu
     */
    private boolean inAccount = false;

    /**
     * Stores the state if the #currentUser can make a deposit
     * - true if we have at least 1 account; false otherwise
     */
    private boolean posibleDeposit = false;

    /**
     * Stores the state if the #currentUser can make a transfer
     * between 2 of his accounts
     * - true if we have at least 2 accounts of the same currency
     *  and 1 of them has balance greater then 0; false otherwise
     */
    private boolean posibleTransfer = false;

    private void loadUser() {
        setAccountsList();
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        if (isUserAbleToTransfer())
            posibleTransfer = true;
    }

    private void disposeUser() {
        currentUser = null;
        isLogged = false;
        inAccount = false;
        posibleDeposit = false;
        posibleTransfer = false;
    }

    /**
     * This method checks current user against the users list and
     * displays the result to the console.
     * @return true if user exists in database; false otherwise
     */
    private boolean validateUser() {

        if (USERS_LIST.contains(currentUser)) {
            System.out.println("Welcome " + currentUser.getUsername() + "!");
            return true;
        } else
            System.out.println("Wrong username/password.");
        return false;
    }

    /**
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    public void setAccountsList() {
        currentUser.getAccountsList().addAll(OperationFile.readAccountsFromFileForUser(FILE_ACCOUNTS,currentUser));
    }

    /**
     * This method adds a new created account to the current user's.
     * @param account the new account that is added to the current user
     */
    public void addAccountToUserAccountsList(Account account){
        currentUser.getAccountsList().add(account);
    }

    /**
     * This method returns the list of filtered accounts
     * based on the currency of the account received as parameter
     * except this account.
     * @param accountFrom the account that we filter by
     * @return the list of accounts with the same currency excluding it
     */
    public List<Account> getFilteredAccounts(Account accountFrom) {
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
    public List<Account> getValidTransferAccounts() {
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
    public int getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES account_type){
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
    public int getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES account_type){
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

    public void createNewAccount(){
        Account accountCreated = new Account(currentUser);
        Database.addAccountToDatabase(accountCreated);
        addAccountToUserAccountsList(accountCreated);
        posibleDeposit = true;
        if (isUserAbleToTransfer())
            posibleTransfer = true;
    }

    public void checkUserCredentials(){
        currentUser = new User();
        isLogged = validateUser();
        if (isLogged())
            loadUser();
    }

    public void displayAccounts(){
        if (isPosibleDeposit()) {
            System.out.println("\nList of Accounts:");
            Menu.printAccounts(currentUser.getAccountsList());
        }else
            setInAccount(false);
    }

    public void logoutUser(){
        System.out.println("Successfully logout.");
        disposeUser();
    }

    public void depositAmountToAcc(){
        if(isPosibleDeposit()) {
            depositAmount();
            if (isUserAbleToTransfer())
                posibleTransfer = true;
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }

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
    public void depositAmount(){

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
