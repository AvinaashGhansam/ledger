package com.ledger.adapter.persistence;

import com.ledger.application.port.out.AccountRepository;
import com.ledger.domain.Account;
import com.ledger.domain.AccountId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
  private final Map<AccountId, Account> store = new ConcurrentHashMap<>();

  @Override
  public void save(Account account) {
    store.put(account.id(), account);
  }

  @Override
  public Optional<Account> findById(AccountId id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Account> findAll() {
    return List.copyOf(store.values());
  }
}
