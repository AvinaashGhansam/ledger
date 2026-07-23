package com.ledger.application.port.in;

import com.ledger.domain.AccountId;
import com.ledger.domain.Currency;
import com.ledger.domain.PostingError;

public sealed interface PostTransactionError
    permits PostTransactionError.UnknownAccount,
        PostTransactionError.AccountCurrencyMismatch,
        PostTransactionError.InvalidPosting {
  record UnknownAccount(AccountId id) implements PostTransactionError {}

  record AccountCurrencyMismatch(AccountId id, Currency expected, Currency actual)
      implements PostTransactionError {}

  record InvalidPosting(PostingError error) implements PostTransactionError {}
}
