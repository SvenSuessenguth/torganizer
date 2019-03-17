package org.cc.torganizer.core;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.enterprise.inject.Instance;

import org.cc.torganizer.core.PendingMatchDetector;
import org.cc.torganizer.core.PendingMatchDetectorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PendingMatchDetectorFactoryTest {

  @Mock
  private Instance<PendingMatchDetector> detectors;

  @InjectMocks
  private PendingMatchDetectorFactory factory;

  @BeforeEach
  public void before() {
  }

  @AfterEach
  public void after() {
    factory = null;
  }


  @Test
  public void testGetMatchMakerNull() {
    // Instances ist noch nicht instanziiert
    assertThrows(NullPointerException.class, ()-> {
      factory.getPendingMatchDetector(null);
    });
  }
}
