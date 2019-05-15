package pentastagiu.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.services.UserService;
import pentastagiu.services.DisplayService;
import pentastagiu.view.LoginService;

import static pentastagiu.util.Constants.FACTORY;

/**
 * This class is the Main class of the project and the starting point.
 */
public class App {

    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main method that starts the application.
     * Sets total number of accounts(this
     * is used for generating new accounts).
     * Begins processing user input.
     * @param args the arguments list for the project
     */
    public static void main(String[] args) {
        UserService userService = new UserService();
        LOGGER.info("Session created");
        userService.createInitialsUsers();
        try {
            DisplayService.InitialMenu();
            LoginService processUserInput = new LoginService();
            UserService cachedUser = new UserService();
            processUserInput.beginProcessing(cachedUser);
        }
        finally {
            LOGGER.info("Closing SessionFactory");
            FACTORY.close();
        }
    }

}
