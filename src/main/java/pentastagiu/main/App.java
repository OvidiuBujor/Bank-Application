package pentastagiu.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.services.UserCacheService;
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
        //Session session = FACTORY.getCurrentSession();
        LOGGER.info("Session created");

        try {
            DisplayService.InitialMenu();

            LoginService processUserInput = new LoginService();
            UserCacheService cachedUser = new UserCacheService();
            //session.beginTransaction();
            processUserInput.beginProcessing(cachedUser);
        } finally {
            if(!FACTORY.isClosed()) {
                LOGGER.info("Closing SessionFactory");
                FACTORY.close();
            }
        }


    }

}
