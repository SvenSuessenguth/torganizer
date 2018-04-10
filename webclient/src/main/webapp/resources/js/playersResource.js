/* global fetch */

class PlayersResource extends Resource{
  constructor() {
    super();
  }
  
  // create a new or update an existing player
  // method must be one of "POST" (create) or "PUT" (update)
  createOrUpdate(player, method, onResolve, onReject){
    super.createOrUpdate("players", player, method, onResolve, onReject);
  }
  
  // read or delete an existing player
  // method must be one of "GET" (read) or "DELETE" (delete)
  readOrDelete(id, method, onResolve, onReject){
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
    .then(function(json) { onResolve(json); })
    .catch(function(err) { onReject("???"); });
  }
}

var playersResource = new PlayersResource();