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
}
