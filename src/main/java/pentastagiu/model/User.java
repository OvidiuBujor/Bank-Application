package pentastagiu.model;
import pentastagiu.files.OperationFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static pentastagiu.util.Constants.*;

/**
 * This class holds information for an user:
 * username, password, state of the user(regarding
 * login) and the list of accounts owned.
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

    /**
     * This method populates the list of accounts that the user owns
     * from the accounts database file.
     */
    public void setAccountsList() {
        accountsList.addAll(OperationFile.readAccountsFromFileForUser(FILE_ACCOUNTS,this));
    }

    /**
     * This method adds a new created account to the current user's
     * {@link #accountsList accounts list}.
     * @param account the new account that is added to the current user
     */
    public void addAccountToUserAccountsList(Account account){
        accountsList.add(account);
    }

    /**
     * This method returns the list of filtered accounts
     * based on the currency of the account received as parameter
     * except this account.
     * @param accountFrom the account that we filter by
     * @return the list of accounts with the same currency excluding it
     */
    List<Account> getFilteredAccounts(Account accountFrom) {
        List<Account> filteredAccounts = new ArrayList<>();
        for(Account account: accountsList){
            if(account.getAccountType() == accountFrom.getAccountType() &&
                    !account.getAccountNumber().equals(accountFrom.getAccountNumber()))
                filteredAccounts.add(account);
        }
        return filteredAccounts;
    }

    /**
     * @return the list of accounts with balance greater then 0.
     */
    List<Account> getValidTransferAccounts() {
        List<Account> validTransferedAccounts = new ArrayList<>();
        for(Account account: accountsList){
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0)
                validTransferedAccounts.add(account);
        }
        return validTransferedAccounts;
    }

    /**
     * This method returns the number of accounts with the currency
     * specified by the parameter account_type
     * @param account_type specifies the currency that we filter by
     * @return the list of accounts with that currency
     */
    public int getTotalNumberOfAccountsForAccType(ACCOUNT_TYPES account_type){
        int totalNumber = 0;
        for(Account account: accountsList)
            if(account.getAccountType() == account_type)
                totalNumber++;
        return totalNumber;
    }

    /**
     * This method returns the number of accounts that can be used to transfer from
     * with the currency specified by the parameter account_type
     * @param account_type the curency we filter by
     * @return number of accounts that satisfy these filters
     */
    public int getTotalNumberOfValidTransferAccounts(ACCOUNT_TYPES account_type){
        int totalNumber = 0;
        List<Account> validTransferedAccounts = getValidTransferAccounts();
        for(Account account: validTransferedAccounts)
            if(account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
                    account.getAccountType() == account_type)
                totalNumber++;
        return totalNumber;
    }

    /**
     * Getter for username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for acccounts list owned by the user.
     * @return the accounts list
     */
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
