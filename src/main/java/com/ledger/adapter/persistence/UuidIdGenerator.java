package com.ledger.adapter.persistence;

import com.ledger.application.port.out.IdGenerator;
import com.ledger.domain.AccountId;
import com.ledger.domain.PostingId;
import org.springframework.stereotype.Component;

@Component
public class UuidIdGenerator implements IdGenerator {
  @Override
  public AccountId nextAccountId() {
    return AccountId.generate();
  }

  @Override
  public PostingId nextPostingId() {
    return PostingId.generate();
  }
}
