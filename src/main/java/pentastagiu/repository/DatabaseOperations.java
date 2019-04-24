package pentastagiu.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.Account;

import java.io.File;
import java.math.BigDecimal;

import static pentastagiu.services.FileService.*;
import static pentastagiu.util.Constants.*;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account.
 */
public class DatabaseOperations {

    private static Logger LOGGER = LogManager.getLogger();
    /**
     * stores the total number of accounts and
     * it's loaded when the application starts
     */
    private static long nrOfAccounts;

    public static void setTotalNrOfAccounts(){
        nrOfAccounts = calculateNrAccFromFile(FILE_ACCOUNTS);
    }

    public static void increaseTotalNrOfAccounts(){
        nrOfAccounts++;
    }

    public static long getNrOfAccounts() {
        return nrOfAccounts;
    }

    /**
     * This method adds the account to accounts database file
     * @param account the account to be added
     */
    public static void addAccountToDatabase(Account account){
        boolean result = writeToFile(FILE_ACCOUNTS,account);
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

        File tempFile = createNewFile(balance,account);
        File accountsDatabaseFile = FILE_ACCOUNTS;

        if(accountsDatabaseFile.exists())
            accountsDatabaseFile.delete();

        tempFile.renameTo(accountsDatabaseFile);
    }
}
