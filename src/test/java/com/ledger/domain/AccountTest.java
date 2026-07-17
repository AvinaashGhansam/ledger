package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountTest {
  @Test
  void equals_sameId_returnsTrue() {
    // Arrange
    UUID id = AccountId.generate().value();
    Account acc1 = new Account(AccountId.of(id), "Liability", Currency.of("USD"));
    Account acc2 = new Account(AccountId.of(id), "Asset", Currency.of("EUR"));
    // Act & Assert
    assertThat(acc1).isEqualTo(acc2);
  }

  @Test
  void equals_differentId_returnFalse() {
    // Arrange
    Account acc1 =
        new Account(AccountId.of(AccountId.generate().value()), "Liability", Currency.of("USD"));
    Account acc2 =
        new Account(AccountId.of(AccountId.generate().value()), "Asset", Currency.of("EUR"));
    // Act & Assert
    assertThat(acc1).isNotEqualTo(acc2);
  }

  @Test
  void equals_nullOrDifferentType_returnsFalse() {
    // Arrange
    Account acc =
        new Account(AccountId.of(AccountId.generate().value()), "Liability", Currency.of("USD"));

    // Act & Assert
    assertThat(acc).isNotEqualTo(null);
    assertThat(acc).isNotEqualTo("I am a string, not an account");
  }
}
