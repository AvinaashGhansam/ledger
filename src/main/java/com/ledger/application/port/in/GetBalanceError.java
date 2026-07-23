package com.ledger.application.port.in;

import com.ledger.domain.AccountId;

public sealed interface GetBalanceError permits GetBalanceError.AccountNotFound {
  record AccountNotFound(AccountId accountId) implements GetBalanceError {}
}
