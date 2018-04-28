/* global fetch */

class PlayersResource {
  constructor() {
  }
  
  // create a new or update an existing player
  // method must be one of "POST" (create) or "PUT" (update)
  createOrUpdate(player, method, onResolve, onReject){
    fetch(resourcesUrl()+'players',{
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
        throw new Error(response.status);
    })
    .then(function(json) { onResolve(json); })
    .catch(function(err) { onReject(err); });
  }
  
  // read or delete an existing player
  // method must be one of "GET" (read) or "DELETE" (delete)
  readOrDelete(id, method, onResolve, onReject){
    fetch(resourcesUrl()+'players/'+id, {
      method: method,
      headers: {
        'Accept': 'application/json'
      }
    }).then(function(response) {
      if (response.ok){
        var json = response.json();
        return json;
      } else {
        throw new Error('Fehlerhandling noch nicht spezifiziert');
      }
    })
    .then(function(json) { onResolve(json); })
    .catch(function(err) { onReject("???"); });
  }
}

var playersResource = new PlayersResource();