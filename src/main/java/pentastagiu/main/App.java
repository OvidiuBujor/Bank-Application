package pentastagiu.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pentastagiu.model.*;
import pentastagiu.repository.DatabaseOperations;
import pentastagiu.services.UserCacheService;
import pentastagiu.services.DisplayService;
import pentastagiu.view.LoginService;

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

        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Notification.class)
                .addAnnotatedClass(Transation.class)
                .addAnnotatedClass(Person.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        LOGGER.info("Session created");

        try {
            session.beginTransaction();

            //session.save();

            session.getTransaction().commit();
            DatabaseOperations.setTotalNrOfAccounts();

            DisplayService.InitialMenu();

            LoginService processUserInput = new LoginService();
            UserCacheService cachedUser = new UserCacheService();

            processUserInput.beginProcessing(cachedUser);

        } finally {
            if(!factory.isClosed()) {
                LOGGER.info("Closing SessionFactory");
                factory.close();
            }
        }


    }

}
