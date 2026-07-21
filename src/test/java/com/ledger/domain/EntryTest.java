package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntryTest {
  @Test
  void of_validAccountAmount_createEntrySuccessfully() {
    // Arrange
    AccountId accountId = AccountId.generate();
    Money amount = Money.of(100, Currency.of("USD"));

    // Act
    Entry entry = Entry.of(accountId, amount);

    // Assert
    assertThat(entry.account()).isEqualTo(accountId);
    assertThat(entry.amount()).isEqualTo(amount);
  }

  @Test
  void of_zeroAmount_throwsException() {
    // Arrange
    AccountId checking = AccountId.generate();
    Money zeroBucks = Money.of(0, Currency.of("USD"));
    // Act & Assert
    assertThatThrownBy(() -> Entry.of(checking, zeroBucks))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void equals_sameAccountAndAmount_returnTrue() {
    // Arrange
    AccountId checking = AccountId.generate();

    Entry e1 = Entry.of(checking, Money.of(50, Currency.of("USD")));
    Entry e2 = Entry.of(checking, Money.of(50, Currency.of("USD")));

    // Act & Assert
    assertThat(e1).isEqualTo(e2);
  }
}
