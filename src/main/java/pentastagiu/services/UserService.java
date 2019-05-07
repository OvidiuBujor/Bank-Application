package pentastagiu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.AccountType;
import pentastagiu.model.User;

import java.util.InputMismatchException;

import static pentastagiu.util.Constants.*;
import static pentastagiu.util.Constants.SCANNER;

/**
 * This class contains all logic for the current logged in user:
 * load and dispose current user.
 */
public class UserService {

    private Logger LOGGER = LogManager.getLogger();
    /**
     * The current user logged in
     */
    private User currentUser;
    private AccountService accountService;
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

    public UserService(){
        this.accountService = new AccountService();
    }

    /**
     * This method loads the current user into the memory and
     * marks him if he can deposit or if he can transfer between
     * his accounts.
     */
    public void loadUser() {
        accountService.loadAccountsForUser(currentUser);
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        if (isPosibleTransfer())
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
     * This method gets the user credentials from the console
     * @return the credentials
     */
    public String[] getUserCredentials(){
        String[] userCredentials = new String[2];
        try {
            System.out.print("Username:");
            userCredentials[USERNAME] = SCANNER.nextLine();
            System.out.print("Password:");
            userCredentials[PASSWORD] = SCANNER.nextLine();
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return userCredentials;
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
        return (accountService.getTotalNumberOfAccountsForAccType(currentUser, AccountType.EUR) > 1 &&
                accountService.getTotalNumberOfValidTransferAccounts(currentUser, AccountType.EUR) > 0) ||
                (accountService.getTotalNumberOfAccountsForAccType(currentUser, AccountType.RON) > 1 &&
                        accountService.getTotalNumberOfValidTransferAccounts(currentUser, AccountType.RON) > 0);
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

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
