package org.cc.torganizer.core.comparators.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.enterprise.inject.Instance;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerComparatorProviderTest {

  @Mock
  private Instance<PlayerComparator> comparators;

  @InjectMocks
  private PlayerComparatorProvider provider;

  @BeforeEach
  public void beforeEach() {
    List<PlayerComparator> list = List.of(
        new PlayerByClubComparator()
    );

    Iterator<PlayerComparator> iterator = list.iterator();
    when(comparators.iterator()).thenReturn(iterator);
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
    PlayerComparator actualComparator = provider.get(criteria);

    assertThat(actualComparator != null).isEqualTo(expected);
  }

}