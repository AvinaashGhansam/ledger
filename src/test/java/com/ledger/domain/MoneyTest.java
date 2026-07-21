package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MoneyTest {
  @Test
  void plus_sameCurrency_returnSum() {
    // Arrange
    Money m1 = Money.of(100, Currency.of("USD"));
    Money m2 = Money.of(50, Currency.of("USD"));
    // Act
    Money result = m1.plus(m2);
    // Assert
    assertThat(result.amount()).isEqualTo(150);
    assertThat(result.currency()).isEqualTo(Currency.of("USD"));
  }

  @Test
  void minus_sameCurrency_returnPositiveDifference() {
    // Arrange
    Money m1 = Money.of(100, Currency.of("USD"));
    Money m2 = Money.of(50, Currency.of("USD"));
    // Act
    Money result = m1.minus(m2);
    // Assert
    assertThat(result.amount()).isEqualTo(50);
    assertThat(result.currency()).isEqualTo(Currency.of("USD"));
  }

  @Test
  void minus_sameCurrency_returnNegativeDifference() {
    // Arrange
    Money m1 = Money.of(50, Currency.of("USD"));
    Money m2 = Money.of(100, Currency.of("USD"));
    // Act
    Money result = m1.minus(m2);
    // Assert
    assertThat(result.amount()).isEqualTo(-50);
    assertThat(result.currency()).isEqualTo(Currency.of("USD"));
  }

  @Test
  void minus_sameCurrency_returnZero() {
    // Arrange
    Money m1 = Money.of(100, Currency.of("USD"));
    Money m2 = Money.of(100, Currency.of("USD"));
    // Act
    Money result = m1.minus(m2);
    // Assert
    assertThat(result.amount()).isEqualTo(0);
    assertThat(result.currency()).isEqualTo(Currency.of("USD"));
  }

  @Test
  void plus_differentUnit_throwsException() {
    // Arrange
    Money m1 = Money.of(100, Currency.of("USD"));
    Money m2 = Money.of(50, Currency.of("GYD"));
    // Act & Assert
    assertThatThrownBy(() -> m1.plus(m2)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void negate_fromNegative_returnPositive() {
    // Arrange
    Money money = Money.of(-100, Currency.of("USD"));

    // Act
    Money result = money.negate();
    // Assert
    assertThat(result.amount()).isEqualTo(100);
    assertThat(result.currency()).isEqualTo(Currency.of("USD"));
  }

  @Test
  void zero_amountIsZero_returnTrue() {
    // Arrange
    Money money = Money.of(0, Currency.of("USD"));

    // Act & Assert
    assertThat(money.isZero()).isTrue();
  }

  @Test
  void zero_amountGreaterThanZero_returnFalse() {
    // Arrange
    Money money = Money.of(1, Currency.of("USD"));

    // Act & Assert
    assertThat(money.isZero()).isFalse();
  }

  @Test
  void absoluteValue_fromNegative_returnPositive() {
    // Arrange
    Money m1 = Money.of(-100, Currency.of("USD"));

    Money m2 = m1.abs();
    // Act & Assert
    assertThat(m2.amount()).isEqualTo(100);
  }

  @Test
  void plus_overflow_throwsException() {
    // Arrange
    Money m1 = Money.of(Long.MAX_VALUE, Currency.of("USD"));
    Money m2 = Money.of(1, Currency.of("USD"));

    // Act & Assert
    assertThatThrownBy(() -> m1.plus(m2)).isInstanceOf(ArithmeticException.class);
  }
}
