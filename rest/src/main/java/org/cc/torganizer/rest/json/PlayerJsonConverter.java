package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;

import static org.cc.torganizer.rest.util.Strings.isEmpty;

@RequestScoped
public class PlayerJsonConverter extends BaseModelJsonConverter<Player>
  implements OpponentJsonConverter {

  @Inject
  private PersonJsonConverter personConverter;

  @Inject
  private ClubJsonConverter clubConverter;

  public PlayerJsonConverter() {
  }

  public PlayerJsonConverter(PersonJsonConverter personConverter,
                             ClubJsonConverter clubConverter) {
    this.clubConverter = clubConverter;
    this.personConverter = personConverter;
  }

  @Override
  public JsonObject toJsonObject(Player player) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", player.getId());
    add(objectBuilder, "lastMatch", player.getLastMatch());
    add(objectBuilder, "status", player.getStatus().toString());
    add(objectBuilder, "person", personConverter.toJsonObject(player.getPerson()));
    add(objectBuilder, "club", clubConverter.toJsonObject(player.getClub()));

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection players) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    players.forEach(player -> arrayBuilder.add(this.toJsonObject((Player) player)));

    return arrayBuilder.build();
  }

  @Override
  public Player toModel(JsonObject jsonObject, Player player) {
    String lastMatchString = get(jsonObject, "lastMatch");
    LocalDateTime lastMatch = super.localDateTimeFromString(lastMatchString);
    player.setLastMatch(lastMatch);

    String statusString = get(jsonObject, "status");
    Status status = isEmpty(statusString)
      ? Status.ACTIVE
      : Status.valueOf(statusString);
    player.setStatus(status);

    JsonObject personJsonObject = jsonObject.getJsonObject("person");
    final Person person = personConverter.toModel(personJsonObject, player.getPerson());
    player.setPerson(person);

    return player;
  }

  @Override
  public Collection toModels(JsonArray jsonArray, Collection players) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Player player = getProperEntity(jsonObject, players);
      toModel(jsonObject, player);
    });

    return players;
  }

  @Override
  public OpponentType getOpponentType() {
    return OpponentType.PLAYER;
  }
}
