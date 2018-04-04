package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.rest.json.PlayerJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

@Stateless
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayersResource {

  @Inject
  private PlayersRepository pRepository;

  @Inject
  private PlayerJsonConverter converter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    
    Player player = converter.toModel(jsonObject, new Player(new Person()));
    
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Player>> violations = validator.validate( player );
    
    if(violations.isEmpty()){    
      pRepository.create(player);
  
      return converter.toJsonObject(player);
    }
    
    return null;
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    Player player = pRepository.read(id);

    return  converter.toJsonObject(player);
  }
  
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Player> players = pRepository.read(offset,length);

    return converter.toJsonArray(players);
  }

  @PUT
  public String update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Player player = pRepository.read(id);

    player = converter.toModel(jsonObject, player);
    pRepository.update(player);

    return  converter.toJsonObject(player).toString();
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Player player = pRepository.delete(id);

    return converter.toJsonObject(player);
  }

  @GET
  @Path("/count")
  public long count() {
    return pRepository.count();
  }
}