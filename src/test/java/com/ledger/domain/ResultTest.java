package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResultTest {

  @Test
  void map_onOk_transformsValue() {
    // Arrange
    Result<Integer, String> result = Result.ok(10);
    // Act
    Result<Integer, String> doubled = result.map(val -> val * 2);
    // Assert
    assertThat(doubled)
        .isInstanceOf(Result.Ok.class)
        .extracting(r -> ((Result.Ok<?, ?>) r).value())
        .isEqualTo(20);
  }

  @Test
  void map_onErr_doesNotRunFunction() {
    // Arrange
    Result<Integer, String> result = Result.err("FAILED");
    // Act
    Result<Integer, String> mapped =
        result.map(
            val -> {
              throw new RuntimeException("This should not run");
            });
    // Assert
    assertThat(mapped).isInstanceOf(Result.Err.class);
    Result.Err<?, ?> errBox = (Result.Err<?, ?>) mapped;
    assertThat(errBox.error()).isEqualTo("FAILED");
  }

  @Test
  void flatMap_onOk_returnsFlattenedResult() {
    // Arrange
    Result<Integer, String> result = Result.ok(10);
    // Act
    Result<Integer, String> mapped = result.flatMap(val -> Result.ok(val * 2));
    // Assert
    assertThat(mapped).isInstanceOf(Result.Ok.class);
    Result.Ok<?, ?> okBox = (Result.Ok<?, ?>) mapped;
    assertThat(okBox.value()).isEqualTo(20);
  }

  @Test
  void flatMap_onErr_shortCircuitsChain() {
    // Arrange
    Result<Integer, String> start = Result.err("boom");

    // Act
    Result<Integer, String> chained =
        start
            .flatMap(
                v -> {
                  throw new RuntimeException("First flatMap should NOT run");
                })
            .flatMap(
                v -> {
                  throw new RuntimeException("Second flatMap should NOT run");
                });

    // Assert
    assertThat(chained).isInstanceOf(Result.Err.class);
    Result.Err<?, ?> errBox = (Result.Err<?, ?>) chained;
    assertThat(errBox.error()).isEqualTo("boom");
  }
}
