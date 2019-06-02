package pentastagiu.DTOs;

import pentastagiu.util.AccountType;

import java.math.BigDecimal;

public class AccountDTO {
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;

    public AccountDTO(String accountNumber, BigDecimal balance, AccountType accountType){
        super();
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
