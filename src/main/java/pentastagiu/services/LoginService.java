package pentastagiu.services;

import java.util.InputMismatchException;

import static pentastagiu.util.Constants.LOGGER;
import static pentastagiu.util.Constants.SCANNER;

/**
 * This class implements a menu with option of "Log in" users
 * based on the username and password entered in console.
 * The validation of login is based on a database file that
 * contains all registered users.
 * Other operations are:
 *  - the user can check his currents accounts and create new ones
 *  - the user can deposit amounts to his accounts
 *  - the user can transfer between his accounts if possible
 */
public class LoginService {

    /**
     * This method process the user input.
     * In case of option:
     * <ul>
     * <li> "Log in" checks the user credentials invoking
     * {@link MenuOptionService#checkUserCredentials()} method.</li>
     * <li> "Display accounts .." displays accounts for current user invoking the
     * {@link MenuOptionService#displayAccounts()} method.</li>
     * <li> "Create account" creates a new account invoking
     * {@link MenuOptionService#createNewAccount()} method..</li>
     * <li> "Deposit amount" invokes {@link MenuOptionService#depositAmountToAcc()} method
     *      to update the balance of the account.</li>
     * <li> "Transfer amount between your accounts" invokes {@link MenuOptionService#transferAmountBetweenAcc()}
     * method to transfer between 2 accounts owned by the user.
     * Also 1 of these accounts need to have a balance greater then 0.</li>
     * <li> "Back to previous menu" displays the previous menu.</li>
     * <li> "Exit" terminates the program.</li>
     * </ul>
     * @param userCacheService the cached user that is logged in
     */
    public void beginProcessing(UserCacheService userCacheService) {

        MenuOptionService handleMenuOptions = new MenuOptionService(userCacheService);

        while (true) {
            try {
                while (SCANNER.hasNext()) {
                    String opt = SCANNER.nextLine();
                    switch (opt) {
                        case "1":
                            if (userCacheService.inAccount())
                                handleMenuOptions.createNewAccount();
                            else if (userCacheService.isLogged())
                                userCacheService.setInAccount(true);
                            else
                                handleMenuOptions.checkUserCredentials();
                            break;
                        case "2":
                            if (userCacheService.inAccount())
                                handleMenuOptions.displayAccounts();
                            else if (userCacheService.isLogged())
                                handleMenuOptions.logoutUser();
                            else {
                                System.out.println("Bye!");
                                System.exit(0);
                            }
                            break;
                        case "3":
                            if (userCacheService.inAccount())
                                handleMenuOptions.depositAmountToAcc();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case "4":
                            if (userCacheService.inAccount())
                                handleMenuOptions.transferAmountBetweenAcc();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case "5":
                            if (userCacheService.inAccount())
                                handleMenuOptions.goToPreviousMenu();
                            else
                                System.out.println("Please enter a valid option(1 or 2).");
                            break;
                        case ""://added for scanner.nextBigDecimal() that reads an empty string after the BigDecimal
                            break;
                        default:
                            handleMenuOptions.displayValidInputOptions();
                            break;
                    }
                    handleMenuOptions.displayTheMenu(opt);
                }
            } catch (InputMismatchException e) {
                LOGGER.error("The input you entered was not expected.");
            }finally {
                SCANNER.close();
            }
        }
    }
}