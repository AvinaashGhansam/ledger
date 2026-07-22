package com.ledger.application.port.out;

import com.ledger.domain.Account;
import com.ledger.domain.AccountId;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
  void save(Account account);

  Optional<Account> findById(AccountId id);

  List<Account> findAll();
}
