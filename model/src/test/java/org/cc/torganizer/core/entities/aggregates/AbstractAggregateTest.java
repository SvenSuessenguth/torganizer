package org.cc.torganizer.core.entities.aggregates;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Result;
import org.junit.Before;
import org.junit.Test;

public class AbstractAggregateTest {
  
  private AbstractAggregate aa;

  @Before
  public void before() throws Exception {
    aa = new ResultAggregate();
  }

  @Test
  public void testGetRatio_keinenGewonnen() throws Exception {
    // keinen geownnen, einen verloren
    Match m = new Match();
    m.addResult(new Result(0, 1, 2));
    aa.aggregate(m, null);
    
    double ratio = aa.getRatio();
    
    assertThat(ratio, is(0.0));
  }
  
  @Test
  public void testGetRatio_keinresult() throws Exception {
    // kein Result
    Match m = new Match();
    aa.aggregate(m, null);
    double ratio = aa.getRatio();
    
    assertThat(ratio, is(0.0));
  }
  
  @Test
  public void testGetRatio_doppeltSoVieleVerlorenWieGewonnen() throws Exception {
    // 1 von 3 gewonnen
    Match m = new Match();
    m.addResult(new Result(0, 0, 1));
    m.addResult(new Result(1, 0, 1));
    m.addResult(new Result(2, 1, 0));
    aa.aggregate(m, null);
    double ratio = aa.getRatio();
    
    assertThat(ratio, is(1/3.0));
  }

}
