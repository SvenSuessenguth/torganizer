package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.rest.json.PlayerJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.validation.*;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

import static javax.json.JsonValue.NULL;

@Stateless
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayersResource {

  @Inject
  private PlayersRepository playersRepository;

  @Inject
  private ClubsRepository clubsRepository;

  @Inject
  private PlayerJsonConverter playersConverter;

  /**
   * Create a player.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Player player = playersConverter.toModel(jsonObject, new Player(new Person()));

    // use existing club instead of creating a new one
    Club club = null;
    JsonValue jsonValue = jsonObject.getJsonObject("club").get("id");
    if (jsonValue != NULL) {
      Long clubId = Long.valueOf(jsonValue.toString());
      club = clubsRepository.read(clubId);
    }
    player.setClub(club);

    validate(player);

    playersRepository.create(player);

    return playersConverter.toJsonObject(player);

  }

  /**
   * Getting the Player with the given id including the person.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    Player player = playersRepository.read(id);

    return playersConverter.toJsonObject(player);
  }

  /**
   * Getting multiple players from offset to maxResults including the persons.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Player> players = playersRepository.read(offset, maxResults);

    return playersConverter.toJsonArray(players);
  }

  /**
   * Updating the player or person with the given id.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Player player = playersRepository.read(id);

    // use existing club instead of creating a new one
    Club club = null;
    JsonValue jsonValue = jsonObject.getJsonObject("club").get("id");
    if (jsonValue != NULL) {
      Long clubId = Long.valueOf(jsonValue.toString());
      club = clubsRepository.read(clubId);
    }
    player.setClub(club);

    player = playersConverter.toModel(jsonObject, player);
    playersRepository.update(player);

    return playersConverter.toJsonObject(player);
  }

  /**
   * Deleting the player with the given id.
   */
  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Player player = playersRepository.delete(id);

    return playersConverter.toJsonObject(player);
  }


  @GET
  @Path("/count")
  public long count() {
    return playersRepository.count();
  }

  private void validate(Player player) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Player>> violations = validator.validate(player);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}