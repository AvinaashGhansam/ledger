package com.ledger.domain;

import java.util.Objects;

public record Currency(String code) {
  public Currency {
    Objects.requireNonNull(code, "[code] cannot be null");

    if (!code.matches("^[A-Z]{3}$")) {
      throw new IllegalArgumentException(
          "Code must be all uppercase letter and exactly three letter value");
    }
  }

  public static Currency of(String currencyCode) {
    return new Currency(currencyCode);
  }
}
