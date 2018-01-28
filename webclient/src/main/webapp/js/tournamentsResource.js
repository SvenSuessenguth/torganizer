/* global fetch */

class TournamentsResource {
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
  
  update(json, onSuccess, onFailure){    
    fetch('http://localhost:8080/rest/resources/tournaments/',{
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
  
  delete(json){    
  }
  
  getPlayers(tournamentId, offset, length, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/players?offset='+offset+'&length='+length)
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure(err); });
  }
  
  addPlayer(tournamentId, playerId, onSuccess, onFailure) {
    console.log("hier bin ich schon mal...");
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/players?pid='+playerId,{
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
  
  countPlayers(tournamentId, onSuccess, onFailure) {
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/players/count')
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
   .then(function(json) { onSuccess(json); })
   .catch(function(err) { onFailure("???"); });    
  }

  getSquads(tournamentId, offset, length, onSuccess, onFailure){
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/squads?offset='+offset+'&length='+length)
    .then(function(response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function(json) { onSuccess(json); })
    .catch(function(err) { onFailure("???"); });
  }

  addSquad(tournamentId, playerId, onSuccess, onFailure) {
    console.log("hier bin ich schon mal...");
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/squads?pid='+playerId,{
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

  countSquads(tournamentId, onSuccess, onFailure) {
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/squads/count')
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

var tournamentsResource = new TournamentsResource();