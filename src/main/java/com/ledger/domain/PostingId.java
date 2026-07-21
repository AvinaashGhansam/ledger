package com.ledger.domain;

import java.util.Objects;
import java.util.UUID;

public record PostingId(UUID value) {
  public PostingId {
    Objects.requireNonNull(value, "[value] cannot be null");
  }

  public static PostingId of(UUID value) {
    return new PostingId(value);
  }

  public static PostingId generate() {
    return new PostingId(UUID.randomUUID());
  }
}
