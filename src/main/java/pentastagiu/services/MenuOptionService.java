package pentastagiu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.Account;
import pentastagiu.repository.AccountOperations;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.util.InvalidUserException;

import java.util.InputMismatchException;

import static pentastagiu.util.Constants.*;

/**
 * Helper class that implements logic for all Menu Options:
 * create new account, login/logout user, display accounts,
 * deposit and transfer between accounts, go to previous menu.
 */
public class MenuOptionService {

    private Logger LOGGER = LogManager.getLogger();
    private UserCacheService cachedUser;
    private AccountOperations accountOperations;

    public MenuOptionService(UserCacheService cachedUser){
        this.cachedUser = cachedUser;
        this.accountOperations = new AccountOperations(cachedUser);
    }

    /**
     * This method creates a new account for the current logged in user.
     * Also marks the user as being able to deposit amounts and if he qualifies
     * also marks the user as being able to transfer.
     */
    public void createNewAccount(){
        Account accountCreated = new Account(cachedUser.getCurrentUser());
        DatabaseOperations.addAccountToDatabase(accountCreated);

        cachedUser.setPosibleDeposit(true);
        if (cachedUser.isPosibleTransfer())
            cachedUser.setPosibleTransfer(true);
    }

    /**
     * This method checks the user for his credentials against the
     * database. If his credentials are ok it loads the user in the memory by
     * invoking {@link UserCacheService#loadUser()} method.
     */
    public void checkUserCredentials(){
        boolean result = false;
        try {
            cachedUser.setCurrentUser(DatabaseOperations.validateUser(getUserCredentials()));
            result = true;
        } catch (InvalidUserException e) {
            System.out.println("Wrong username/password.");
        }
        cachedUser.setLogged(result);
        if (cachedUser.isLogged())
            cachedUser.loadUser();
    }

    /**
     * This method gets the user credentials from the console
     * @return the credentials
     */
    private String[] getUserCredentials(){
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

    /**
     * This method displays all the accounts for the current logged in user.
     */
    public void displayAccounts(){
        if (cachedUser.isPosibleDeposit()) {
            System.out.println("\nList of Accounts:");
            cachedUser.loadAccountsForUser();
            DisplayService.AccountsList(cachedUser.getCurrentUser().getAccountsList());
        }else
            cachedUser.setInAccount(false);
    }

    /**
     * This method removes the current logged in user from memory
     * by invoking {@link UserCacheService#disposeUser()} method.
     */
    public void logoutUser(){
        System.out.println("Successfully logout.");
        cachedUser.disposeUser();
    }

    /**
     * This method handles the deposit to an account logic by invoking the
     * {@link AccountOperations#depositAmount()} method. Also after the deposit it checks
     * if the user is able to transfer between his accounts and marks him.
     */
    public void depositAmountToAcc(){
        if(cachedUser.isPosibleDeposit()) {
            accountOperations.depositAmount();
            if (cachedUser.isPosibleTransfer())
                cachedUser.setPosibleTransfer(true);
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }


    /**
     * This method handles the logic for transferring between 2 accounts by
     * invoking the {@link AccountOperations#transferAmount()} method.
     */
    public void transferAmountBetweenAcc(){
        if (cachedUser.isPosibleTransfer())
            accountOperations.transferAmount();
        else if (cachedUser.isPosibleDeposit())
            cachedUser.setInAccount(false);
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method handles the logic for the menu option "Back to previous menu"
     */
    public void goToPreviousMenu(){
        if(cachedUser.isPosibleDeposit() && cachedUser.isPosibleTransfer())
            cachedUser.setInAccount(false);
        else if(cachedUser.isPosibleDeposit())
            System.out.println("Please enter a valid option(1, 2, 3 or 4).");
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method displays to the user the valid options he can choose on any menu.
     */
    public void displayValidInputOptions(){
        if (cachedUser.inAccount())
            if(cachedUser.isPosibleDeposit() && cachedUser.isPosibleTransfer())
                System.out.println("Please enter a valid option(1, 2, 3, 4 or 5).");
            else if(cachedUser.isPosibleDeposit())
                System.out.println("Please enter a valid option(1, 2, 3 or 4).");
            else
                System.out.println("Please enter a valid option(1 or 2).");
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method displays the Menu after the current selected operation was executed.
     * @param opt the option the user entered on the console
     */
    public void displayTheMenu(String opt){
        if(!opt.equals("")) { //added for scanner.nextBigDecimal() that reads an empty string after the BigDecimal
            if (cachedUser.inAccount())
                DisplayService.AccountMenu(cachedUser);
            else if (cachedUser.isLogged())
                DisplayService.LoggedInMenu(cachedUser);
            else
                DisplayService.InitialMenu();
        }
    }
}
