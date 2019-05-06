package pentastagiu.services;

import pentastagiu.model.Account;

import java.util.List;
/**
 * This class displays the different menus based
 * on user input from console also it displays all
 * accounts for an user.
 */
public class DisplayService {

    /**
     * This method displays InitialMenu.
     */
    public static void InitialMenu() {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Log in");
        System.out.println("2.Exit");
        System.out.print("Please enter your option:");
    }

    /**
     * This method displays the state of the menu when the current user is logged in.
     * @param userCacheService cached user
     */
    public static void LoggedInMenu(UserCacheService userCacheService) {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Inspect account '" + userCacheService.getCurrentUser().getUsername() + "'");
        System.out.println("2.Logout");
        System.out.print("Please enter your option:");
    }

    /**
     * This method displays the state of the menu when the current user is in his account.
     * @param cachedUser cached user
     */
    public static void AccountMenu(UserCacheService cachedUser) {
        System.out.println("\n-----Bank App------");
        System.out.println("1.Create account");
        if(cachedUser.isPosibleDeposit()) {
            System.out.println("2.Display accounts for " + cachedUser.getCurrentUser().getUsername());
            System.out.println("3.Deposit amount");
            if (cachedUser.isPosibleTransfer()) {
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
     * This method displays all the accounts owned by the current user.
     * @param accounts the list of all accounts owned by the user
     */
    public static void AccountsList(List<Account> accounts){
        int lineNumber = 1;
        for (Account account : accounts) {
            printAccount(account, lineNumber);
            lineNumber++;
        }
    }

    /**
     * This method is used to print an account is a specific format.
     * Can't use {@link Account#toString()} method because that has
     * a different format and it used for adding an AccountMenu to Database.
     * @param account the account to be printed
     * @param lineNumber the order number of the account that is printed
     */
    private static void printAccount(Account account,int lineNumber){
        System.out.println(lineNumber + ". Account{" +
                "accountNumber='" + account.getAccountNumber() + "\'" +
                ", balance=" + account.getBalance() +
                ", accountType='" + account.getAccountType() + "\'" +
                "}");
    }
}
