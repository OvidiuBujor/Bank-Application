package pentastagiu.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import pentastagiu.model.User;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.services.UserService;
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
//        try{
//            User userToBeAdded;
//            userToBeAdded = new User("Ovidiu", "123", LocalDateTime.now(), LocalDateTime.now());
//            DatabaseOperations.addUserToDatabase(userToBeAdded);
//        }catch(ConstraintViolationException e){
//            System.out.println("User already exists. No user was added.");
//        }

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
