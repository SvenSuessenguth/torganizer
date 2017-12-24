/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.of;
import java.time.Month;
import java.util.Collection;
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class ModelJsonConverterTest {
  
  private ModelJsonConverter converter;
  
  @Before
  public void before(){
    converter = new ModelJsonConverterImpl();
  }
  
  @Test
  public void testLocalDateTimeToString(){
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0,0);
    String christmasEveString = "2017-12-24 18:00:00";
    final String localDateTimeToString = converter.localDateTimeToString(christmasEve);
    
    assertThat(converter.localDateTimeToString(christmasEve), is(christmasEveString));
  }
  
  @Test
  public void testLocalDateToString(){
    LocalDate christmasEve = LocalDate.of(2017, 12, 24);
    String christmasEveString = "2017-12-24";
    final String localDateTimeToString = converter.localDateToString(christmasEve);
    
    assertThat(converter.localDateToString(christmasEve), is(christmasEveString));
  }

  private static class ModelJsonConverterImpl extends ModelJsonConverter {

    public ModelJsonConverterImpl() {
    }

    @Override
    public JsonObject toJsonObject(Object t) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JsonArray toJsonArray(Collection ts) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object toModel(JsonObject jsonObject) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection toModels(JsonArray jsonArray) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
