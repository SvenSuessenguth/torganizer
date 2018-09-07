/* global fetch */

class   DisciplinesResource{
  constructor() {
 }

  getOpponents(disciplineId, offset, maxResults, onResolve) {
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/opponents")
    .then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
    .then(function (json) {
        onResolve(json);
    })
    .catch(function (err) {
      resourceError(err);
    });
  }

  addOpponent(disciplineId, opponentId, onResolve) {
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/opponents?opponentId="+opponentId,{
      method: "POST",
        headers: {
        'Accept': 'application/json'
      }
    }).then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        resourceError(err);
      });
  }

  removeOpponent(disciplineId, opponentId, onResolve) {
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/opponents?opponentId="+opponentId,{
      method: "DELETE",
      headers: {
        'Accept': 'application/json'
      }
    }).then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        resourceError(err);
      });
  }

  getRounds(disciplineId, onResolve){
    getMultipleResources(resourcesUrl()+'disciplines/' +disciplineId+"/rounds", 0, 100, onResolve);
  }

  addRound(disciplineId, roundId, onResolve){
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/rounds?roundId="+roundId,{
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
    }).then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        resourceError(err);
      });
  }
  removeRound(disciplineId, roundId, onResolve){
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/rounds?roundId="+roundId,{
      method: "DELETE",
      headers: {
        'Accept': 'application/json'
      }
    }).then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        resourceError(err);
      });
  }
}

var disciplinesResource = new DisciplinesResource();