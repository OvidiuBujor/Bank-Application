package pentastagiu.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pentastagiu.model.*;

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
     * Scanner object used for reading user input from console.
     */
    public static final Scanner SCANNER = new Scanner(System.in);

    public static SessionFactory FACTORY = new Configuration().configure("hibernate.cfg.xml")
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Account.class)
            .addAnnotatedClass(Notification.class)
            .addAnnotatedClass(Transation.class)
            .addAnnotatedClass(Person.class)
            .buildSessionFactory();
}
