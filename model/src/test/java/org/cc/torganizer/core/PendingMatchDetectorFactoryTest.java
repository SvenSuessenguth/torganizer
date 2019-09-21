package org.cc.torganizer.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PendingMatchDetectorFactoryTest {

  @InjectMocks
  private PendingMatchDetectorFactory factory;

  @BeforeEach
  void before() {
  }

  @AfterEach
  void after() {
    factory = null;
  }


  @Test
  void testGetMatchMakerNull() {
    // Instances ist noch nicht instanziiert
    assertThrows(NullPointerException.class, ()-> factory.getPendingMatchDetector(null));
  }
}
