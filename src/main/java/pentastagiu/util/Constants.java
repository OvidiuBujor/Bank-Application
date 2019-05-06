package pentastagiu.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pentastagiu.model.*;

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

    public static final int USERNAME = 0;
    public static final int PASSWORD = 1;

    /**
     * Scanner object used for reading user input from console.
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    /**
     * SessionFactory object used to get current session to access the database
     */
    public static final SessionFactory FACTORY = new Configuration().configure()
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Account.class)
            .addAnnotatedClass(Notification.class)
            .addAnnotatedClass(Transaction.class)
            .addAnnotatedClass(Person.class)
            .buildSessionFactory();
}
