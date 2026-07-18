package com.ledger.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class Posting {
  private final PostingId id;
  private final Instant occurredAt;
  private final String description;
  private final List<Entry> entries;

  private Posting(PostingId id, Instant occurredAt, String description, List<Entry> entries) {
    Objects.requireNonNull(id, "[id] cannot be null");
    Objects.requireNonNull(occurredAt, "[occurredAt] cannot be null");
    Objects.requireNonNull(description, "[description] cannot be null");
    Objects.requireNonNull(entries, "[entries] cannot be null");

    List<Entry> entryCopy = List.copyOf(entries);

    Currency currency = entryCopy.getFirst().amount().currency();

    Money zero = Money.of(0, currency);

    Money sum = entryCopy.stream().map(Entry::amount).reduce(zero, Money::plus);

    if (!sum.isZero()) {
      throw new IllegalStateException(
          "Programmer error: attempted to construct an unbalanced Posting");
    }

    this.id = id;
    this.occurredAt = occurredAt;
    this.description = description;
    this.entries = entryCopy;
  }
}
