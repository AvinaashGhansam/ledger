package com.ledger.domain;

import java.util.function.Function;

public sealed interface Result<T, E> permits Result.OK, Result.Err {
  record OK<T, E>(T value) implements Result<T, E> {}

  record Err<T, E>(E error) implements Result<T, E> {}

  static <T, E> Result<T, E> ok(T value) {
    return new OK<>(value);
  }

  static <T, E> Result<T, E> err(E error) {
    return new Err<>(error);
  }

  default <U> Result<U, E> map(Function<? super T, ? extends U> f) {
    return switch (this) {
      case OK<T, E> ok -> ok(f.apply(ok.value()));
      case Err<T, E> err -> new Err<>(err.error());
    };
  }

  default <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> f) {
    return switch (this) {
      case OK<T, E> ok -> f.apply(ok.value());
      case Err<T, E> err -> new Err<>(err.error());
    };
  }
}
