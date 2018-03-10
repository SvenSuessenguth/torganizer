package org.cc.torganizer.rest.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.cc.torganizer.core.entities.Discipline;

/**
 * A json-disciplines contains nothing but label and restrictions.
 * 
 */
@RequestScoped
public class DisciplineJsonConverter extends ModelJsonConverter<Discipline>{

  @Inject  
  private RestrictionJsonConverter restrictionConverter;
  
  public DisciplineJsonConverter(){
  }
  
  public DisciplineJsonConverter(RestrictionJsonConverter restrictionConverter){
    this.restrictionConverter = restrictionConverter;
  }
  
  @Override
  public JsonObject toJsonObject(Discipline discipline) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
    
    add(objectBuilder, "id", discipline.getId());
    // objectBuilder.add("players", playerConverter.toJsonArray(squad.getPlayers()));
      
    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Discipline> disciplines) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
    
    disciplines.forEach(discipline -> arrayBuilder.add(this.toJsonObject(discipline)) );
    
    return arrayBuilder.build();
  }

  @Override
  public Discipline toModel(JsonObject jsonObject) {
    
    Discipline discipline = new Discipline();
    
    String idString = get(jsonObject, "id");
    Long id = idString==null?null:Long.valueOf(idString);
    discipline.setId(id);
    
    return discipline;
  }

  @Override
  public Collection<Discipline> toModels(JsonArray jsonArray) {
    List<Discipline> disciplines = new ArrayList<>();
    
    jsonArray.forEach((JsonValue arrayValue) -> disciplines.add(toModel((JsonObject)arrayValue)));
    
    return disciplines;
  }
}
