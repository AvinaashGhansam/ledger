package com.ledger.application.port.in;

import com.ledger.domain.Posting;
import com.ledger.domain.Result;

public interface PostTransactionUseCase {
  Result<Posting, PostTransactionError> post(PostTransactionCommand command);
}
