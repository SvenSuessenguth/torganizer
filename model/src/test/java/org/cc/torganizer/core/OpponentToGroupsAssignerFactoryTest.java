package org.cc.torganizer.core;

import static java.util.Collections.emptyIterator;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import jakarta.enterprise.inject.Instance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpponentToGroupsAssignerFactoryTest {

  @Mock
  private Instance<OpponentToGroupsAssigner> assigners;

  @InjectMocks
  private OpponentToGroupsAssignerFactory factory;

  @Test
  void throwMissingOpponentsToGroupsAssignerException() {
    when(assigners.iterator()).thenReturn(emptyIterator());

    assertThrows(MissingOpponentsToGroupsAssignerException.class, ()
        -> factory.getOpponentToGroupsAssigner(null));
  }
}