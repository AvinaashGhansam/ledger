package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountTest {
  @Test
  void equals_sameIdDifferentCurrency_returnsTrue() {
    // Arrange
    UUID id = AccountId.generate().value();
    Account acc1 = new Account(AccountId.of(id), Currency.of("USD"));
    Account acc2 = new Account(AccountId.of(id), Currency.of("EUR"));
    // Act & Assert
    assertThat(acc1).isEqualTo(acc2);
  }
}
