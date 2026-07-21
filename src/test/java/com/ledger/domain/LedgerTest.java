package com.ledger.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LedgerTest {
  private final Currency usd = Currency.of("USD");
  private final Account checkingAccount = new Account(AccountId.generate(), "Checking", usd);
  private final Account savingsAccount = new Account(AccountId.generate(), "Savings", usd);
  private final Instant now = Instant.now();

  @Test
  void moveMoneyBetweenAccount_checkingAndSavings_verifyNoResidual() {
    // Transaction 1
    Entry t1Debit = Entry.of(checkingAccount.id(), Money.of(-500, usd));
    Entry t1Credit = Entry.of(savingsAccount.id(), Money.of(500, usd));

    Posting p1 =
        ((Result.Ok<Posting, PostingError>)
                Posting.create(
                    PostingId.generate(), now, "First Transfer", List.of(t1Debit, t1Credit)))
            .value();

    // Transaction 2
    Entry t2Debit = Entry.of(checkingAccount.id(), Money.of(-200, usd));
    Entry t2Credit = Entry.of(savingsAccount.id(), Money.of(200, usd));

    Posting p2 =
        ((Result.Ok<Posting, PostingError>)
                Posting.create(
                    PostingId.generate(), now, "Second Transfer", List.of(t2Debit, t2Credit)))
            .value();

    // Ledger history
    List<Posting> ledgerHistory = List.of(p1, p2);

    Set<Account> activeAccounts = Set.of(checkingAccount, savingsAccount);

    Money netSystemBalance =
        activeAccounts.stream()
            .map(account -> Ledger.balance(ledgerHistory, account))
            .reduce(Money::plus)
            .orElseThrow();

    // Assert: The mathematical law of double-entry holds true!
    assertThat(netSystemBalance).isEqualTo(Money.zero(usd));
  }
}
