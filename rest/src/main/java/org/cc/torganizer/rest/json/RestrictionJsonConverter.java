package org.cc.torganizer.rest.json;

import static org.cc.torganizer.rest.util.Strings.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;

/**
 * Converting a restriction from/to json.
 * What does a restriction-json look like:
 *
 * <p>AgeRestriction
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
public class RestrictionJsonConverter extends BaseModelJsonConverter<Restriction> {

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
        throw new IllegalArgumentException("Can't convert restriction with discriminator-id "
            + restriction.getDiscriminator().getId() + " to json.");
    }

    return objectBuilder.build();
  }

  public JsonObjectBuilder toJsonObject(AgeRestriction restriction,
                                        JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "minDateOfBirth", restriction.getMinDateOfBirth());
    add(objectBuilder, "maxDateOfBirth", restriction.getMaxDateOfBirth());

    return objectBuilder;
  }

  public JsonObjectBuilder toJsonObject(GenderRestriction restriction,
                                        JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "gender", restriction.getGender().toString());

    return objectBuilder;
  }

  public JsonObjectBuilder toJsonObject(OpponentTypeRestriction restriction,
                                        JsonObjectBuilder objectBuilder) {
    add(objectBuilder, "opponentType", restriction.getOpponentType().toString());

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
  public Restriction toModel(JsonObject jsonObject, Restriction restriction) {

    String discriminatorId = jsonObject.getString("discriminator");
    Restriction.Discriminator discriminator = Restriction.Discriminator.byId(discriminatorId);

    switch (discriminator) {
      case AGE_RESTRICTION:
        toModel((AgeRestriction) restriction, jsonObject);
        break;
      case GENDER_RESTRICTION:
        toModel((GenderRestriction) restriction, jsonObject);
        break;
      case OPPONENT_TYPE_RESTRICTION:
        toModel((OpponentTypeRestriction) restriction, jsonObject);
        break;
      default:
        throw new IllegalArgumentException("Can't convert json with discriminator-id "
            + restriction.getDiscriminator().getId() + " to model.");
    }

    return restriction;
  }

  private AgeRestriction toModel(AgeRestriction ageRestriction, JsonObject jsonObject) {
    String minDateOfBirthString = get(jsonObject, "minDateOfBirth");
    LocalDate minDateOfBirth = isEmpty(minDateOfBirthString)
        ? null
        : LocalDate.parse(minDateOfBirthString, DateTimeFormatter.ISO_DATE);
    ageRestriction.setMinDateOfBirth(minDateOfBirth);

    String maxDateOfBirthString = get(jsonObject, "maxDateOfBirth");
    LocalDate maxDateOfBirth = isEmpty(maxDateOfBirthString)
        ? null
        : LocalDate.parse(maxDateOfBirthString, DateTimeFormatter.ISO_DATE);
    ageRestriction.setMaxDateOfBirth(maxDateOfBirth);

    return ageRestriction;
  }

  private GenderRestriction toModel(GenderRestriction genderRestriction, JsonObject jsonObject) {
    String genderString = get(jsonObject, "gender");
    Gender gender = Gender.valueOf(genderString);
    genderRestriction.setGender(gender);

    return genderRestriction;
  }

  private OpponentTypeRestriction toModel(OpponentTypeRestriction opponentTypeRestriction,
                                          JsonObject jsonObject) {
    String opponentTypeString = get(jsonObject, "opponentType");
    OpponentType opponentType = OpponentType.valueOf(opponentTypeString);
    opponentTypeRestriction.setOpponentType(opponentType);

    return opponentTypeRestriction;
  }

  @Override
  public Collection<Restriction> toModels(JsonArray jsonArray,
                                          Collection<Restriction> restrictions) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Restriction restriction = getProperEntity(jsonObject, restrictions);
      toModel(jsonObject, restriction);
    });

    return restrictions;
  }
}
