package org.cc.torganizer.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstraintViolationExceptionMapperTest {

  private ConstraintViolationExceptionMapper mapper;

  @BeforeEach
  void beforeEach() {
    mapper = new ConstraintViolationExceptionMapper();
  }

  @Test
  void toJsonObject_noViolation() {
    Set<ConstraintViolation<?>> violations = new HashSet<>();
    JsonObject jsonObject = mapper.toJsonObject(violations);

    String actual = jsonObject.toString();
    String expected = "{\"violations-count\":0,\"violations\":[]}";

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toJsonObject_omeViolation() {
    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(path.toString()).thenReturn("propertyPath");
    when(violation.getMessage()).thenReturn("message");
    when(violation.getPropertyPath()).thenReturn(path);
    when(violation.getInvalidValue()).thenReturn("invalidValue");

    violations.add(violation);

    JsonObject jsonObject = mapper.toJsonObject(violations);

    String actual = jsonObject.toString();
    String expected = "{\"violations-count\":1," +
        "\"violations\":[" +
        "{\"message\":\"message\",\"propertyPath\":\"propertyPath\",\"invalidValue\":\"invalidValue\"}" +
        "]}";

    assertThat(actual).isEqualTo(expected);
  }
}