package com.ledger.application.service;

import com.ledger.application.port.in.OpenAccountCommand;
import com.ledger.application.port.in.OpenAccountUseCase;
import com.ledger.application.port.out.AccountRepository;
import com.ledger.application.port.out.IdGenerator;
import com.ledger.domain.Account;
import com.ledger.domain.AccountId;
import org.springframework.stereotype.Service;

@Service
public class OpenAccountService implements OpenAccountUseCase {
  private final IdGenerator idGenerator;
  private final AccountRepository accountRepository;

  public OpenAccountService(IdGenerator idGenerator, AccountRepository accountRepository) {
    this.idGenerator = idGenerator;
    this.accountRepository = accountRepository;
  }

  @Override
  public Account open(OpenAccountCommand command) {
    AccountId id = this.idGenerator.nextAccountId();

    Account newAccount = new Account(id, command.name(), command.currency());

    accountRepository.save(newAccount);
    return newAccount;
  }
}
