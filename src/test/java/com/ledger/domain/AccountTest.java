package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTest {
  @Test
  void equals_sameId_returnsTrue() {
    // Arrange
    AccountId id = AccountId.generate();
    Account acc1 = new Account(id, "Liability", Currency.of("USD"));
    Account acc2 = new Account(id, "Asset", Currency.of("EUR"));
    // Act & Assert
    assertThat(acc1).isEqualTo(acc2);
  }

  @Test
  void equals_differentId_returnFalse() {
    // Arrange
    Account acc1 = new Account(AccountId.generate(), "Liability", Currency.of("USD"));
    Account acc2 = new Account(AccountId.generate(), "Asset", Currency.of("EUR"));
    // Act & Assert
    assertThat(acc1).isNotEqualTo(acc2);
  }

  @Test
  void equals_nullOrDifferentType_returnsFalse() {
    // Arrange
    Account acc = new Account(AccountId.generate(), "Liability", Currency.of("USD"));

    // Act & Assert
    assertThat(acc).isNotEqualTo(null);
    assertThat(acc).isNotEqualTo("I am a string, not an account");
  }

  @Test
  void equals_twoObjects_returnsSameHashCode() {
    // Assert
    AccountId id = AccountId.generate();
    Account acc1 = new Account(id, "Liability", Currency.of("USD"));
    Account acc2 = new Account(id, "Liability", Currency.of("USD"));

    // Act & Assert
    assertThat(acc1.hashCode()).isEqualTo(acc2.hashCode());
  }
}
