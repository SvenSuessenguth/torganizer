/* global fetch */

class SquadsResource {
  constructor() {
  }
  
  createOrUpdate(json, method, onResolve, onReject){
    fetch('http://localhost:8080/rest/resources/squads',{
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(json)
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
  
  readSingle(id, onResolve, onReject){
    fetch('http://localhost:8080/rest/resources/squads/'+id).then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onResolve(json); })
    .catch(function(err) { onReject("???"); });
  }
}

var squadsResource = new SquadsResource();