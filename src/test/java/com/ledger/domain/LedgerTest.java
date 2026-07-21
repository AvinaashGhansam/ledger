package com.ledger.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class LedgerTest {
  private final Currency usd = Currency.of("USD");
  private final AccountId checkingId = AccountId.generate();
  private final AccountId savingsId = AccountId.generate();
  private final Instant now = Instant.now();

  @Test
  void moveMoneyBetweenAccount_checkingAndSavings_verifyNoResidual() {
    // Transaction 1
    Entry t1Debit = Entry.of(checkingId, Money.of(-500, usd));
    Entry t1Credit = Entry.of(savingsId, Money.of(500, usd));

    Posting p1 =
        ((Result.Ok<Posting, PostingError>)
                Posting.create(
                    PostingId.generate(), now, "First Transfer", List.of(t1Debit, t1Credit)))
            .value();

    // Transaction 2
    Entry t2Debit = Entry.of(checkingId, Money.of(-200, usd));
    Entry t2Credit = Entry.of(savingsId, Money.of(200, usd));

    Posting p2 =
        ((Result.Ok<Posting, PostingError>)
                Posting.create(
                    PostingId.generate(), now, "Second Transfer", List.of(t2Debit, t2Credit)))
            .value();

    // Ledger history
    List<Posting> legerHistory = List.of(p1, p2);

    // Act
    Money checkingBalance = Ledger.balance(legerHistory, checkingId);
    Money savingsBalance = Ledger.balance(legerHistory, savingsId);

    // Assert
    assertThat(checkingBalance).isEqualTo(Money.of(-700, usd));
    assertThat(savingsBalance).isEqualTo(Money.of(700, usd));

    assertThat(checkingBalance.plus(savingsBalance)).isEqualTo(Money.zero(usd));
  }
}
