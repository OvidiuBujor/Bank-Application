package pentastagiu.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import pentastagiu.model.*;
import pentastagiu.util.InvalidUserException;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        long nrAccounts = session.createQuery("from Account").getResultList().size();
        session.close();
        return nrAccounts;
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
        session.close();
        if (session.getTransaction().getStatus() == TransactionStatus.COMMITTED) {
            LOGGER.info("Account added successfully.");
            System.out.println("Account added successfully.");
        }
        else
            LOGGER.warn("Account wasn't added. Please check log file for details.");
    }

    public static void addUserToDatabase(User userToBeAdded){
        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        List resultList = new ArrayList();
        try {
            resultList = session.createQuery("from User where username = '" + userToBeAdded.getUsername() + "'").getResultList();
        }catch (NoResultException e) {
            LOGGER.info("No user found.");
        }

        if (resultList.size() == 0) {
            session.saveOrUpdate(userToBeAdded);
            session.getTransaction().commit();
        }
        session.close();
        if (session.getTransaction().getStatus() == TransactionStatus.COMMITTED) {
            LOGGER.info("User '" + userToBeAdded.getUsername() + "' added successfully.");
            System.out.println("User '" + userToBeAdded.getUsername() + "' added successfully.");
        }
        else {
            LOGGER.warn("User already exists. No user was added.");
            System.out.println("User '" + userToBeAdded.getUsername() +"' already exists. No user was added.");
        }
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
        session.createQuery("update Account set balance = " + balance + ",updated_time = '" + LocalDateTime.now() +
                                "' where id = " + account.getId()).executeUpdate();
        session.close();
    }

    /**
     * This method records a Transaction between two accounts
     * @param accountFrom where we transfer
     * @param amount that is transferred
     * @param accountTo where we tarnsfer
     * @param details about transaction
     */
    public static void saveTransaction(Account accountFrom, BigDecimal amount, Account accountTo, String details, TransactionType transactionType) {
        Transaction transactionToBeSaved = new Transaction();

        transactionToBeSaved.setAccount(accountTo.getAccountNumber());
        transactionToBeSaved.setAmount(amount);
        transactionToBeSaved.setDetails(details);
        transactionToBeSaved.setCreatedTime(LocalDateTime.now());
        transactionToBeSaved.setAccountID(accountFrom);
        transactionToBeSaved.setType(transactionType);

        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        session.save(transactionToBeSaved);
        session.getTransaction().commit();
        session.close();

        if (session.getTransaction().getStatus() == TransactionStatus.COMMITTED) {
            LOGGER.info("Transaction saved successfully.");
            System.out.println("Transaction saved successfully.");
        }
        else
            LOGGER.warn("Transaction wasn't saved. Please check log file for details.");
    }

    /**
     * adds a notification for a transaction
     * @param details the details of the transaction
     */
    public static void addNotification(User cachedUser, String details){
        Notification notificationToBeSaved = new Notification();

        notificationToBeSaved.setDetails(details);
        notificationToBeSaved.setCreatedTime(LocalDateTime.now());
        notificationToBeSaved.setSentTime(LocalDateTime.now());
        notificationToBeSaved.setUser(cachedUser);

        Session session = FACTORY.getCurrentSession();
        session.beginTransaction();
        session.save(notificationToBeSaved);
        session.getTransaction().commit();
        session.close();

        if (session.getTransaction().getStatus() == TransactionStatus.COMMITTED) {
            LOGGER.info("Notification saved successfully.");
            System.out.println("Notification saved successfully.");
        }
        else
            LOGGER.warn("Notification wasn't saved. Please check log file for details.");
    }

    /**
     * This method validates the user's credentials against the database file
     * @param userCredentials the user credentials to be validated
     * @return the user if user's credentials are valid
     * @throws InvalidUserException when the user's credentials are not valid
     */
    public static User validateUser(String[] userCredentials) throws InvalidUserException {
        Session session = FACTORY.getCurrentSession();
        try (session) {
            if (!session.getTransaction().isActive()) session.beginTransaction();
            return (User) session.createQuery("from User where username = '" + userCredentials[USERNAME] +
                    "' and password = '" + userCredentials[PASSWORD] + "'").getSingleResult();
        } catch (NoResultException e) {
            throw new InvalidUserException("User credentials are not correct.");
        }
    }

    /**
     * This method creates the list of accounts for user
     * @param currentUser the user for which we create the accounts list
     * @return the account list
     */
    public static List<Account> readAccountsForUser(User currentUser){
        List<Account> listAccounts = new ArrayList<>();
        Session session = FACTORY.getCurrentSession();
        try (session) {
            session.beginTransaction();
            listAccounts = (List<Account>)session.createQuery("from Account where user_id = " + currentUser.getId()).list();
        } catch (NoResultException e) {
            LOGGER.info("No accounts for user.");
        }
        return listAccounts;
    }
}
