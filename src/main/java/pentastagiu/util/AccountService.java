package pentastagiu.util;

import pentastagiu.files.Database;
import pentastagiu.model.Account;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static pentastagiu.util.Constants.*;

public class AccountService {

    public static boolean validateAccount(String[] accountDetails){
        return accountDetails.length == 4 &&
                validateAccountNumber(accountDetails[ACCOUNT_NUMBER].toUpperCase()) &&
                validateAccountBalance(accountDetails[BALANCE]) &&
                validateAccountType(accountDetails[ACCOUNT_TYPE].toUpperCase());
    }
    /**
     * This method validates account number entered from console.
     * @param accountNumber the account number to be validated
     * @return true if the account number is valid; false otherwise
     */
    private static boolean validateAccountNumber(String accountNumber){
        return Pattern.matches("RO[0-9]{2}[A-Z]{4}[0-9]{16}", accountNumber.toUpperCase());
    }

    /**
     * This method validates the balance of the acount
     * @param balance the string that will be validated
     * @return true if the string is a BigDecimal; false otherwise
     */
    private static boolean validateAccountBalance(String balance){
        try {
            new BigDecimal(balance);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * This method validates the account type entered from console.
     * Valid types of account are RON or EUR.
     * @param accountType the account type entered from console
     * @return true if account type is correct; false otherwise
     */
    public static boolean validateAccountType(String accountType){
        return accountType.toUpperCase().equals("RON") || accountType.toUpperCase().equals("EUR");
    }

    /**
     * This method updates the balance  of the account.
     * Updates the database of accounts by invoking
     * {@link Database#updateBalanceAccountInDatabase(BigDecimal,Account) updateBalanceAccountInDatabase} static method.
     * @param amount the amount entered from console
     */
    public static void updateBalanceAccount(BigDecimal amount, Account account){
        BigDecimal balance = account.getBalance();
        balance = balance.add(amount);
        account.setBalance(balance);
        Database.updateBalanceAccountInDatabase(balance,account);
    }
}
