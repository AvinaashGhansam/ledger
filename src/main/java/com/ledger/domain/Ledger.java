package com.ledger.domain;

import java.util.List;
import java.util.Objects;

public class Ledger {
  public static Money balance(List<Posting> postings, AccountId expected) {
    Objects.requireNonNull(postings, "[postings] cannot be null");
    Objects.requireNonNull(expected, "[accountId] cannot be null");

    if (postings.isEmpty()) {
      throw new IllegalStateException("Cannot evaluate balance for empty posting list");
    }

    Currency currency = postings.getFirst().entries().getFirst().amount().currency();

    return postings.stream()
        .map(posting -> posting.balanceFor(expected))
        .reduce(Money.zero(currency), Money::plus);
  }
}
