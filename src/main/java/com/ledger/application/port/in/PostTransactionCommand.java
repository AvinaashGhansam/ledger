package com.ledger.application.port.in;

import com.ledger.domain.AccountId;
import com.ledger.domain.Money;
import java.util.List;

public record PostTransactionCommand(String description, List<Line> lines) {
  public record Line(AccountId accountId, Money amount) {}
}
