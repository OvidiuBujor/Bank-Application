package pentastagiu.services;

import pentastagiu.model.ACCOUNT_TYPES;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static pentastagiu.util.Constants.*;

/**
 * This class contains all logic for the current logged in user:
 * load and dispose current user,
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
     * Stores the state if the {@link #currentUser}  is in account
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
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    private void setAccountsList() {
        currentUser.getAccountsList().addAll(FileService.readAccountsFromFileForUser(FILE_ACCOUNTS,currentUser));
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
