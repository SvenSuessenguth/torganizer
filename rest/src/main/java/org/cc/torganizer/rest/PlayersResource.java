package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.rest.json.ClubJsonConverter;
import org.cc.torganizer.rest.json.PlayerJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
  private PlayersRepository pRepository;

  @Inject
  private ClubsRepository cRepository;

  @Inject
  private PlayerJsonConverter pConverter;

  @Inject
  private ClubJsonConverter clubJsonConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    
    Player player = pConverter.toModel(jsonObject, new Player(new Person()));

    // use existing club instead of creating a new one
    Club club = null;
    JsonValue jsonValue = jsonObject.getJsonObject("club").get("id");
    if(jsonValue != NULL){
      Long id = Long.valueOf(jsonValue.toString());
      club = cRepository.read(id);
    }
    player.setClub(club);


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Player>> violations = validator.validate( player );
    
    if(violations.isEmpty()){    
      pRepository.create(player);
  
      return pConverter.toJsonObject(player);
    }
    
    return null;
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    Player player = pRepository.read(id);

    return  pConverter.toJsonObject(player);
  }
  
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Player> players = pRepository.read(offset,length);

    return pConverter.toJsonArray(players);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Player player = pRepository.read(id);

    // use existing club instead of creating a new one
    Club club = null;
    JsonValue jsonValue = jsonObject.getJsonObject("club").get("id");
    if(jsonValue != NULL){
      Long clubId = Long.valueOf(jsonValue.toString());
      club = cRepository.read(clubId);
    }
    player.setClub(club);

    player = pConverter.toModel(jsonObject, player);
    pRepository.update(player);

    return  pConverter.toJsonObject(player);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Player player = pRepository.delete(id);

    return pConverter.toJsonObject(player);
  }

  @GET
  @Path("/count")
  public long count() {
    return pRepository.count();
  }
}