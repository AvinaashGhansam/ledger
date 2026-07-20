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

    if (entryCopy.size() < 2) {
      throw new IllegalStateException("Programmer error: posting must have at least two entries");
    }

    Currency expected = entryCopy.getFirst().amount().currency();

    for (Entry entry : entryCopy) {
      if (!entry.amount().currency().equals(expected)) {
        throw new IllegalStateException(
            "Programmer error: attempted to construct a mixed-currency Posting");
      }
    }

    Money zero = Money.zero(expected);
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

  public static Result<Posting, PostingError> create(
      PostingId id, Instant occurredAt, String description, List<Entry> entries) {
    Objects.requireNonNull(id, "[id] cannot be null");
    Objects.requireNonNull(occurredAt, "[occurredAt] cannot be null");
    Objects.requireNonNull(description, "[description] cannot be null");
    Objects.requireNonNull(entries, "[entries] cannot be null");

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

  public PostingId id() {
    return id;
  }

  public Instant occurredAt() {
    return occurredAt;
  }

  public String description() {
    return description;
  }

  public List<Entry> entries() {
    return entries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Posting posting = (Posting) o;
    // Identity Equality: Aggregate Roots are equal if their IDs match.
    return id.equals(posting.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Posting{id=" + id.value() + ", description='" + description + "'}";
  }
}
