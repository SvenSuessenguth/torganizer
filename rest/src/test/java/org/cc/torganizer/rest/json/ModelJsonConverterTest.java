/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;

import static java.time.LocalDateTime.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author svens
 */
public class ModelJsonConverterTest {
  
  private ModelJsonConverter<?> converter;
  
  @BeforeEach
  public void before(){
    converter = new ModelJsonConverterImpl<>();
  }
  
  @Test
  public void testLocalDateTimeToString(){
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0,0);
    String christmasEveString = "2017-12-24 18:00:00";
    final String localDateTimeToString = converter.localDateTimeToString(christmasEve);
    
    assertThat(localDateTimeToString, is(christmasEveString));
  }
  
  @Test
  public void testLocalDateToString(){
    LocalDate christmasEve = LocalDate.of(2017, 12, 24);
    String christmasEveString = "2017-12-24";
    final String localDateTimeToString = converter.localDateToString(christmasEve);
    
    assertThat(localDateTimeToString, is(christmasEveString));
  }

  @Test
  public void testGetProper(){
    JsonObject jsonObject = Json
      .createObjectBuilder()
      .add("id", 1)
      .build();

    Entity properModel = converter.getProperEntity(jsonObject, Collections.emptyList());
    assertThat(properModel, is(not(nullValue())));
  }

  private static class ModelJsonConverterImpl<T extends Entity> extends ModelJsonConverter<Entity> {

    public ModelJsonConverterImpl() {
    }

    @Override
    public JsonObject toJsonObject(Entity t) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JsonArray toJsonArray(Collection<Entity> ts) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity toModel(JsonObject jsonObject, Entity entity) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Entity> toModels(JsonArray jsonArray, Collection<Entity> entities) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
