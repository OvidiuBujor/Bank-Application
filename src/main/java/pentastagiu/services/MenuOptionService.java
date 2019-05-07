package pentastagiu.services;

import pentastagiu.model.Account;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.util.InvalidUserException;

/**
 * Helper class that implements logic for all Menu Options:
 * create new account, login/logout user, display accounts,
 * deposit and transfer between accounts, go to previous menu.
 */
public class MenuOptionService {

    private UserService userService;
    private AccountService accountService;

    public MenuOptionService(UserService userService){
        this.userService = userService;
        this.accountService = new AccountService();
    }

    /**
     * This method creates a new account for the current logged in user.
     * Also marks the user as being able to deposit amounts and if he qualifies
     * also marks the user as being able to transfer.
     */
    public void createNewAccount(){
        Account accountCreated = new Account(userService.getCurrentUser());
        DatabaseOperations.addAccountToDatabase(accountCreated);

        userService.setPosibleDeposit(true);
        if (userService.isPosibleTransfer())
            userService.setPosibleTransfer(true);
    }

    /**
     * This method checks the user for his credentials against the
     * database. If his credentials are ok it loads the user in the memory by
     * invoking {@link UserService#loadUser()} method.
     */
    public void checkUserCredentials(){
        boolean result = false;
        try {
            userService.setCurrentUser(DatabaseOperations.validateUser(userService.getUserCredentials()));
            result = true;
        } catch (InvalidUserException e) {
            System.out.println("Wrong username/password.");
        }
        userService.setLogged(result);
        if (userService.isLogged())
            userService.loadUser();
    }

    /**
     * This method displays all the accounts for the current logged in user.
     */
    public void displayAccounts(){
        if (userService.isPosibleDeposit()) {
            System.out.println("\nList of Accounts:");
            accountService.loadAccountsForUser(userService.getCurrentUser());
            DisplayService.AccountsList(userService.getCurrentUser().getAccountsList());
        }else
            userService.setInAccount(false);
    }

    /**
     * This method removes the current logged in user from memory
     * by invoking {@link UserService#disposeUser()} method.
     */
    public void logoutUser(){
        System.out.println("Successfully logout.");
        userService.disposeUser();
    }

    /**
     * This method handles the deposit to an account logic by invoking the
     * {@link AccountService#depositAmount(pentastagiu.model.User)} method. Also after the deposit it checks
     * if the user is able to transfer between his accounts and marks him.
     */
    public void depositAmountToAcc(){
        if(userService.isPosibleDeposit()) {
            accountService.depositAmount(userService.getCurrentUser());
            if (userService.isPosibleTransfer())
                userService.setPosibleTransfer(true);
        }else
            System.out.println("Please enter a valid option(1 or 2).");
    }


    /**
     * This method handles the logic for transferring between 2 accounts by
     * invoking the {@link AccountService#transferAmount(pentastagiu.model.User)} method.
     */
    public void transferAmountBetweenAcc(){
        if (userService.isPosibleTransfer())
            accountService.transferAmount(userService.getCurrentUser());
        else if (userService.isPosibleDeposit())
            userService.setInAccount(false);
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method handles the logic for the menu option "Back to previous menu"
     */
    public void goToPreviousMenu(){
        if(userService.isPosibleDeposit() && userService.isPosibleTransfer())
            userService.setInAccount(false);
        else if(userService.isPosibleDeposit())
            System.out.println("Please enter a valid option(1, 2, 3 or 4).");
        else
            System.out.println("Please enter a valid option(1 or 2).");
    }

    /**
     * This method displays to the user the valid options he can choose on any menu.
     */
    public void displayValidInputOptions(){
        if (userService.inAccount())
            if(userService.isPosibleDeposit() && userService.isPosibleTransfer())
                System.out.println("Please enter a valid option(1, 2, 3, 4 or 5).");
            else if(userService.isPosibleDeposit())
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
            if (userService.inAccount())
                DisplayService.AccountMenu(userService);
            else if (userService.isLogged())
                DisplayService.LoggedInMenu(userService);
            else
                DisplayService.InitialMenu();
        }
    }
}
