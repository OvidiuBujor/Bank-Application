package pentastagiu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.Account;
import pentastagiu.model.User;
import pentastagiu.repository.AccountOperations;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.repository.UserOperations;
import pentastagiu.util.InvalidUserException;

import java.util.InputMismatchException;

import static pentastagiu.util.Constants.SCANNER;

/**
 * Helper class that implements logic for all Menu Options:
 * create new account, login/logout user, display accounts,
 * deposit and transfer between accounts, go to previous menu.
 */
public class MenuOptionService {

    private Logger LOGGER = LogManager.getLogger();
    private UserCacheService userCacheService;
    private AccountOperations accountOperations;
    private UserOperations userOperations;

    public MenuOptionService(UserCacheService userCacheService){
        this.userCacheService = userCacheService;
        this.accountOperations = new AccountOperations(userCacheService);
        this.userOperations = new UserOperations(userCacheService);
    }

    /**
     * This method creates a new account for the current logged in user.
     * Also marks the user as being able to deposit amounts and if he qualifies
     * also marks the user as being able to transfer.
     */
    public void createNewAccount(){
        Account accountCreated = new Account(userCacheService.getCurrentUser());
        DatabaseOperations.addAccountToDatabase(accountCreated);

        userCacheService.setPosibleDeposit(true);
        if (userOperations.isUserAbleToTransfer())
            userCacheService.setPosibleTransfer(true);
    }

    /**
     * This method checks the user for his credentials against the
     * database. If his credentials are ok it loads the user in the memory by
     * invoking {@link UserCacheService#loadUser()} method.
     */
    public void checkUserCredentials(){
        User userToValidate = getUserCredentials();
        boolean result = false;
        try {
            userCacheService.setCurrentUser(DatabaseOperations.validateUser(userToValidate));
            result = true;
        } catch (InvalidUserException e) {
            System.out.println("Wrong username/password.");
        }
        userCacheService.setLogged(result);
        if (userCacheService.isLogged())
            userCacheService.loadUser();
    }

    private User getUserCredentials(){
        String username = "";
        String password = "";
        try {
            System.out.print("Username:");
            username = SCANNER.nextLine();
            System.out.print("Password:");
            password = SCANNER.nextLine();
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return new User(username,password);
    }

    /**
     * This method displays all the accounts for the current logged in user.
     */
    public void displayAccounts(){
        if (userCacheService.isPosibleDeposit()) {
            System.out.println("\nList of Accounts:");
            userCacheService.loadAccountsForUser();
            DisplayService.AccountsList(userCacheService.getCurrentUser().getAccountsList());
        }else
            userCacheService.setInAccount(false);
    }

    /**
     * This method removes the current logged in user from memory
     * by invoking {@link UserCacheService#disposeUser()} method.
     */
    public void logoutUser(){
        System.out.println("Successfully logout.");
        userCacheService.disposeUser();
    }

    /**
     * This method handles the deposit to an account logic by invoking the
     * {@link AccountOperations#depositAmount()} method. Also after the deposit it checks
     * if the user is able to transfer between his accounts and marks him.
     */
    public void depositAmountToAcc(){
        if(userCacheService.isPosibleDeposit()) {
            accountOperations.depositAmount();
            if (userOperations.isUserAbleToTransfer())
                userCacheService.setPosibleTransfer(true);
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }


    /**
     * This method handles the logic for transferring between 2 accounts by
     * invoking the {@link AccountOperations#transferAmount()} method.
     */
    public void transferAmountBetweenAcc(){
        if (userCacheService.isPosibleTransfer())
            accountOperations.transferAmount();
        else if (userCacheService.isPosibleDeposit())
            userCacheService.setInAccount(false);
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method handles the logic for the menu option "Back to previous menu"
     */
    public void goToPreviousMenu(){
        if(userCacheService.isPosibleDeposit() && userCacheService.isPosibleTransfer())
            userCacheService.setInAccount(false);
        else if(userCacheService.isPosibleDeposit())
            System.out.println("Please enter a valid option(1, 2, 3 or 4).");
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method displays to the user the valid options he can choose on any menu.
     */
    public void displayValidInputOptions(){
        if (userCacheService.inAccount())
            if(userCacheService.isPosibleDeposit() && userCacheService.isPosibleTransfer())
                System.out.println("Please enter a valid option(1, 2, 3, 4 or 5).");
            else if(userCacheService.isPosibleDeposit())
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
            if (userCacheService.inAccount())
                DisplayService.AccountMenu(userCacheService);
            else if (userCacheService.isLogged())
                DisplayService.LoggedInMenu(userCacheService);
            else
                DisplayService.InitialMenu();
        }
    }
}
