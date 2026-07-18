package com.ledger.domain;

public sealed interface PostingError
    permits PostingError.Unbalanced, PostingError.CurrencyMismatch, PostingError.TooFewEntries {
  record Unbalanced(Money residual) implements PostingError {}

  record CurrencyMismatch(Currency expected, Currency found) implements PostingError {}

  record TooFewEntries(int count) implements PostingError {}
}
