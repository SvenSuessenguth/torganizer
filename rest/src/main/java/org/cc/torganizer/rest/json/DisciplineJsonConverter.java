package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Discipline;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * A json-disciplines contains nothing but id, name and restrictions.
 *
 */
@RequestScoped
public class DisciplineJsonConverter extends ModelJsonConverter<Discipline> {

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
    objectBuilder.add("restrictions", restrictionConverter.toJsonArray(discipline.getRestrictions()));

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
