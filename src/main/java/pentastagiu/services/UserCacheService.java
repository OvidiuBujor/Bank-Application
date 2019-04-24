package pentastagiu.services;

import pentastagiu.model.User;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.repository.UserOperations;

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
        setAccountsList();
        if(currentUser.getAccountsList().size() > 0)
            posibleDeposit = true;
        UserOperations userOperations = new UserOperations(this);
        if (userOperations.isUserAbleToTransfer())
            posibleTransfer = true;
    }

    /**
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    private void setAccountsList() {
        currentUser.getAccountsList().addAll(DatabaseOperations.readAccountsForUser(currentUser));
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
