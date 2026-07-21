package com.ledger.domain;

import java.util.Objects;

public record Entry(AccountId account, Money amount) {
  public Entry {
    Objects.requireNonNull(account, "[account] cannot be null");
    Objects.requireNonNull(amount, "[amount] cannot be null");

    if (amount.isZero()) {
      throw new IllegalArgumentException("Amount cannot be zero");
    }
  }

  public static Entry of(AccountId account, Money amount) {
    return new Entry(account, amount);
  }
}
