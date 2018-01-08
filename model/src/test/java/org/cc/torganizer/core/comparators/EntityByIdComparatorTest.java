package org.cc.torganizer.core.comparators;

import org.cc.torganizer.core.entities.Entity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import org.junit.Before;
import org.junit.Test;

/**
 * @author svens
 */
public class EntityByIdComparatorTest {

  private EntityByIdComparator comparator;
  
  @Before
  public void before(){
    comparator = new EntityByIdComparator();
  }
  
  @Test
  public void testCompare_bothNull() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    
    int i = comparator.compare(e1, e2);
    
    assertThat(i, is(0));
  }
  
  @Test
  public void testCompare_e1IsNull() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    e2.setId(Long.MIN_VALUE);
    
    int i = comparator.compare(e1, e2);
    
    assertThat(i, is(lessThan(0)));
  }
  
  @Test
  public void testCompare_e2IsNull() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    e1.setId(Long.MIN_VALUE);
    
    int i = comparator.compare(e1, e2);
    
    assertThat(i, is(greaterThan(0)));
  }
  
  @Test
  public void testCompare_lessThan() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    e1.setId(Long.MIN_VALUE);
    e2.setId(Long.MAX_VALUE);
    
    int i = comparator.compare(e1, e2);
    
    assertThat(i, is(lessThan(0)));
  }
  
  @Test
  public void testCompare_greaterThan() {
    Entity e1 = new Entity();
    Entity e2 = new Entity();
    e1.setId(Long.MAX_VALUE);
    e2.setId(Long.MIN_VALUE);
    
    int i = comparator.compare(e1, e2);
    
    assertThat(i, is(greaterThan(0)));
  }
  
}
