package pentastagiu.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import pentastagiu.model.Account;
import pentastagiu.model.User;
import pentastagiu.util.InvalidUserException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static pentastagiu.util.Constants.*;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account.
 */
public class DatabaseOperations {

    private static Logger LOGGER = LogManager.getLogger();

    /**
     * This method calculates the number of valid accounts from
     * the database file.
     * @return the number of valid accounts
     */
    public static long calculateNrAccFromFile(){
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        return session.createQuery("from Account").getResultList().size();
    }

    /**
     * This method adds the account to accounts database file
     * @param account the account to be added
     */
    public static void addAccountToDatabase(Account account){
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        session.save(account);
        session.getTransaction().commit();
        if (session.getTransaction().getStatus() == TransactionStatus.COMMITTED)
            LOGGER.info("Account added successfully.");
        else
            LOGGER.warn("Account wasn't added. Please check log file for details.");
    }

    /**
     * This method updates the balance of the account in the database
     * @param amount the new balance that will be updated
     * @param account the account that is updated
     */
    public static void updateBalanceAccount(BigDecimal amount, Account account){
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        BigDecimal balance = account.getBalance();
        balance = balance.add(amount);
        account.setBalance(balance);
        account.setUpdatedTime(LocalDateTime.now());
        session.getTransaction().commit();
    }

    /**
     * This method validates the user's credentials against the database file
     * @param currentUser the user to be validated
     * @return true if user's credentials are valid
     * @throws InvalidUserException when the user's credentials are not valid
     */
    public static boolean validateUser(User currentUser) throws InvalidUserException {
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        if(session.createQuery("select username from User where username = '" + currentUser.getUsername() +
                "' and password = " + currentUser.getPassword()).list().size() == 1)
            return true;
        else
            throw new InvalidUserException("User credentials are not correct.");
    }

    /**
     * This method creates the list of accounts for user
     * @param currentUser the user for which we create the accounts list
     * @return the account list
     */
    public static List<Account> readAccountsForUser(User currentUser){
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        return  session.createQuery("from Account where user_id = " + currentUser.getId()).list();
    }
}
