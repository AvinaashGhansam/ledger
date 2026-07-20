package com.ledger.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class PostingTest {
  private final Currency usd = Currency.of("USD");
  private final Currency eur = Currency.of("EUR");

  private final AccountId checkingId = AccountId.generate();
  private final AccountId savingsId = AccountId.generate();
  private final PostingId postingId = PostingId.generate();
  private final Instant now = Instant.now();

  @Test
  void create_singleEntry_returnsErrTooFewEntries() {
    Entry singleEntry = Entry.of(checkingId, Money.of(100, usd));

    Result<Posting, PostingError> result =
        Posting.create(postingId, now, "Test", List.of(singleEntry));

    assertThat(result).isInstanceOf(Result.Err.class);
    Result.Err<Posting, PostingError> err = (Result.Err<Posting, PostingError>) result;
    assertThat(err.error()).isInstanceOf(PostingError.TooFewEntries.class);
  }

  @Test
  void create_mixedCurrencies_returnsErrCurrencyMismatch() {
    Entry e1 = Entry.of(checkingId, Money.of(100, usd));
    Entry e2 = Entry.of(checkingId, Money.of(100, eur));

    Result<Posting, PostingError> result = Posting.create(postingId, now, "Test", List.of(e1, e2));

    assertThat(result).isInstanceOf(Result.Err.class);
    Result.Err<Posting, PostingError> err = (Result.Err<Posting, PostingError>) result;
    assertThat(err.error()).isInstanceOf(PostingError.CurrencyMismatch.class);
  }

  @Test
  void create_unbalancedEntries_returnsErrUnbalanced() {
    Entry debit = Entry.of(checkingId, Money.of(-100, usd));
    Entry credit = Entry.of(savingsId, Money.of(102, usd));

    Result<Posting, PostingError> result =
        Posting.create(postingId, now, "Test", List.of(debit, credit));

    assertThat(result).isInstanceOf(Result.Err.class);
    Result.Err<Posting, PostingError> err = (Result.Err<Posting, PostingError>) result;
    assertThat(err.error()).isInstanceOf(PostingError.Unbalanced.class);

    PostingError.Unbalanced unbalancedErr = (PostingError.Unbalanced) err.error();
    assertThat(unbalancedErr.residual()).isEqualTo(Money.of(2, usd));
  }

  @Test
  void create_balancedEntries_returnOk() {
    Entry debit = Entry.of(checkingId, Money.of(-100, usd));
    Entry credit = Entry.of(savingsId, Money.of(100, usd));

    Result<Posting, PostingError> result =
        Posting.create(postingId, now, "Transfer to savings", List.of(debit, credit));

    // Assert
    assertThat(result).isInstanceOf(Result.Ok.class);

    Posting posting = ((Result.Ok<Posting, PostingError>) result).value();

    assertThat(posting.balanceFor(checkingId)).isEqualTo(Money.of(-100, usd));
    assertThat(posting.balanceFor(savingsId)).isEqualTo(Money.of(100, usd));

    AccountId randomId = AccountId.generate();
    assertThat(posting.balanceFor(randomId)).isEqualTo(Money.zero(usd));
  }
}
