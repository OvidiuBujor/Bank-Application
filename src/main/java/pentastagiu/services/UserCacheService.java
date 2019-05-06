package pentastagiu.services;

import pentastagiu.model.Account;
import pentastagiu.model.AccountType;
import pentastagiu.model.User;
import pentastagiu.repository.DatabaseOperations;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains all logic for the current logged in user:
 * load and dispose current user.
 */
public class UserCacheService {

    /**
     * The current user logged in
     */
    private User currentUser;
    private boolean isLogged = false;
    private boolean inAccount = false;
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
        loadAccountsForUser();
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        if (isPosibleTransfer())
            posibleTransfer = true;
    }

    /**
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    public void loadAccountsForUser() {
        currentUser.setAccountsList(DatabaseOperations.readAccountsForUser(currentUser));
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
        return (getTotalNumberOfAccountsForAccType(AccountType.EUR) > 1 &&
                getTotalNumberOfValidTransferAccounts(AccountType.EUR) > 0) ||
                (getTotalNumberOfAccountsForAccType(AccountType.RON) > 1 &&
                        getTotalNumberOfValidTransferAccounts(AccountType.RON) > 0);
    }

    /**
     * This method returns the list of filtered accounts
     * based on the currency of the account received as parameter
     * except this account.
     * @param accountFrom the account that we filter by
     * @return the list of accounts with the same currency excluding it
     */
    public List<Account> getFilteredAccounts(Account accountFrom) {
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(currentUser);
        return allAccounts.stream().filter(account ->
                account.getAccountType() == accountFrom.getAccountType() &&
                        !account.getAccountNumber().equals(accountFrom.getAccountNumber())).collect(Collectors.toList());
    }

    /**
     * @return the list of accounts with balance greater then 0.
     */
    public List<Account> getValidTransferAccounts() {
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(currentUser);
        List<Account> validTransferredAccounts = allAccounts.stream().filter(account ->
                account.getBalance().compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
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
        if(getTotalNumberOfAccountsForAccType(AccountType.EUR) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.EUR));
        if(getTotalNumberOfAccountsForAccType(AccountType.RON) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.RON));
    }

    /**
     * This method returns the number of accounts with the currency
     * specified by the parameter account_type
     * @param account_type specifies the currency that we filter by
     * @return the list of accounts with that currency
     */
    private long getTotalNumberOfAccountsForAccType(AccountType account_type){
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(currentUser);
        return allAccounts.stream().filter(account -> account.getAccountType() == account_type).count();
    }

    /**
     * This method returns the number of accounts that can be used to transfer from
     * with the currency specified by the parameter account_type
     * @param account_type the curency we filter by
     * @return number of accounts that satisfy these filters
     */
    private long getTotalNumberOfValidTransferAccounts(AccountType account_type){
        List<Account> validTransferredAccounts = getValidTransferAccounts();
        return validTransferredAccounts.stream().filter(account ->
                account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
                        account.getAccountType() == account_type).count();
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
