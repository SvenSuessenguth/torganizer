package org.cc.torganizer.rest.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class AbstractJsonConverterTest implements AbstractJsonAdapter {

  private AbstractJsonAdapter adapter;

  @Before
  public void before() {
    adapter = new AbstractJsonAdapter() {
    };
  }

  @Test
    public void testLocalDateToString_validLocalDate() throws Exception {
      LocalDate localDate = LocalDate.of(2017, 1, 12);
      String expected = "2017-01-12";
  
      assertThat(expected, is(adapter.localDateToString(localDate)));
    }

  @Test
    public void testLocalDateToString_null() throws Exception {
      LocalDate localDate = null;
      String expected = "";
  
      assertThat(expected, is(adapter.localDateToString(localDate)));
    }

}
