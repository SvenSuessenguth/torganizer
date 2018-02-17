/* global fetch */

class PlayersResource {
  constructor() {
  }
  
  create(json, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/players',{
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: json
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
  
  readSingle(id, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/players/'+id, {
      method: "GET",
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
  
  update(json, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/players/',{
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: json
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
  
  delete(json, onSuccess, onFailure){
  }
}

var playersResource = new PlayersResource();