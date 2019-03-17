package org.cc.torganizer.core.entities.aggregates;

import static org.assertj.core.api.Assertions.assertThat;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractAggregateTest {
  
  private AbstractAggregate aa;

  @BeforeEach
  void before() {
    aa = new ResultAggregate();
  }

  @Test
  void testGetRatio_keinenGewonnen() {
    // keinen geownnen, einen verloren
    Match m = new Match();
    m.addResult(new Result(0, 1, 2));
    aa.aggregate(m, null);
    
    double ratio = aa.getRatio();
    
    assertThat(ratio).isEqualTo(0.0);
  }
  
  @Test
  void testGetRatio_keinresult() {
    // kein Result
    Match m = new Match();
    aa.aggregate(m, null);
    double ratio = aa.getRatio();
    
    assertThat(ratio).isEqualTo(0.0);
  }
  
  @Test
  void testGetRatio_doppeltSoVieleVerlorenWieGewonnen() {
    // 1 von 3 gewonnen
    Match m = new Match();
    m.addResult(new Result(0, 0, 1));
    m.addResult(new Result(1, 0, 1));
    m.addResult(new Result(2, 1, 0));
    aa.aggregate(m, null);
    double ratio = aa.getRatio();
    
    assertThat(ratio).isEqualTo(1/3.0);
  }
}
