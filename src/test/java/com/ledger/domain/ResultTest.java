package com.ledger.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResultTest {
  @Test
  void map_onOK_transformsValue() {
    // Arrange
    Result<Integer, String> result = Result.ok(10);
    // Act
    Result<Integer, String> doubled = result.map(val -> val * 2);
    // Assert
    assertThat(doubled)
        .isInstanceOf(Result.OK.class)
        .extracting(r -> ((Result.OK<?, ?>) r).value())
        .isEqualTo(20);
  }

  @Test
  void map_onErr_doesNotRunFunction() {
    // Arrange
    Result<Integer, String> result = Result.err("FAILED");
    // Act
    Result<Integer, String> mapped =
        result =
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
  void flatMap_returnsFlattenedResult() {
    // Arrange
    Result<Integer, String> result = Result.ok(10);
    // Act
    Result<Integer, String> mapped = result.flatMap(val -> Result.ok(val * 2));
    // Assert
    assertThat(mapped).isInstanceOf(Result.OK.class);
    Result.OK<?, ?> okBox = (Result.OK<?, ?>) mapped;
    assertThat(okBox.value()).isEqualTo(20);
  }
}
