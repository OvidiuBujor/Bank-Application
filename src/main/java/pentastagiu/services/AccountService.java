package pentastagiu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pentastagiu.model.Account;
import pentastagiu.repository.AccountRepository;

import java.util.List;

/**
 * This class handles the operations for accounts:
 * deposit and transfer between 2 valid accounts.
 */
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<Account> getAccounts(Integer userID){
            return accountRepository.getAccountByUserID(userID);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

//    private Logger LOGGER = LogManager.getLogger();

//    /**
//     *
//     * This method populates the list of accounts that the user owns
//     * from the accounts database file.
//     * @param cachedUser logged in user
//     */
//    public void loadAccountsForUser(User cachedUser) {
//        cachedUser.setAccountsList(userRepository.findByUserId(cachedUser.getId()));
////        cachedUser.setAccountsList(AccountRepository.readAccountsForUser(cachedUser));
//    }
//
//    /**
//     * This method transfers an amount between 2 accounts owned by the user.
//     * The accounts need to be of the same currency type and at least 1 of them
//     * needs to have balance greater then 0.
//     * @param cachedUser current logged in user
//     */
//    public void transferAmount(User cachedUser) {
//        Account accountFrom, accountTo;
//        BigDecimal amount;
//        String details;
//
//        accountFrom = getAccountFrom(cachedUser);
//        amount = getAmountToBeTransferred(accountFrom);
//        details = getDetails();
//        accountTo = getAccountTo(cachedUser, accountFrom);
//
//        accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
//        accountRepository.save(accountFrom);
//
////        updateBalanceAccount(amount.negate(),accountFrom);
//
//        accountTo.setBalance(accountTo.getBalance().add(amount));
//        accountRepository.save(accountTo);
//
////        updateBalanceAccount(amount,accountTo);
//
//        Transaction transactionToBeSaved = new Transaction();
//
//        transactionToBeSaved.setAccount(accountTo.getAccountNumber());
//        transactionToBeSaved.setAmount(amount);
//        transactionToBeSaved.setDetails(details);
//        transactionToBeSaved.setAccountID(accountFrom);
//        transactionToBeSaved.setType(TransactionType.outgoing);
//
//        transactionRepository.save(transactionToBeSaved);
//
//        LOGGER.info("Transaction saved successfully.");
//        System.out.println("Transaction saved successfully.");
//
//        transactionToBeSaved.setAccount(accountFrom.getAccountNumber());
//        transactionToBeSaved.setAmount(amount);
//        transactionToBeSaved.setDetails(details);
//        transactionToBeSaved.setAccountID(accountTo);
//        transactionToBeSaved.setType(TransactionType.incoming);
//
//        transactionRepository.save(transactionToBeSaved);
//
//        LOGGER.info("Transaction saved successfully.");
//        System.out.println("Transaction saved successfully.");
//
//
////        saveTransaction(accountFrom, amount, accountTo, details, TransactionType.outgoing);
////        saveTransaction(accountTo, amount, accountFrom, details, TransactionType.incoming);
//
//        String transactionDetails = "From account: " + accountFrom.getAccountNumber() +
//                                    " To account: " + accountTo.getAccountNumber() +
//                                    ", amount : " + amount + " , details : " + details;
//
//        Notification notificationToBeSaved = new Notification();
//
//        notificationToBeSaved.setDetails(transactionDetails);
//        notificationToBeSaved.setUser(cachedUser);
//
//        notificationRepository.save(notificationToBeSaved);
//
//        LOGGER.info("Notification saved successfully.");
//        System.out.println("Notification saved successfully.");
//
////        addNotification(cachedUser,transactionDetails);
//
//        System.out.println("Transfer to Account {Account Number: " + accountTo.getAccountNumber() +
//                " New Balance: " + accountTo.getBalance().toString() + " " +
//                accountTo.getAccountType().toString() +"}");
//    }
//
//    /**
//     * This method returns the account selected by the user to transfer FROM.
//     * This account will be used by the {@link #transferAmount(User)} method.
//     * @return the account to transfer FROM
//     * @param cachedUser current logged in user
//     */
//    private Account getAccountFrom(User cachedUser){
//        List<Account> validTransferAccounts = getValidTransferAccounts(cachedUser);
//        Account accountFrom;
//        int opt;
//            if (validTransferAccounts.size() == 1) {
//                accountFrom = validTransferAccounts.get(0);
//            } else {
//                System.out.println("\nList of Accounts to transfer FROM:");
//                DisplayService.AccountsList(validTransferAccounts);
//                do {
//                    System.out.print("Please enter the number of the account you want to transfer from:");
//                    try {
//                        opt = Integer.parseInt(SCANNER.nextLine());
//                        if (opt < 1 || opt > validTransferAccounts.size())
//                            System.out.println("Please enter a number between 1 and " + validTransferAccounts.size());
//                        else {
//                            accountFrom = validTransferAccounts.get(opt - 1);
//                            break;
//                        }
//                    }catch (NumberFormatException e){
//                        System.out.println("Please enter a number.");
//                    }
//                }while(true);
//            }
//
//        System.out.println("From Account: {Account Number: " + accountFrom.getAccountNumber() +
//                " Balance: " + accountFrom.getBalance() + " " +
//                accountFrom.getAccountType().toString() + "}");
//        return accountFrom;
//    }
//
//    /**
//     * @return the list of accounts with balance greater then 0.
//     * @param cachedUser the user for which we get the valid transfer accounts
//     */
//    private List<Account> getValidTransferAccounts(User cachedUser) {
//        List<Account> allAccounts = userRepository.findByUserId(cachedUser.getId());
//
////        List<Account> allAccounts = AccountRepository.readAccountsForUser(cachedUser);
//
//        List<Account> validTransferredAccounts = allAccounts.stream().filter(account ->
//                account.getBalance().compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
//        removeAccountThatDoesNotQualify(cachedUser, validTransferredAccounts);
//        return validTransferredAccounts;
//    }
//
//    /**
//     * This method removes the single valid transfer accounts from the validTransferredAccounts
//     * list. This is needed because it is possible to have only 1 valid account to transfer from
//     * but no accounts to transfer into for one currency and 2 accounts for the other currency.
//     * @param cachedUser the current logged in user
//     * @param validTransferredAccounts the list with all the valid transfer account
//     */
//    private void removeAccountThatDoesNotQualify(User cachedUser, List<Account> validTransferredAccounts){
//        if(getTotalNumberOfAccountsForAccType(cachedUser, AccountType.EUR) < 2)
//            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.EUR));
//        if(getTotalNumberOfAccountsForAccType(cachedUser, AccountType.RON) < 2)
//            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.RON));
//    }
//
//    /**
//     * This method returns the number of accounts with the currency
//     * specified by the parameter account_type
//     *
//     * @param cachedUser the user for which we get nr of accounts of the type specified
//     * @param account_type specifies the currency that we filter by
//     * @return the list of accounts with that currency
//     */
//    public long getTotalNumberOfAccountsForAccType(User cachedUser, AccountType account_type){
//        List<Account> allAccounts = userRepository.findByUserId(cachedUser.getId());
//
////        List<Account> allAccounts = AccountRepository.readAccountsForUser(cachedUser);
//
//        return allAccounts.stream().filter(account -> account.getAccountType() == account_type).count();
//    }
//
//    /**
//     * This method returns the number of accounts that can be used to transfer from
//     * with the currency specified by the parameter account_type
//     *
//     * @param cachedUser current logged in user
//     * @param account_type the curency we filter by
//     * @return number of accounts that satisfy these filters
//     */
//    public long getTotalNumberOfValidTransferAccounts(User cachedUser, AccountType account_type){
//        List<Account> validTransferredAccounts = getValidTransferAccounts(cachedUser);
//        return validTransferredAccounts.stream().filter(account ->
//                account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
//                        account.getAccountType() == account_type).count();
//    }
//
//    /**
//     * This method gets the amount entered from console by the user.
//     * This amount will be used by the {@link #transferAmount(User)} method.
//     * @param accountFrom the account from which the transfer will be made
//     * @return the amount to be transferred
//     */
//    private BigDecimal getAmountToBeTransferred(Account accountFrom){
//        BigDecimal amount;
//
//        do {
//            System.out.print("Insert the amount that will transferred between accounts:");
//            try{
//                amount = new BigDecimal(SCANNER.nextLine());
//                if(amount.compareTo(accountFrom.getBalance()) > 0 || amount.compareTo(new BigDecimal(0)) <= 0)
//                    System.out.println("Please enter an amount greater then 0 and " +
//                            "less then or equal with the current balance:" +
//                            accountFrom.getBalance().toString() + " " +
//                            accountFrom.getAccountType().toString());
//                else
//                    break;
//            }catch(NumberFormatException e){
//                System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
//            }
//        }while (true);
//        return amount;
//    }
//
//    private String getDetails(){
//        String details = "";
//        try{
//            do{
//                System.out.print("Insert details regarding this transaction:");
//                details =  SCANNER.nextLine();
//            }while(details.length() == 0);
//        }catch (InputMismatchException e) {
//            LOGGER.error("The input you entered was not expected.");
//        }
//        return details;
//    }
//
//    /**
//     * This method gets the account chosen by the user to transfer into.
//     * This account will be used by {@link #transferAmount(User)} method.
//     *
//     * @param cachedUser current logged in user
//     * @param accountFrom the account from which we will transfer
//     * @return the account where the transfer will be made
//     */
//    private Account getAccountTo(User cachedUser, Account accountFrom){
//        int opt;
//        Account accountTo;
//
//        List<Account> filteredAccounts = getFilteredAccounts(cachedUser, accountFrom);
//
//        if (filteredAccounts.size() == 1) {
//            accountTo = filteredAccounts.get(0);
//        }
//        else {
//            System.out.println("\nList of accounts to transfer TO:");
//            DisplayService.AccountsList(filteredAccounts);
//            do {
//                System.out.print("Please enter the number of the account you want to transfer to:");
//                try {
//                    opt = Integer.parseInt(SCANNER.nextLine());
//                    if(opt < 1 || opt > filteredAccounts.size())
//                        System.out.println("Please enter a number between 1 and " + filteredAccounts.size());
//                    else {
//                        accountTo = filteredAccounts.get(opt - 1);
//                        break;
//                    }
//                } catch(NumberFormatException e){
//                    System.out.println("Please enter a number.");
//                }
//            }while(true);
//        }
//        return accountTo;
//    }
//
//    /**
//     * This method returns the list of filtered accounts
//     * based on the currency of the account received as parameter
//     * except this account.
//     *
//     * @param cachedUser current logged in user
//     * @param accountFrom the account that we filter by
//     * @return the list of accounts with the same currency excluding it
//     */
//    private List<Account> getFilteredAccounts(User cachedUser, Account accountFrom) {
//        List<Account> allAccounts = userRepository.findByUserId(cachedUser.getId());
////        List<Account> allAccounts = AccountRepository.readAccountsForUser(cachedUser);
//        return allAccounts.stream().filter(account ->
//                account.getAccountType() == accountFrom.getAccountType() &&
//                        !account.getAccountNumber().equals(accountFrom.getAccountNumber())).collect(Collectors.toList());
//    }
//
//    /**
//     * This method updates the balance of the account with the amount entered
//     * from console.
//     * @param cachedUser logged in user
//     */
//    public void depositAmount(User cachedUser){
//        Account accountToDeposit;
//        BigDecimal amount;
//
//        accountToDeposit = getAccountToDeposit(cachedUser);
//
//        amount = getAmountToBeDeposited();
//
//        accountToDeposit.setBalance(accountToDeposit.getBalance().add(amount));
//        accountRepository.save(accountToDeposit);
//
////        updateBalanceAccount(amount,accountToDeposit);
//
//        System.out.println("Updated Account: {Account Number: " + accountToDeposit.getAccountNumber() +
//                " New Balance: " + accountToDeposit.getBalance().toString() + " " +
//                accountToDeposit.getAccountType().toString() +"}");
//    }
//
//    /**
//     * This method gets the account selected by the user to deposit/withdraw from.
//     * This account will be used by {@link #depositAmount(User)} method.
//     * @return the account where the amount will be transferred
//     * @param cachedUser logged in user
//     */
//    private Account getAccountToDeposit(User cachedUser){
//        int opt;
//        loadAccountsForUser(cachedUser);
//        List<Account> allAccounts = cachedUser.getAccountsList();
//        Account accountToDeposit;
//
//        if (allAccounts.size() == 1)
//            accountToDeposit =  allAccounts.get(0);
//        else {
//            System.out.println("\nList of Accounts:");
//            DisplayService.AccountsList(allAccounts);
//            do {
//                System.out.print("Please enter the number of the account you want to deposit in:");
//                try{
//                    opt = Integer.parseInt(SCANNER.nextLine());
//                    if (opt < 1 || opt > allAccounts.size())
//                        System.out.println("Please enter a number between 1 and " + allAccounts.size());
//                    else {
//                        accountToDeposit =  allAccounts.get(opt - 1);
//                        break;
//                    }
//                }catch (NumberFormatException e){
//                    System.out.println("Please enter a number.");
//                }
//            }while (true);
//        }
//        System.out.println("\nAccount To Deposit: {Account Number: " + accountToDeposit.getAccountNumber() +
//                " Balance: " + accountToDeposit.getBalance() + " " +
//                accountToDeposit.getAccountType().toString() + "}");
//        return accountToDeposit;
//    }
//
//    /**
//     * This method gets the amount entered from console by the user.
//     * This amount will be used by the {@link #depositAmount(User)}  method.
//     * @return the amount to be deposited
//     */
//    private BigDecimal getAmountToBeDeposited(){
//        BigDecimal amount = BigDecimal.valueOf(0);
//        do{
//            System.out.print("Insert the amount that will update the current balance:");
//            try{
//                amount = new BigDecimal(SCANNER.nextLine());
//                if(amount.compareTo(new BigDecimal(0)) <= 0)
//                    System.out.println("Please enter a number greater then 0.");
//            }catch (NumberFormatException e){
//                System.out.println("Please enter a number.Use ',' as delimiter for decimals.");
//            }
//        }while(amount.compareTo(new BigDecimal(0)) <= 0);
//        return amount;
//    }

}
