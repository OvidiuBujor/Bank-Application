package pentastagiu.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pentastagiu.model.Account;
import pentastagiu.services.DisplayService;
import pentastagiu.services.UserCacheService;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;

import static pentastagiu.repository.DatabaseOperations.updateBalanceAccount;
import static pentastagiu.util.Constants.SCANNER;

/**
 * This class handles the operations for accounts:
 * deposit and transfer between 2 valid accounts.
 */
public class AccountOperations {

    private Logger LOGGER = LogManager.getLogger();
    private UserCacheService userCacheService;
    private UserOperations userOperations;

    public AccountOperations(UserCacheService userCacheService){
        this.userCacheService = userCacheService;
        this.userOperations = new UserOperations(userCacheService);
    }
    /**
     * This method transfers an amount between 2 accounts owned by the user.
     * The accounts need to be of the same currency type and at least 1 of them
     * needs to have balance greater then 0.
     */
    public void transferAmount() {
        Account accountFrom, accountTo;
        BigDecimal amount;

        accountFrom = getAccountFrom();
        amount = getAmountToBeTransferred(accountFrom);
        accountTo = getAccountTo(accountFrom);

        updateBalanceAccount(amount.negate(),accountFrom);
        updateBalanceAccount(amount,accountTo);

        System.out.println("Transfer to Account {Account Number: " + accountTo.getAccountNumber() +
                " New Balance: " + accountTo.getBalance().toString() + " " +
                accountTo.getAccountType().toString() +"}");
    }

    /**
     * This method returns the account selected by the user to transfer FROM.
     * This account will be used by the {@link #transferAmount()} method.
     * @return the account to transfer FROM
     */
    private Account getAccountFrom(){
        List<Account> validTransferAccounts = userOperations.getValidTransferAccounts();
        Account accountFrom = new Account();
        int opt;
        try {
            if (validTransferAccounts.size() == 1) {
                accountFrom = validTransferAccounts.get(0);
            } else {
                System.out.println("\nList of Accounts to transfer FROM:");
                DisplayService.AccountsList(validTransferAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to transfer from:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if (opt < 1 || opt > validTransferAccounts.size())
                            System.out.println("Please enter a number between 1 and " + validTransferAccounts.size());
                        else {
                            accountFrom = validTransferAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        System.out.println("From Account: {Account Number: " + accountFrom.getAccountNumber() +
                " Balance: " + accountFrom.getBalance() + " " +
                accountFrom.getAccountType().toString() + "}");
        return accountFrom;
    }

    /**
     * This method gets the amount entered from console by the user.
     * This amount will be used by the {@link #transferAmount()} method.
     * @param accountFrom the account from which the transfer will be made
     * @return the amount to be transferred
     */
    private BigDecimal getAmountToBeTransferred(Account accountFrom){
        BigDecimal amount = BigDecimal.valueOf(0);
        try{
            while (true) {
                System.out.print("Insert the amount that will transferred between accounts:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    if(amount.compareTo(accountFrom.getBalance()) > 0 || amount.compareTo(new BigDecimal(0)) <= 0)
                        System.out.println("Please enter an amount greater then 0 and " +
                                "less then or equal with the current balance:" +
                                accountFrom.getBalance().toString() + " " +
                                accountFrom.getAccountType().toString());
                    else
                        break;
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return amount;
    }

    /**
     * This method gets the account chosen by the user to transfer into.
     * This account will be used by {@link #transferAmount()} method.
     * @param accountFrom the account from which we will transfer
     * @return the account where the transfer will be made
     */
    private Account getAccountTo(Account accountFrom){
        int opt;
        Account accountTo = new Account();

        try{
            List<Account> filteredAccounts = userOperations.getFilteredAccounts(accountFrom);

            if (filteredAccounts.size() == 1) {
                accountTo = filteredAccounts.get(0);
            }
            else {
                System.out.println("\nList of accounts to transfer TO:");
                DisplayService.AccountsList(filteredAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to transfer to:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if(opt < 1 || opt > filteredAccounts.size())
                            System.out.println("Please enter a number between 1 and " + filteredAccounts.size());
                        else {
                            accountTo = filteredAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return accountTo;
    }

    /**
     * This method updates the balance of the account with the amount entered
     * from console.
     */
    public void depositAmount(){
        Account accountToDeposit;
        BigDecimal amount;

        accountToDeposit = getAccountToDeposit();

        amount = getAmountToBeDeposited();

        updateBalanceAccount(amount,accountToDeposit);

        System.out.println("Updated Account: {Account Number: " + accountToDeposit.getAccountNumber() +
                " New Balance: " + accountToDeposit.getBalance().toString() + " " +
                accountToDeposit.getAccountType().toString() +"}");
    }

    /**
     * This method gets the account selected by the user to deposit/withdraw from.
     * This account will be used by {@link #depositAmount()} method.
     * @return the account where the amount will be transferred
     */
    private Account getAccountToDeposit(){
        int opt;
        userCacheService.loadAccountsForUser();
        List<Account> allAccounts = userCacheService.getCurrentUser().getAccountsList();
        Account accountToDeposit = new Account();

        try {
            if (allAccounts.size() == 1)
                accountToDeposit =  allAccounts.get(0);
            else {
                System.out.println("\nList of Accounts:");
                DisplayService.AccountsList(allAccounts);
                while (true) {
                    System.out.print("Please enter the number of the account you want to deposit in:");
                    if (!SCANNER.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        SCANNER.next();
                    } else {
                        opt = SCANNER.nextInt();
                        if (opt < 1 || opt > allAccounts.size())
                            System.out.println("Please enter a number between 1 and " + allAccounts.size());
                        else {
                            accountToDeposit =  allAccounts.get(opt - 1);
                            break;
                        }
                    }
                }
            }
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        System.out.println("\nAccount To Deposit: {Account Number: " + accountToDeposit.getAccountNumber() +
                " Balance: " + accountToDeposit.getBalance() + " " +
                accountToDeposit.getAccountType().toString() + "}");
        return accountToDeposit;
    }

    /**
     * This method gets the amount entered from console by the user.
     * This amount will be used by the {@link #depositAmount()}  method.
     * @return the amount to be deposited
     */
    private BigDecimal getAmountToBeDeposited(){
        BigDecimal amount = BigDecimal.valueOf(0);
        try{
            do{
                System.out.print("Insert the amount that will update the current balance:");
                if (!SCANNER.hasNextBigDecimal()) {
                    System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
                    SCANNER.next();
                } else {
                    amount = SCANNER.nextBigDecimal();
                    if(amount.compareTo(new BigDecimal(0)) <= 0)
                        System.out.println("Please enter a number greater then 0.");
                }
            }while(amount.compareTo(new BigDecimal(0)) <= 0);
        }catch (InputMismatchException e) {
            LOGGER.error("The input you entered was not expected.");
        }
        return amount;
    }

}
