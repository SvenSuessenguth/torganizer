package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Restriction;

/**
 * A json-disciplines contains nothing but id, name and restrictions.
 */
@RequestScoped
public class DisciplineJsonConverter extends BaseModelJsonConverter<Discipline> {

  @Inject
  private RestrictionJsonConverter restrictionConverter;

  public DisciplineJsonConverter() {
  }

  public DisciplineJsonConverter(RestrictionJsonConverter restrictionConverter) {
    this.restrictionConverter = restrictionConverter;
  }

  @Override
  public JsonObject toJsonObject(Discipline discipline) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", discipline.getId());
    add(objectBuilder, "name", discipline.getName());

    Collection<Restriction> restrictions = discipline.getRestrictions();
    objectBuilder.add("restrictions", restrictionConverter.toJsonArray(restrictions));

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Discipline> disciplines) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    disciplines.forEach(discipline -> arrayBuilder.add(this.toJsonObject(discipline)));

    return arrayBuilder.build();
  }

  @Override
  public Discipline toModel(JsonObject jsonObject, Discipline discipline) {
    String name = get(jsonObject, "name");
    discipline.setName(name);

    return discipline;
  }

  @Override
  public Collection<Discipline> toModels(JsonArray jsonArray, Collection<Discipline> disciplines) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Discipline discipline = getProperEntity(jsonObject, disciplines);
      toModel(jsonObject, discipline);
    });

    return disciplines;
  }
}
