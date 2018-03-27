package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.rest.json.PlayerJsonConverter;
import org.cc.torganizer.rest.json.SquadJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/squads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SquadsResource {

  @Inject
  private SquadJsonConverter converter;
  
  @Inject
  private PlayerJsonConverter pConverter;

  @Inject
  private SquadsRepository sRepository;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Squad squad = converter.toModel(jsonObject);
    squad = sRepository.create(squad);

    return converter.toJsonObject(squad);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Squad squad = sRepository.read(id);

    return  converter.toJsonObject(squad);
  }
  
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Squad> squads = sRepository.read(offset, length);

    return converter.toJsonArray(squads);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Squad squad = converter.toModel(jsonObject);
    squad = sRepository.update(squad);

    return  converter.toJsonObject(squad);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Squad squad = sRepository.delete(id);

    return converter.toJsonObject(squad);
  }

  @GET
  @Path("/count")
  public long count() {
    return sRepository.count();
  }
  
  @GET
  @Path("/{id}/players")
  public JsonArray players(@PathParam("id") Long squadId) {
    List<Player> players = sRepository.getPlayers(squadId);

    return pConverter.toJsonArray(players);
  }
}