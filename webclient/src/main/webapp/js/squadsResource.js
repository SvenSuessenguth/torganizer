/* global fetch */

class SquadsResource {
  constructor() {
  }
  
  createOrUpdate(json, method, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/squads',{
      method: method,
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
    fetch('http://localhost:8080/rest/resources/squads/'+id).then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
}

var squadsResource = new SquadsResource();