package pentastagiu.files;

import pentastagiu.model.Account;

import java.io.File;
import java.math.BigDecimal;

import static pentastagiu.util.Constants.*;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account,
 * populates the users list.
 */
public class Database{

    private static Database instance = new Database();

    /**
     * Private constructor to prevent instatiation - Singleton.
     */
    private Database(){
    }

    /**
     * @return the ONLY instance of the class
     */
    public static Database getInstance(){
        return instance;
    }

    /**
     * This method populates the users list from our database file.
     */
    public void populateUsers(){
        USERS_LIST.addAll(OperationFile.readUsersFromFile(USERS_FILE));
    }

    public void setTotalNrOfAccounts(){
        Account.setNrOfAccounts(OperationFile.calculateNrAccFromFile(FILE_ACCOUNTS));
    }

    /**
     * This method adds the account to accounts database file
     * @param account the account to be added
     */
    public static void addAccountToDatabase(Account account){
        boolean result = OperationFile.writeToFile(FILE_ACCOUNTS,account);
        if (result)
            LOGGER.info("Account added successfully.");
        else
            LOGGER.warn("Account wasn't added. Please check log file for details.");
    }

    /**
     * This method updates the balance of the account in the database
     * by creating a copy of the accounts file with the updated balance
     * and rename it to accounts file name.
     * @param balance the new balance that will be updated
     * @param account the account that is updated
     */
    public static void updateBalanceAccountInDatabase(BigDecimal balance, Account account){

        File tempFile = OperationFile.createNewFile(balance,account);
        File accountsDatabaseFile = FILE_ACCOUNTS;

        if(accountsDatabaseFile.exists())
            accountsDatabaseFile.delete();

        tempFile.renameTo(accountsDatabaseFile);
    }
}
