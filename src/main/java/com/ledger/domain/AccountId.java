package com.ledger.domain;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {
  public AccountId {
    Objects.requireNonNull(value, "[value] cannot be null");
  }

  public static AccountId of(UUID value) {
    return new AccountId(value);
  }

  public static AccountId generate() {
    return new AccountId(UUID.randomUUID());
  }
}
