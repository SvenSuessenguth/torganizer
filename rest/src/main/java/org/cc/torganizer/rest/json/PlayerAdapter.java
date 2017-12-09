package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

/**
 * @author svens
 */
public class PlayerAdapter implements JsonbAdapter<Player, JsonObject>{

  @Override
  public JsonObject adaptToJson(Player player) throws Exception {
    String lastMatch = player.getLastMatch()==null?null:player.getLastMatch().toString();
    String status = player.getStatus()==null?null:player.getStatus().toString();
    Person person = player.getPerson();
    
    JsonObject value = Json.createObjectBuilder()
     .add("id", player.getId())
     .add("lastMatch", lastMatch)
     .add("status", status)
     .add("person", Json.createObjectBuilder()
         .add("id", person.getId())
         .add("firstName", person.getFirstName())
         .add("lastName", person.getLastName())
         .add("gender", person.getGender().toString())
         .add("dateOfBirth", person.getDateOfBirth().toString())
     )
     .build();
    
    return value;
  }

  @Override
  public Player adaptFromJson(JsonObject plazerJson) throws Exception {
    JsonObject personJson = plazerJson.getJsonObject("person");
    Person person = new Person();
    person.setId(Long.valueOf(personJson.getString("id")));
    person.setFirstName(personJson.getString("firstName"));
    person.setLastName(personJson.getString("lastName"));
    person.setGender(Gender.valueOf(personJson.getString("gender")));    
    person.setDateOfBirth(LocalDate.parse(personJson.getString("dateOfBirth"), DateTimeFormatter.ISO_DATE));
    
    Player player = new Player(person);
    player.setId(Long.valueOf(plazerJson.getString("id")));
    player.setLastMatch(LocalDateTime.parse(plazerJson.getString("lastMatch"), DateTimeFormatter.ISO_DATE));
    
    return player;
  }
}
