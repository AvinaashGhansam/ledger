package com.ledger.domain;

import java.util.Objects;

public class Account {
  private final AccountId id;
  private final Currency currency;
  private final String name;

  public Account(AccountId id, String name, Currency currency) {
    Objects.requireNonNull(id, "[id] should not be null");
    Objects.requireNonNull(name, "[name] should not be null");
    Objects.requireNonNull(currency, "[currency] should not be null");

    this.id = id;
    this.currency = currency;
    this.name = name;
  }

  public AccountId id() {
    return id;
  }

  public Currency currency() {
    return currency;
  }

  public String name() {
    return name;
  }

  // An Account is an Entity. Its identity is its AccountId. Two Accounts are the same iff their ids
  // match, regardless of name or currency, because changing a name doesn't change the identity of
  // the account.
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(id, account.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
