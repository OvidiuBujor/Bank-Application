package pentastagiu.main;

import pentastagiu.cache.UserCacheService;
import pentastagiu.files.Database;
import pentastagiu.util.Login;
import pentastagiu.util.Menu;

/**
 * This class is the Main class of the project and the starting point.
 */
public class App {

    /**
     * Main method that starts the application.
     * Populates list of users. Sets total number of accounts(this
     * is used for generating new accounts).
     * Begins processing user input.
     * @param args the arguments list for the project
     */
    public static void main(String[] args) {

        Database internalDatabase = Database.getInstance();

        internalDatabase.populateUsers();
        internalDatabase.setTotalNrOfAccounts();

        Menu.printInitialMenu();
        Login processUserInput = new Login();
        UserCacheService userCacheService = new UserCacheService();
        processUserInput.beginProcessing(userCacheService);
    }

}
