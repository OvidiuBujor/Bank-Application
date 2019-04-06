package pentastagiu.util;

import pentastagiu.cache.UserCacheService;

import java.util.InputMismatchException;

import static pentastagiu.util.Constants.LOGGER;
import static pentastagiu.util.Constants.SCANNER;

/**
 * This class implements a menu with option of "Log in" users
 * based on the username and password entered in console.
 * The validation of login is based on a database file that
 * contains username and passwords for all registered users.
 * Other operations are:
 *  - the user can check his currents accounts and create new ones.
 *  - the user can deposit/withdraw amounts from his accounts
 *  - the user can transfer between his accounts
 */
public class Login {

    /**
     * This method process the user input.
     * In case of option:
     * <ul>
     * <li> "Log in" checks the user credentials invoking
     * {@link UserCacheService#checkUserCredentials()} method.</li>
     * <li> "Display accounts .." displays accounts for current user invoking the
     * {@link UserCacheService#displayAccounts()} method.</li>
     * <li> "Create account" creates a new account invoking
     * {@link UserCacheService#createNewAccount()} method..</li>
     * <li> "Deposit/withdraw amount ..." invokes {@link UserCacheService#depositAmount()} method
     *      to update the balance of the account.</li>
     * <li> "Transfer amount between your accounts" invokes {@link UserCacheService#transferAmountBetweenAcc()}
     * method to transfer between 2 accounts owned by the user.
     * Also 1 of these accounts need to have a balance greater then 0.</li>
     * <li> "Back to previous menu" displays the previous menu.</li>
     * <li> "Exit" terminates the program.</li>
     * </ul>
     * @param userCacheService the cached user that is logged in
     */
    public void beginProcessing(UserCacheService userCacheService) {
        while (true) {
            try {
                while (SCANNER.hasNext()) {
                    String opt = SCANNER.nextLine();
                    switch (opt) {
                        case "1":
                            if (userCacheService.inAccount())
                                userCacheService.createNewAccount();
                            else if (userCacheService.isLogged())
                                userCacheService.setInAccount(true);
                            else
                                userCacheService.checkUserCredentials();
                            break;
                        case "2":
                            if (userCacheService.inAccount())
                                userCacheService.displayAccounts();
                            else if (userCacheService.isLogged())
                                userCacheService.logoutUser();
                            else {
                                System.out.println("Bye!");
                                System.exit(0);
                            }
                            break;
                        case "3":
                            if (userCacheService.inAccount())
                                userCacheService.depositAmount();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case "4":
                            if (userCacheService.inAccount())
                                userCacheService.transferAmount();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case "5":
                            if (userCacheService.inAccount())
                                userCacheService.goToPreviousMenu();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case ""://added for scanner.nextBigDecimal() that reads an empty string after the BigDecimal
                            break;
                        default:
                            if (userCacheService.inAccount())
                                System.out.println("Please enter a valid option(1, 2, 3, 4 or 5).");
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                    }
                    if(!opt.equals("")) { //added for scanner.nextBigDecimal() that reads an empty string after the BigDecimal
                        if (userCacheService.inAccount())
                            Menu.printAccountMenu(userCacheService);
                        else if (userCacheService.isLogged())
                            Menu.printLoggedInMenu(userCacheService);
                        else
                            Menu.printInitialMenu();
                    }
                }
            } catch (InputMismatchException e) {
                LOGGER.error("The input you entered was not expected.");
            }finally {
                SCANNER.close();
            }
        }
    }
}