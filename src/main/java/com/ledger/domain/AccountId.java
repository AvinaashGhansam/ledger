package com.ledger.domain;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) implements LedgerId {
  public AccountId {
    Objects.requireNonNull(value, "[value] cannot be null");
  }

  public static AccountId generate() {
    return new AccountId(UUID.randomUUID());
  }
}
