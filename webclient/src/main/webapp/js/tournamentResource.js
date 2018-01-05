/* global fetch */

class TournamentResource {
  constructor() {
  }
  
  create(json, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments',{
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: json
    })
    .then(function(response){
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
  
  readSingle(tournamentId, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId)
    .then(function(response){
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
  
  readMultiple(offset, length, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments?offset='+offset+'&length='+length, {
      method: "GET",
      headers: {
        'Accept': 'application/json'
      }
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
  
  getSubscribers(tournamentId, offset, length, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers?offset='+offset+'&length='+length)
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }
  
  addSubscriber(tournamentId, playerId, onSuccess, onFailure) {    
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers/'+playerId,{
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
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
  
  countSubscribers(tournamentId, onSuccess, onFailure) {
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers/count')
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
   .then(function(json) { onSuccess(json); })
   .catch(function(err) { onFailure("???"); });    
  }
}

var tournamentResource = new TournamentResource();