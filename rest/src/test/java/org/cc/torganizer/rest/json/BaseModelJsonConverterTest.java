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
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author svens
 */
class BaseModelJsonConverterTest {
  
  private BaseModelJsonConverter<?> converter;
  
  @BeforeEach
  void before(){
    converter = new ModelJsonConverterImpl<>();
  }
  
  @Test
  void testLocalDateTimeToString(){
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0,0);
    String christmasEveString = "2017-12-24 18:00:00";
    final String localDateTimeToString = converter.localDateTimeToString(christmasEve);
    
    assertThat(localDateTimeToString).isEqualTo(christmasEveString);
  }
  
  @Test
  void testLocalDateToString(){
    LocalDate christmasEve = LocalDate.of(2017, 12, 24);
    String christmasEveString = "2017-12-24";
    final String localDateTimeToString = converter.localDateToString(christmasEve);
    
    assertThat(localDateTimeToString).isEqualTo(christmasEveString);
  }

  @Test
  void testGetProper(){
    JsonObject jsonObject = Json
      .createObjectBuilder()
      .add("id", 1)
      .build();

    Entity properModel = converter.getProperEntity(jsonObject, Collections.emptyList());
    assertThat(properModel).isNotNull();
  }

  private static class ModelJsonConverterImpl<T extends Entity> extends BaseModelJsonConverter<Entity> {

    ModelJsonConverterImpl() {
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
    public T toModel(JsonObject jsonObject, Entity entity) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Entity> toModels(JsonArray jsonArray, Collection<Entity> entities) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
