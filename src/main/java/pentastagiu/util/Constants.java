package pentastagiu.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
     * This enum holds the valid types of account type.
     */
    public enum ACCOUNT_TYPES
    {
        RON("RON"), EUR("EUR");

        private String text;

        ACCOUNT_TYPES(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        /**
         * This method transforms a string into an ACCOUNT_TYPES
         * @param text the string that will be transformed
         * @return the corresponding ACCOUNT_TYPE
         */
        public static ACCOUNT_TYPES fromString(String text) {
            for (ACCOUNT_TYPES account_type : ACCOUNT_TYPES.values()) {
                if (account_type.text.equalsIgnoreCase(text)) {
                    return account_type;
                }
            }
            return null;
        }
    }

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
     * stores the users list
     */
    public final static List<User> USERS_LIST = new ArrayList<>();
    /**
     * stores the database file for Users
     */
    public static File FILE_USERS = new File("src/main/resources/users.txt");
    /**
     * stores the database file for Accounts
     */
    public static File FILE_ACCOUNTS = new File("src/main/resources/accounts.txt");
}
