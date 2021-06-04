package org.cc.torganizer.core.comparators;

import static org.cc.torganizer.core.comparators.SignAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.assertj.core.api.AbstractAssert;
import org.cc.torganizer.core.entities.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MatchIdleTimeComparatorTest {

  private MatchIdleTimeComparator comparator;

  @BeforeEach
  public void beforeEach() {
    comparator = new MatchIdleTimeComparator();
  }

  public static Stream<Arguments> testCompareArguments() {
    return Stream.of(
        Arguments.of(null, null, 0),
        Arguments.of(match(1L), null, -1),
        Arguments.of(null, match(1L), 1),
        Arguments.of(match(1L), match(1L), 0),
        Arguments.of(match(1L), match(2L), 1),
        Arguments.of(match(2L), match(1L), -1)
    );
  }

  @ParameterizedTest
  @MethodSource("testCompareArguments")
  void testCompare(Match m1, Match m2, int expected) {
    int compare = comparator.compare(m1, m2);

    assertThat(compare).hasSameSign(expected);
  }

  private static final Match match(Long idleTime) {
    Match match = mock(Match.class);
    when(match.getIdleTime()).thenReturn(idleTime);

    return match;
  }

}

class SignAssert extends AbstractAssert<SignAssert, Number> {
  public SignAssert(Number actual) {
    super(actual, SignAssert.class);
  }

  public static SignAssert assertThat(Number actual) {
    return new SignAssert(actual);
  }

  public SignAssert hasSameSign(Number other) {
    isNotNull();

    if (actual.doubleValue() == 0.0 && other.doubleValue() == 0.0) {
      return this;
    }

    if (actual.doubleValue() * other.doubleValue() < 0) {
      String actualSign = actual.doubleValue() > 0 ? "'+'" : "'-";
      String expectedSign = other.doubleValue() > 0 ? "'+'" : "'-'";
      failWithMessage("Expected sign is <%s> but was <%s>", expectedSign, actualSign);
    }

    return this;
  }
}