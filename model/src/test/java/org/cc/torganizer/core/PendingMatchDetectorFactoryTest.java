package org.cc.torganizer.core;

import jakarta.enterprise.inject.Instance;
import org.cc.torganizer.core.doubleelimination.DoubleEliminationMatchDetector;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.core.roundrobin.RoundRobinMatchDetector;
import org.cc.torganizer.core.singleelimination.SingleEliminationMatchDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PendingMatchDetectorFactoryTest {

  @Mock
  private Instance<PendingMatchDetector> detectors;

  @InjectMocks
  private PendingMatchDetectorFactory factory;

  @BeforeEach
  void beforeEach() {
    var list = Arrays.asList(
      new DoubleEliminationMatchDetector(),
      new SingleEliminationMatchDetector(),
      new RoundRobinMatchDetector()
    );

    var iterator = list.iterator();
    when(detectors.iterator()).thenReturn(iterator);
  }

  @Test
  void testGetMatchMakerNull() {
    assertThrows(MissingPendingMatchDetectorException.class, () -> factory.getPendingMatchDetector(null));
  }

  @Test
  void testGetMatchMakerExisting() {
    var pmd = factory.getPendingMatchDetector(System.ROUND_ROBIN);
    assertThat(pmd).isNotNull();
  }
}
