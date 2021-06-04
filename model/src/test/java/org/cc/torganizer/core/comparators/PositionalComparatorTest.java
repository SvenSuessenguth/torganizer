package org.cc.torganizer.core.comparators;

import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Positional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PositionalComparatorTest {

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

  }
}

class TestPositional implements Positional {
  Integer position;

  public TestPositional(Integer position) {
    this.position = position;
  }

  @Override
  public Integer getPosition() {
    return position;
  }
}