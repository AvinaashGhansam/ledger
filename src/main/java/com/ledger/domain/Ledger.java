package com.ledger.domain;

import java.util.List;
import java.util.Objects;

public class Ledger {
  public static Money balance(List<Posting> postings, Account targetAccount) {
    Objects.requireNonNull(postings, "[postings] cannot be null");
    Objects.requireNonNull(targetAccount, "[targetAccount] cannot be null");

    return postings.stream()
        .map(posting -> posting.balanceFor(targetAccount.id()))
        .reduce(Money.zero(targetAccount.currency()), Money::plus);
  }
}
