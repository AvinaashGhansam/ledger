package com.ledger.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

  public Money {
    Objects.requireNonNull(amount, "[amount] cannot be a null value");
    Objects.requireNonNull(currency, "[currency] cannot be a null value");
  }

  public Money add(Money other) {
    assertSameCurrency(other);
    return new Money(this.amount.add(other.amount), this.currency);
  }

  public Money subtract(Money other) {
    assertSameCurrency(other);
    return new Money(this.amount.subtract(other.amount), this.currency);
  }

  private void assertSameCurrency(Money other) {
    if (!this.currency().equals(other.currency)) {
      throw new IllegalArgumentException(
          "Cannot add two values of different currencies. Value of "
              + this.currency
              + "!="
              + other.currency);
    }
  }
}
