package org.cc.torganizer.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import jakarta.enterprise.inject.Instance;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.cc.torganizer.core.doubleelimination.DoubleEliminationMatchDetector;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.core.roundrobin.RoundRobinMatchDetector;
import org.cc.torganizer.core.singleelimination.SingleEliminationMatchDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PendingMatchDetectorFactoryTest {

  @Mock
  private Instance<PendingMatchDetector> detectors;

  @InjectMocks
  private PendingMatchDetectorFactory factory;

  @BeforeEach
  public void beforeEach() {
    List<PendingMatchDetector> list = Arrays.asList(
        new DoubleEliminationMatchDetector(),
        new SingleEliminationMatchDetector(),
        new RoundRobinMatchDetector()
    );

    Iterator<PendingMatchDetector> iterator = list.iterator();
    when(detectors.iterator()).thenReturn(iterator);
  }

  @Test
  void testGetMatchMakerNull() {
    assertThrows(MissingPendingMatchDetectorException.class, () -> factory.getPendingMatchDetector(null));
  }

  @Test
  void testGetMatchMakerExisting() {
    PendingMatchDetector pmd = factory.getPendingMatchDetector(System.ROUND_ROBIN);
    assertThat(pmd).isNotNull();
  }
}
