/* global fetch */

class PlayersResource {
  constructor() {
  }
  
  // create a new or update an existing player
  // method must be one of "POST" (create) or "PUT" (update)
  createOrUpdate(player, method, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/players',{
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(player)
    })
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
  
  // read or delete an existing player
  // method must be one of "GET" (read) or "DELETE" (delete)
  readOrDelete(id, method, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/players/'+id, {
      method: method,
      headers: {
        'Accept': 'application/json'
      }
    }).then(function(response) {
      if (response.ok){
        var json = response.json();
        return json;
      } else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
}

var playersResource = new PlayersResource();