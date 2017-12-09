package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.adapter.JsonbAdapter;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

/**
 * @author svens
 */
public class PlayerAdapter implements JsonbAdapter<Player, JsonObject>{

  private final static Logger LOGGER = Logger.getLogger(PlayerAdapter.class.getName());
  
  public Long toLong(JsonValue jsonValue){
    
    Long l = null;
    
    if(jsonValue==null||JsonObject.NULL.equals(jsonValue)){
      return null;
    }
    try{
      l = Long.valueOf(jsonValue.toString());
    }catch(NumberFormatException nfExc){
      LOGGER.log(Level.WARNING, "Fehler beim konvertieren von '{0}' in einen Long-Wert", jsonValue);
    }
    
    return l;
  }
  
  public String fromLong(Long value){
    return value==null?"":value.toString();
  }
  
  public LocalDate toLocalDate(JsonValue value){
    LocalDate ld = null;
    
    if(value==null
       || JsonObject.NULL.equals(value)
       || !JsonValue.ValueType.STRING.equals(value.getValueType())){
      return null;
    }
    
    String localDateISO = ((JsonString) value).getString();
    try{
      ld = LocalDate.parse(localDateISO, DateTimeFormatter.ISO_DATE);
    }catch(DateTimeParseException dtpExc){
      LOGGER.log(Level.WARNING, "Fehler beim konvertieren von '{0}' in einen LocalDate-Wert", localDateISO);
    }
    
    return ld;
  }
  
  public JsonValue fromLocalDate(String key, LocalDate value){
    return value==null?JsonValue.NULL:Json.createObjectBuilder().add(key, value.toString()).build().get(key);
  }
  
  public JsonValue fromLocalDateTime(String key, LocalDateTime ldt){
    return ldt==null?JsonValue.NULL:Json.createObjectBuilder().add(key, ldt.toString()).build().get(key);
  }
  
  public JsonValue fromString(String key, String value){
    return value==null?JsonValue.NULL:Json.createObjectBuilder().add(key, value).build().get(key);
  }
  
  public LocalDateTime toLocalDateTime(JsonValue value){
    LocalDateTime ldt = null;
    
    if(value==null||JsonObject.NULL.equals(value)){
      return null;
    }
    try{
      ldt = LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_DATE);
    }catch(DateTimeParseException dtpExc){
      LOGGER.log(Level.WARNING, "Fehler beim konvertieren von '{0}' in einen LocalDate-Wert", value);
    }
    
    return ldt;
  }
  
  @Override
  public JsonObject adaptToJson(Player player) throws Exception {
    String status = player.getStatus()==null?null:player.getStatus().toString();
    Person person = player.getPerson();
    
    return Json.createObjectBuilder()
     .add("id", fromLong(player.getId()))
     .add("lastMatch", fromLocalDateTime("lastMatch", player.getLastMatch()))
     .add("status", status)
     .add("person", Json.createObjectBuilder()
         .add("id", fromLong(person.getId()))
         .add("firstName", fromString("firstName", person.getFirstName()))
         .add("lastName", fromString("lastName", person.getLastName()))
         .add("gender", person.getGender().toString())             
         .add("dateOfBirth", fromLocalDate("dateOfBirth", person.getDateOfBirth()))
     )
     .build();    
  }

  @Override
  public Player adaptFromJson(JsonObject playerJson) throws Exception {
    JsonObject personJson = playerJson.getJsonObject("person");
    Person person = new Person();
    
    person.setId(toLong(personJson.get("id")));
    person.setFirstName(personJson.getString("firstName"));
    person.setLastName(personJson.getString("lastName"));
    person.setGender(Gender.valueOf(personJson.getString("gender")));    
    person.setDateOfBirth(toLocalDate(personJson.get("dateOfBirth")));
    
    Player player = new Player(person);
    player.setId(toLong(playerJson.get("id")));
    player.setLastMatch(toLocalDateTime(playerJson.get("lastMatch")));
    
    return player;
  }
}
