package pentastagiu.util;

import pentastagiu.cache.UserCacheService;
import pentastagiu.model.Account;

import java.util.List;
/**
 * This class prints the different menus based
 * on user input from console
 */
public class Menu {

    /**
     * This method prints Initial Menu.
     */
    public static void printInitialMenu() {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Log in");
        System.out.println("2.Exit");
        System.out.print("Please enter your option:");
    }

    /**
     * This method prints the state of the menu when the current user is logged in.
     * @param userCacheService cached user
     */
    public static void printLoggedInMenu(UserCacheService userCacheService) {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Inspect account '" + userCacheService.getCurrentUser().getUsername() + "'");
        System.out.println("2.Logout");
        System.out.print("Please enter your option:");
    }

    /**
     * This method prints the state of the menu when the current user is in his account.
     * @param userCacheService cached user
     */
    public static void printAccountMenu(UserCacheService userCacheService) {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Create account");
        if(userCacheService.isPosibleDeposit()) {
            System.out.println("2.Display accounts for " + userCacheService.getCurrentUser().getUsername());
            System.out.println("3.Deposit/withdraw amount(enter negative value)");
            if (userCacheService.isPosibleTransfer()) {
                System.out.println("4.Transfer amount between your accounts");
                System.out.println("5.Back to previous menu");
            }else
                System.out.println("4.Back to previous menu");
        }
        else
            System.out.println("2.Back to previous menu");
        System.out.print("Please enter your option:");
    }

    /**
     * This method prints all the accounts owned by the current user.
     * @param accounts the list of all acccounts owned by the user
     */
    public static void printAccounts(List<Account> accounts){
        int lineNumber = 1;
        for (Account account : accounts) {
            printAccount(account, lineNumber);
            lineNumber++;
        }
    }

    /**
     * This method is used to print an account is a specific format
     * can't use Account.toString because that has a different format
     * and it used for adding an Account to Database
     * @param account the account to be printed
     * @param lineNumber the order number of the account that is printed
     */
    private static void printAccount(Account account,int lineNumber){
        System.out.print(lineNumber + ". Account{" +
                "accountNumber='" + account.getAccountNumber() + "\'" +
                ", balance=" + account.getBalance() +
                ", accountType='" + account.getAccountType() + "\'" +
                "}\n");
    }
}
