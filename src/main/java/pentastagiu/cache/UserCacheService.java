package pentastagiu.cache;

import pentastagiu.files.OperationFile;
import pentastagiu.model.ACCOUNT_TYPES;
import pentastagiu.model.Account;
import pentastagiu.model.User;
import pentastagiu.util.Display;

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

    /**
     * The current user logged in
     */
    private User currentUser;
    /**
     * Stores the state if the {@link #currentUser} is logged in
     */
    private boolean isLogged = false;
    /**
     * Stores the state if the {@link #currentUser}  is in account Display
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
    public void loadUser() {
        setAccountsList();
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        if (isUserAbleToTransfer())
            posibleTransfer = true;
    }

    /**
     * This method removes the current logged in user from memory.
     */
    public void disposeUser() {
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
        List<Account> validTransferredAccounts = new ArrayList<>();
        for (Account account: currentUser.getAccountsList()){
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0)
                validTransferredAccounts.add(account);
        }
        removeAccountThatDoesNotQualify(validTransferredAccounts);
        return validTransferredAccounts;
    }

    /**
     * This method removes the single valid transfer accounts from the validTransferredAccounts
     * list. This is needed because it is possible to have only 1 valid account to transfer from
     * but no accounts to transfer into for one currency and 2 accounts for the other currency.
     * @param validTransferredAccounts the list with all the valid transfer account
     */
    private void removeAccountThatDoesNotQualify(List<Account> validTransferredAccounts){
        if(getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.EUR) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(ACCOUNT_TYPES.EUR));
        if(getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.RON) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(ACCOUNT_TYPES.RON));
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
        List<Account> validTransferredAccounts = getValidTransferAccounts();
        for(Account account: validTransferredAccounts)
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
                    account.getAccountType() == account_type)
                totalNumber++;
        return totalNumber;
    }

    /**
     * This method checks to see if the user is able to transfer between his accounts.
     * @return true if user is able to transfer; false otherwise
     */
    public boolean isUserAbleToTransfer(){
        return (getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.EUR) > 1 &&
                getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES.EUR) > 0) ||
                (getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES.RON) > 1 &&
                        getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES.RON) > 0);
    }

    /**
     * This method transfers an amount between 2 accounts owned by the user.
     * The accounts need to be of the same currency type and at least 1 of them
     * needs to have balance greater then 0.
     */
    public void transferAmount() {
        Account accountFrom, accountTo;
        BigDecimal amount;

        accountFrom = getAccountFrom();
        amount = getAmountToBeTransferred(accountFrom);
        accountTo = getAccountTo(accountFrom);

        updateBalanceAccount(amount.negate(),accountFrom);
        updateBalanceAccount(amount,accountTo);

        System.out.println("Transfer to Account {Account Number: " + accountTo.getAccountNumber() +
                " New Balance: " + accountTo.getBalance().toString() + " " +
                accountTo.getAccountType().toString() +"}");
    }

    /**
     * This method returns the account selected by the user to transfer FROM.
     * This account will be used by the {@link #transferAmount()} method.
     * @return the account to transfer FROM
     */
    private Account getAccountFrom(){
        List<Account> validTransferAccounts = getValidTransferAccounts();
        Account accountFrom = new Account();
        int opt;
        try {
            if (validTransferAccounts.size() == 1) {
                accountFrom = validTransferAccounts.get(0);
            } else {
                System.out.println("\nList of Accounts to transfer FROM:");
                Display.AccountsList(validTransferAccounts);
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
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        System.out.println("From Account: {Account Number: " + accountFrom.getAccountNumber() +
                " Balance: " + accountFrom.getBalance() + " " +
                accountFrom.getAccountType().toString() + "}");
        return accountFrom;
    }

    /**
     * This method gets the amount entered from console by the user.
     * This amount will be used by the {@link #transferAmount()} method.
     * @param accountFrom the account from which the transfer will be made
     * @return the amount to be transferred
     */
    private BigDecimal getAmountToBeTransferred(Account accountFrom){
        BigDecimal amount = BigDecimal.valueOf(0);
        try{
            while (true) {
                System.out.print("Insert the amount that will transferred between accounts:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    if(amount.compareTo(accountFrom.getBalance()) > 0 || amount.compareTo(new BigDecimal(0)) <= 0)
                        System.out.println("Please enter an amount greater then 0 and " +
                                            "less then or equal with the current balance:" +
                                            accountFrom.getBalance().toString() + " " +
                                            accountFrom.getAccountType().toString());
                    else
                        break;
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return amount;
    }

    /**
     * This method gets the account chosen by the user to transfer into.
     * This account will be used by {@link #transferAmount()} method.
     * @param accountFrom the account from which we will transfer
     * @return the account where the transfer will be made
     */
    private Account getAccountTo(Account accountFrom){
        int opt;
        Account accountTo = new Account();

        try{
            List<Account> filteredAccounts = getFilteredAccounts(accountFrom);

            if (filteredAccounts.size() == 1) {
                accountTo = filteredAccounts.get(0);
            }
            else {
                System.out.println("\nList of accounts to transfer TO:");
                Display.AccountsList(filteredAccounts);
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
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return accountTo;
    }

    /**
     * This method updates the balance of the account with the amount entered
     * from console.
     */
    public void depositAmount(){
        Account accountToDeposit;
        BigDecimal amount;

        accountToDeposit = getAccountToDeposit();

        amount = getAmountToBeDeposited();

        updateBalanceAccount(amount,accountToDeposit);

        System.out.println("Updated Account: {Account Number: " + accountToDeposit.getAccountNumber() +
                " New Balance: " + accountToDeposit.getBalance().toString() + " " +
                accountToDeposit.getAccountType().toString() +"}");
    }

    /**
     * This method gets the account selected by the user to deposit/withdraw from.
     * This account will be used by {@link #depositAmount()} method.
     * @return the account where the amount will be transferred
     */
    private Account getAccountToDeposit(){
        int opt;
        List<Account> allAccounts = currentUser.getAccountsList();
        Account accountToDeposit = new Account();

        try {
            if (allAccounts.size() == 1)
                accountToDeposit =  allAccounts.get(0);
            else {
                System.out.println("\nList of Accounts:");
                Display.AccountsList(allAccounts);
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
                            accountToDeposit =  allAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        System.out.println("\nAccount To Deposit: {Account Number: " + accountToDeposit.getAccountNumber() +
                " Balance: " + accountToDeposit.getBalance() + " " +
                accountToDeposit.getAccountType().toString() + "}");
        return accountToDeposit;
    }

    /**
     * This method gets the amount entered from console by the user.
     * This amount will be used by the {@link #depositAmount()}  method.
     * @return the amount to be deposited
     */
    private BigDecimal getAmountToBeDeposited(){
        BigDecimal amount = BigDecimal.valueOf(0);
        try{
            while (true) {
                System.out.print("Insert the amount that will update the current balance:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    if(amount.compareTo(new BigDecimal(0)) <= 0)
                        System.out.println("Please enter a number greater then 0.");
                    else
                        break;
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return amount;
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

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setPosibleDeposit(boolean posibleDeposit) {
        this.posibleDeposit = posibleDeposit;
    }

    public void setPosibleTransfer(boolean posibleTransfer) {
        this.posibleTransfer = posibleTransfer;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
