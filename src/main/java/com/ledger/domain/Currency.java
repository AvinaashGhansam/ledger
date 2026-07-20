package com.ledger.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Currency(String code) {
  private static final Pattern VALID_CODE = Pattern.compile("[A-Z]{3}");

  public Currency {
    Objects.requireNonNull(code, "[code] cannot be null");

    if (!VALID_CODE.matcher(code).matches()) {
      throw new IllegalArgumentException("Currency code must be exactly three uppercase letters");
    }
  }

  public static Currency of(String currencyCode) {
    return new Currency(currencyCode);
  }
}
