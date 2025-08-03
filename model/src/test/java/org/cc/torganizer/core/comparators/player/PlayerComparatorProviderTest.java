package org.cc.torganizer.core.comparators.player;

import jakarta.enterprise.inject.Instance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PlayerComparatorProviderTest {

  @Mock
  private Instance<PlayerComparator> comparators;

  @InjectMocks
  private PlayerComparatorProvider provider;

  @BeforeEach
  void beforeEach() {
    var list = List.of(new PlayerByClubComparator());
    var iterator = list.iterator();
    doReturn(iterator).when(comparators).iterator();
  }

  public static Stream<Arguments> get() {
    return Stream.of(
      Arguments.of(null, false),
      Arguments.of(PlayerOrderCriteria.BY_LAST_UPDATE, false),
      Arguments.of(PlayerOrderCriteria.BY_CLUB, true)
    );
  }

  @ParameterizedTest
  @MethodSource
  void get(PlayerOrderCriteria criteria, boolean expected) {
    var actualComparator = provider.get(criteria);

    assertThat(actualComparator != null).isEqualTo(expected);
  }
}