package pentastagiu.cache;

import pentastagiu.files.Database;
import pentastagiu.model.Account;
import pentastagiu.model.User;
import pentastagiu.util.Constants;
import pentastagiu.util.Menu;

import static pentastagiu.util.Constants.USERS_LIST;

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
        currentUser.setAccountsList();
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
     * This method checks to see if the user is able to transfer between his accounts.
     * The user needs to have 2 accounts with the same currency and at least 1 of them
     * should have balance greater then 0.
     * @return true if user is able to transfer; false otherwise
     */
    private boolean isUserAbleToTransfer(){
        return (currentUser.getTotalNumberOfAccountsForAccType(Constants.ACCOUNT_TYPES.EUR) > 1 &&
                currentUser.getTotalNumberOfValidTransferAccounts(Constants.ACCOUNT_TYPES.EUR) > 0) ||
                (currentUser.getTotalNumberOfAccountsForAccType(Constants.ACCOUNT_TYPES.RON) > 1 &&
                        currentUser.getTotalNumberOfValidTransferAccounts(Constants.ACCOUNT_TYPES.RON) > 0);
    }

    public void createNewAccount(){
        Account accountCreated = new Account(currentUser);
        Database.addAccountToDatabase(accountCreated);
        currentUser.addAccountToUserAccountsList(accountCreated);
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

    public void depositAmount(){
        if(isPosibleDeposit()) {
            Account.depositAmount(currentUser);
            if (isUserAbleToTransfer())
                posibleTransfer = true;
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    public void transferAmount(){
        if (isPosibleTransfer())
            Account.transferAmount(currentUser);
        else if (isPosibleDeposit())
            setInAccount(false);
        else
            System.out.println("Please enter a valid option(1 or 2).");
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

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setInAccount(boolean inAccount) {
        this.inAccount = inAccount;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isPosibleTransfer() {
        return posibleTransfer;
    }


    public boolean isPosibleDeposit() {
        return posibleDeposit;
    }
}
