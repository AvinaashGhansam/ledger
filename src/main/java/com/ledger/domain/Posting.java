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

    Currency expected = entryCopy.getFirst().amount().currency();

    Money zero = Money.zero(expected);

    Money sum = entries.stream().map(Entry::amount).reduce(zero, Money::plus);

    if (!sum.isZero()) {
      throw new IllegalStateException(
          "Programmer error: attempted to construct an unbalanced Posting");
    }

    this.id = id;
    this.occurredAt = occurredAt;
    this.description = description;
    this.entries = entryCopy;
  }

  public static Result<Posting, PostingError> create(
      PostingId id, Instant occurredAt, String description, List<Entry> entries) {
    if (entries.size() < 2) {
      return Result.err(new PostingError.TooFewEntries(entries.size()));
    }

    Currency expected = entries.getFirst().amount().currency();
    for (Entry entry : entries) {
      if (!entry.amount().currency().equals(expected)) {
        return Result.err(new PostingError.CurrencyMismatch(expected, entry.amount().currency()));
      }
    }

    Money zero = Money.zero(expected);

    Money sum = entries.stream().map(Entry::amount).reduce(zero, Money::plus);

    if (!sum.isZero()) {
      return Result.err(new PostingError.Unbalanced(sum));
    }

    return Result.ok(new Posting(id, occurredAt, description, entries));
  }

  public Money balanceFor(AccountId requestedAccount) {
    Currency expected = this.entries.getFirst().amount().currency();
    Money zero = Money.zero(expected);

    return this.entries.stream()
        .filter((acc) -> acc.account().equals(requestedAccount))
        .map(Entry::amount)
        .reduce(zero, Money::plus);
  }
}
