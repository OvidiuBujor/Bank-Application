package pentastagiu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

/**
 * This class holds all the constants used in this project.
 */
public final class Constants {
    /**
     * Constructor is private to prevent instantiation.
     */
    private Constants() {

    }

    public static final int ACCOUNT_NUMBER = 0;
    public static final int USERNAME = 1;
    public static final int BALANCE = 2;
    public static final int ACCOUNT_TYPE = 3;

    /**
     * stores users file name to be used for ClassLoader
     */
    public static final String USERS_FILE = "users.txt";

    /**
     * Scanner object used for reading user input from console.
     */
    public static final Scanner SCANNER = new Scanner(System.in);
    /**
     * Logger used to log to console and file
     */
    public final static Logger LOGGER = LogManager.getLogger();

    /**
     * stores the database file for Accounts
     */
    public static File FILE_ACCOUNTS = new File("src/main/resources/accounts.txt");
}
