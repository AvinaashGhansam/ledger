package com.ledger.domain;

import java.util.Objects;

public record Money(long amount, Currency currency) {

  public Money {
    Objects.requireNonNull(currency, "[currency] cannot be a null value");
  }

  public Money plus(Money other) {
    assertSameCurrency(other);
    return new Money(Math.addExact(this.amount(), other.amount()), this.currency());
  }

  public Money minus(Money other) {
    assertSameCurrency(other);
    return new Money(Math.subtractExact(this.amount(), other.amount()), this.currency());
  }

  public Money negate() {
    return new Money(Math.negateExact(this.amount()), this.currency());
  }

  public boolean isZero() {
    return this.amount == 0;
  }

  public Money abs() {
    return new Money(Math.absExact(this.amount()), this.currency());
  }

  public static Money of(long amount, Currency currency) {
    return new Money(amount, currency);
  }

  public static Money zero(Currency currency) {
    return Money.of(0, currency);
  }

  private void assertSameCurrency(Money other) {
    if (!this.currency().equals(other.currency())) {
      throw new IllegalArgumentException(
          "Currency mismatch. Cannot perform arithmetic on "
              + this.currency()
              + " and "
              + other.currency());
    }
  }
}
