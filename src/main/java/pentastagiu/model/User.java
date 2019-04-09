package pentastagiu.model;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static pentastagiu.util.Constants.*;

/**
 * This class holds information for an user:
 * username, password and the list of accounts owned.
 */
public class User {
    private String username;
    private String password;

    /**
     * The list of all accounts owned by the user
     */
    private List<Account> accountsList = new ArrayList<>();

    /**
     * Constructs an user with the information received
     * @param username of the new User that is created
     * @param password of the new User that is created
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * This constructor creates an user from information
     * entered from console.
     */
    public User(){
        try {
            System.out.print("Username:");
            this.username = SCANNER.nextLine();
            System.out.print("Password:");
            this.password = SCANNER.nextLine();
        } catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
    }

    public String getUsername() {
        return username;
    }

    public List<Account> getAccountsList() {
        return accountsList;
    }

    /**
     * This method overrites the equals method used to compare 2 users.
     * @param o the user that we compare with current instance
     * @return true if the users are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }


}
