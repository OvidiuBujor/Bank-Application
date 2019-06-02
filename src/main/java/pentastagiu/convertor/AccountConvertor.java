package pentastagiu.convertor;

import org.springframework.stereotype.Component;
import pentastagiu.DTOs.AccountDTO;
import pentastagiu.model.Account;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class converts an Account to
 * an AccountDTO that is used for responses of
 * the server
 */
@Component
public class AccountConvertor {
    public Account convertFromAccountDTO(AccountDTO accountDTO) {
        return new Account(accountDTO.getAccountNumber(),accountDTO.getBalance(),accountDTO.getAccountType());
    }

    public AccountDTO convertToAccountDTO(Account account) {
        return new AccountDTO(account.getAccountNumber(),account.getBalance(),account.getAccountType());
    }

    public List<AccountDTO> convertToAccountDTOList(List<Account> accountList){
        return accountList.stream().map(this::convertToAccountDTO).collect(Collectors.toList());
    }
}
