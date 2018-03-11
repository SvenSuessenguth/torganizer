package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;

/**
 * What does a restriction-json look like:
 * <p>
 * AgeRestriction
 * <pre>
 * {
 *   id:
 *   type:
 *   maxDateOfBirth:
 *   minDateOfBirth:
 * }
 * </pre>
 *
 * @author svens
 */
@RequestScoped
public class RestrictionJsonConverter extends ModelJsonConverter<Restriction> {

  public RestrictionJsonConverter() {
  }

  @Override
  public JsonObject toJsonObject(Restriction restriction) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    Restriction.Discriminator discriminator = restriction.getDiscriminator();

    add(objectBuilder, "id", restriction.getId());
    add(objectBuilder, "discriminator", discriminator.getId());

    switch (discriminator) {
      case AGE_RESTRICTION:
        toJsonObject((AgeRestriction) restriction, objectBuilder);
        break;
      case GENDER_RESTRICTION:
        toJsonObject((GenderRestriction) restriction, objectBuilder);
        break;
      case OPPONENT_TYPE_RESTRICTION:
        toJsonObject((OpponentTypeRestriction) restriction, objectBuilder);
        break;
      default:
        throw new IllegalArgumentException("Can't convert restriction with discriminator-id " + restriction.getDiscriminator().getId() + " to json.");
    }

    return objectBuilder.build();
  }

  public JsonObjectBuilder toJsonObject(AgeRestriction restriction, JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "minDateOfBirth", restriction.getMinDateOfBirth());
    add(objectBuilder, "maxDateOfBirth", restriction.getMaxDateOfBirth());

    return objectBuilder;
  }

  public JsonObjectBuilder toJsonObject(GenderRestriction restriction, JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "validGender", restriction.getValidGender().toString());

    return objectBuilder;
  }

  public JsonObjectBuilder toJsonObject(OpponentTypeRestriction restriction, JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "validOpponentType", restriction.getValidOpponentType().toString());

    return objectBuilder;
  }

  @Override
  public JsonArray toJsonArray(Collection<Restriction> restrictions) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    restrictions.forEach(restriction -> arrayBuilder.add(this.toJsonObject(restriction)));

    return arrayBuilder.build();
  }

  @Override
  public Restriction toModel(JsonObject jsonObject) {

    String discriminatorId = jsonObject.getString("discriminator");
    Restriction.Discriminator discriminator = Restriction.Discriminator.byId(discriminatorId);
    Restriction restriction = discriminator.create();

    String idString = get(jsonObject, "id");
    Long id = idString == null ? null : Long.valueOf(idString);
    restriction.setId(id);

    switch (discriminator) {
      case AGE_RESTRICTION:
        restriction = toModel((AgeRestriction) restriction, jsonObject);
        break;
      case GENDER_RESTRICTION:
        restriction = toModel((GenderRestriction) restriction, jsonObject);
        break;
      case OPPONENT_TYPE_RESTRICTION:
        restriction = toModel((OpponentTypeRestriction) restriction, jsonObject);
        break;
      default:
        throw new IllegalArgumentException("Can't convert json with discriminator-id " + restriction.getDiscriminator().getId() + " to model.");

    }
    return restriction;
  }

  private AgeRestriction toModel(AgeRestriction ageRestriction, JsonObject jsonObject) {
    String minDateOfBirthString = get(jsonObject, "minDateOfBirth");
    LocalDate minDateOfBirth = minDateOfBirthString == null || minDateOfBirthString.trim().isEmpty() ? null : LocalDate.parse(minDateOfBirthString, DateTimeFormatter.ISO_DATE);
    ageRestriction.setMinDateOfBirth(minDateOfBirth);

    String maxDateOfBirthString = get(jsonObject, "maxDateOfBirth");
    LocalDate maxDateOfBirth = maxDateOfBirthString == null || maxDateOfBirthString.trim().isEmpty() ? null : LocalDate.parse(maxDateOfBirthString, DateTimeFormatter.ISO_DATE);
    ageRestriction.setMaxDateOfBirth(maxDateOfBirth);

    return ageRestriction;
  }

  private GenderRestriction toModel(GenderRestriction genderRestriction, JsonObject jsonObject) {
    String validGenderString = get(jsonObject, "validGender");
    Gender validGender = Gender.valueOf(validGenderString);
    genderRestriction.setValidGender(validGender);

    return genderRestriction;
  }

  private OpponentTypeRestriction toModel(OpponentTypeRestriction opponentTypeRestriction, JsonObject jsonObject) {
    String validOpponentTypeString = get(jsonObject, "validOpponentType");
    OpponentType validOpponentType = OpponentType.valueOf(validOpponentTypeString);
    opponentTypeRestriction.setValidOpponentType(validOpponentType);

    return opponentTypeRestriction;
  }

  @Override
  public Collection<Restriction> toModels(JsonArray jsonArray) {
    List<Restriction> restrictions = new ArrayList<>();

    jsonArray.forEach((JsonValue arrayValue) -> restrictions.add(toModel((JsonObject) arrayValue)));

    return restrictions;
  }
}
