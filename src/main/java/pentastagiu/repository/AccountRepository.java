package pentastagiu.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pentastagiu.model.Account;
import pentastagiu.model.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class is a helper class that operates on the
 * database file of accounts.It can perform different operations:
 * adding an account to database, update the balance of an account.
 */

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

    List<Account> getAccountByUser(User user);

   @Modifying(clearAutomatically = true)
   @Transactional
   @Query(value = "update Account a set a.balance = :balance where a.id = :id", nativeQuery  = true)
   void updateAccount(@Param("id") Long id, @Param("balance")BigDecimal balance);
}
