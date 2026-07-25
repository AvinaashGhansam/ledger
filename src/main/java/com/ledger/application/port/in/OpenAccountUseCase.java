package com.ledger.application.port.in;

import com.ledger.domain.Account;

public interface OpenAccountUseCase {
  Account open(OpenAccountCommand command);
}
