package org.cc.torganizer.core.comparators;

import lombok.Value;
import org.cc.torganizer.core.entities.Positional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PositionalComparatorTest {

  private PositionalComparator comparator;

  @BeforeEach
  void beforeEach() {
    comparator = new PositionalComparator();
  }

  public static Stream<Arguments> testCompareArguments() {
    return Stream.of(
      Arguments.of(null, null, 0),
      Arguments.of(new TestPositional(1), null, -1),
      Arguments.of(null, new TestPositional(1), 1),
      Arguments.of(new TestPositional(1), new TestPositional(1), 0),
      Arguments.of(new TestPositional(1), new TestPositional(2), -1),
      Arguments.of(new TestPositional(2), new TestPositional(1), 1)
    );
  }

  @ParameterizedTest
  @MethodSource("testCompareArguments")
  void testCompare(Positional p0, Positional p1, int expected) {
    var actual = comparator.compare(p0, p1);

    assertThat(actual).isEqualTo(expected);
  }
}

@Value
class TestPositional implements Positional {
  Integer position;
}