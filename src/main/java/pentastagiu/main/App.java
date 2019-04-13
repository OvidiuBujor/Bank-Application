package pentastagiu.main;

import pentastagiu.operations.DatabaseOperations;
import pentastagiu.services.UserCacheService;
import pentastagiu.services.DisplayService;
import pentastagiu.services.LoginService;

/**
 * This class is the Main class of the project and the starting point.
 */
public class App {

    /**
     * Main method that starts the application.
     * Sets total number of accounts(this
     * is used for generating new accounts).
     * Begins processing user input.
     * @param args the arguments list for the project
     */
    public static void main(String[] args) {

        DatabaseOperations.setTotalNrOfAccounts();

        DisplayService.InitialMenu();

        LoginService processUserInput = new LoginService();
        UserCacheService cachedUser = new UserCacheService();

        processUserInput.beginProcessing(cachedUser);
    }

}
