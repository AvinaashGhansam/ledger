package com.ledger.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CurrencyTest {
  @Test
  void validCode_constructsSuccessfully() {
    // Arrange & Validate
    Currency currency = Currency.of("USD");
    // Assert
    assertThat(currency.code()).isEqualTo("USD");
  }

  @Test
  void lowercaseCode_throwsException() {
    assertThatThrownBy(() -> Currency.of("usd")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void invalidLengthCode_throwsException() {
    assertThatThrownBy(() -> Currency.of("ABCD")).isInstanceOf(IllegalArgumentException.class);
  }
}
