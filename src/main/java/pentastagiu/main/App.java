package pentastagiu.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import pentastagiu.model.User;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.services.UserCacheService;
import pentastagiu.services.DisplayService;
import pentastagiu.view.LoginService;

import java.time.LocalDateTime;

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
        LOGGER.info("Session created");
        try {
            //trebuie sa modific metoda de creare user doar daca nu exista deja
//            User userToBeAdded = new User("Ovidiu","123",LocalDateTime.now(),LocalDateTime.now());
//            DatabaseOperations.addUserToDatabase(userToBeAdded);
            DisplayService.InitialMenu();
            LoginService processUserInput = new LoginService();
            UserCacheService cachedUser = new UserCacheService();
            processUserInput.beginProcessing(cachedUser);
        } finally {
                LOGGER.info("Closing SessionFactory");
                FACTORY.close();
        }


    }

}
