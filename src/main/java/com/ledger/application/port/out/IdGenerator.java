package com.ledger.application.port.out;

import com.ledger.domain.AccountId;
import com.ledger.domain.PostingId;

public interface IdGenerator {
  AccountId nextAccountId();

  PostingId nextPostingId();
}
