package com.ledger.application.port.in;

import com.ledger.domain.AccountId;
import com.ledger.domain.Money;
import com.ledger.domain.Result;

public interface GetAccountBalanceUseCase {
  Result<Money, GetBalanceError> balance(AccountId id);
}
