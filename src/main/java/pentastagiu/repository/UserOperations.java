package pentastagiu.repository;

import pentastagiu.model.AccountType;
import pentastagiu.model.Account;
import pentastagiu.services.UserCacheService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles all the operations for the user.
 */
public class UserOperations {

    private UserCacheService userCacheService;

    public UserOperations(UserCacheService userCacheService){
        this.userCacheService = userCacheService;
    }

    /**
     * This method returns the list of filtered accounts
     * based on the currency of the account received as parameter
     * except this account.
     * @param accountFrom the account that we filter by
     * @return the list of accounts with the same currency excluding it
     */
    public List<Account> getFilteredAccounts(Account accountFrom) {
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(userCacheService.getCurrentUser());
        return allAccounts.stream().filter(account ->
                account.getAccountType() == accountFrom.getAccountType() &&
                !account.getAccountNumber().equals(accountFrom.getAccountNumber())).collect(Collectors.toList());
    }

    /**
     * @return the list of accounts with balance greater then 0.
     */
    public List<Account> getValidTransferAccounts() {
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(userCacheService.getCurrentUser());
        List<Account> validTransferredAccounts = allAccounts.stream().filter(account ->
                account.getBalance().compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
        removeAccountThatDoesNotQualify(validTransferredAccounts);
        return validTransferredAccounts;
    }

    /**
     * This method removes the single valid transfer accounts from the validTransferredAccounts
     * list. This is needed because it is possible to have only 1 valid account to transfer from
     * but no accounts to transfer into for one currency and 2 accounts for the other currency.
     * @param validTransferredAccounts the list with all the valid transfer account
     */
    private void removeAccountThatDoesNotQualify(List<Account> validTransferredAccounts){
        if(getTotalNumberOfAccountsForAccType(AccountType.EUR) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.EUR));
        if(getTotalNumberOfAccountsForAccType(AccountType.RON) < 2)
            validTransferredAccounts.removeIf(account -> account.getAccountType().equals(AccountType.RON));
    }

    /**
     * This method returns the number of accounts with the currency
     * specified by the parameter account_type
     * @param account_type specifies the currency that we filter by
     * @return the list of accounts with that currency
     */
    private long getTotalNumberOfAccountsForAccType(AccountType account_type){
        List<Account> allAccounts = DatabaseOperations.readAccountsForUser(userCacheService.getCurrentUser());
        return allAccounts.stream().filter(account -> account.getAccountType() == account_type).count();
    }

    /**
     * This method returns the number of accounts that can be used to transfer from
     * with the currency specified by the parameter account_type
     * @param account_type the curency we filter by
     * @return number of accounts that satisfy these filters
     */
    private long getTotalNumberOfValidTransferAccounts(AccountType account_type){
        List<Account> validTransferredAccounts = getValidTransferAccounts();
        return validTransferredAccounts.stream().filter(account ->
                account.getBalance().compareTo(new BigDecimal(0)) > 0 &&
                        account.getAccountType() == account_type).count();
    }

    /**
     * This method checks to see if the user is able to transfer between his accounts.
     * @return true if user is able to transfer; false otherwise
     */
    public boolean isUserAbleToTransfer(){
        return (getTotalNumberOfAccountsForAccType(AccountType.EUR) > 1 &&
                getTotalNumberOfValidTransferAccounts(AccountType.EUR) > 0) ||
                (getTotalNumberOfAccountsForAccType(AccountType.RON) > 1 &&
                        getTotalNumberOfValidTransferAccounts(AccountType.RON) > 0);
    }
}
