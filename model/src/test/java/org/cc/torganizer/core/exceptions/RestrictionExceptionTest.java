package org.cc.torganizer.core.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RestrictionExceptionTest {
  @Test
  void testMessage() {
    var message = "message";
    RestrictionException exception = new RestrictionException(message);

    assertThat(exception.getMessage()).isEqualTo(message);
  }
}