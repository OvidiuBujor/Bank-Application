package pentastagiu.files;

import pentastagiu.model.ACCOUNT_TYPES;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static pentastagiu.util.AccountService.validateAccount;
import static pentastagiu.util.Constants.*;

/**
 * This class is a helper class that deals with reading/writing to
 * a file and also creating a new file.
 */
public class OperationFile {

    /**
     * This method writes an Account to the Accounts database file
     * @param account the account to be written
     * @param file the file where is written
     * @return true if the account was written to the file
     */
    static boolean writeToFile(File file,Account account){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
            bw.write(account.toString());
            return true;
        } catch (IOException e) {
            LOGGER.error("Unable to write to file.");
        }
        return false;
    }

    /**
     * This method writes a line to a file
     * @param file the file where we write the line
     * @param line the line that we write
     */
    private static void writeToFile(File file,String line){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
            bw.write(line);
        } catch (IOException e) {
            LOGGER.error("Unable to write to file.");
        }
    }

    public static boolean validateUserFromFile(User currentUser, String fileName){
        String line;
        try (InputStream resourceAsStream = OperationFile.class.getClassLoader().getResourceAsStream(fileName)) {
            try (Scanner br = new Scanner(Objects.requireNonNull(resourceAsStream))) {
                while (br.hasNext()) {
                    line = br.nextLine();
                    String[] userDetails = line.split(" ");
                    if (userDetails.length == 2){
                        User userChecked = new User(userDetails[ACCOUNT_NUMBER], userDetails[USERNAME]);
                        if (currentUser.equals(userChecked)){
                            System.out.println("Welcome " + currentUser.getUsername() + "!");
                            return true;
                        }
                    }
                }
            }catch (InputMismatchException e) {
                LOGGER.error("The input you entered was not expected.");
                System.exit(0);
            }
        } catch (IOException e) {
            LOGGER.error("Database file not found. We can't proceed with checking credentials.");
            System.exit(0);
        }
        System.out.println("Wrong username/password.");
        return false;
    }

    /**
     * This metod creates the list of accounts for user
     * @param file the file we read
     * @param currentUser the user for which we create the accounts list
     * @return the account list
     */
    public static List<Account> readAccountsFromFileForUser(File file,User currentUser){
        String line;
        List<Account> accountList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] accountDetails = line.split(" ");
                if(validateAccount(accountDetails)) {
                    if (currentUser.getUsername().equals(accountDetails[USERNAME])) {
                        accountList.add(new Account(accountDetails[ACCOUNT_NUMBER].toUpperCase(),
                                accountDetails[USERNAME],
                                new BigDecimal(String.valueOf(accountDetails[BALANCE])),
                                ACCOUNT_TYPES.fromString(accountDetails[ACCOUNT_TYPE].toUpperCase())));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Accounts database file not found. We can't proceed with loading the accounts.");
        }
        return accountList;
    }

    /**
     * This method reads a number from a file and returns it.
     * @param file the file we read from
     * @return the number
     */
    static long calculateNrAccFromFile(File file){
        String line;
        int lineNumber = 1;
        long nrOffAccounts = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] accountDetails = line.split(" ");
                if(validateAccount(accountDetails)) {
                    nrOffAccounts++;
                }else{
                    LOGGER.warn("Problem at line " + lineNumber +
                            " in 'accounts.txt' files. Account didn't count to the total number of valid accounts.");
                    lineNumber++;
                }
            }
            System.out.println("Number of valid accounts: " + nrOffAccounts);
        } catch (IOException e) {
            LOGGER.error("Accounts database file not found. We can't proceed with loading number of accounts.");
        }
        return nrOffAccounts;
    }

    /**
     * This method creates a new database file for Accounts with the updated balance
     * for the account received as parameter.
     * @param balance the new balance of the account
     * @param account the acccount to be updated
     * @return the new updated file
     */
    static File createNewFile(BigDecimal balance, Account account){
        String line;
        File tempFile = new File("src/main/resources/temp.txt");
        StringBuilder modifiedLine = new StringBuilder();

        if (tempFile.exists())
            tempFile.delete();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_ACCOUNTS))) {
            while ((line = br.readLine()) != null) {
                String[] accountDetails = line.split(" ");

                if (accountDetails[ACCOUNT_NUMBER].equals(account.getAccountNumber().toUpperCase())) {

                    accountDetails[ACCOUNT_NUMBER] = accountDetails[ACCOUNT_NUMBER].toUpperCase();
                    accountDetails[BALANCE] = "" + balance.toString();
                    accountDetails[ACCOUNT_TYPE] = accountDetails[ACCOUNT_TYPE].toUpperCase();

                    modifiedLine.append(accountDetails[ACCOUNT_NUMBER]).append(" ")
                            .append(accountDetails[USERNAME]).append(" ")
                            .append(accountDetails[BALANCE]).append(" ")
                            .append(accountDetails[ACCOUNT_TYPE]);
                    line = modifiedLine.toString();
                }
                writeToFile(tempFile,line + "\n");
            }
        }catch (IOException e) {
            LOGGER.error("Accounts database file not found. We can't proceed with adding the accounts.");
        }
        return tempFile;
    }
}
