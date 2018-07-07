/* global fetch */

class   DisciplinesResource extends CrudResource{
  constructor() {
    super();
  }

  createOrUpdate(json, method, onResolve, onReject) {
    super.createOrUpdate("disciplines", json, method, onResolve, onReject);
  }

  readSingle(id, onResolve, onReject) {
    super.readSingle("disciplines", id, onResolve, onReject);
  }

  getOpponents(disciplineId, offset, maxResults, onResolve, onReject) {
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
      onReject(err);
    });
  }

  addOpponent(disciplineId, opponentId, onResolve, onReject) {
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
        onReject(err);
      });
  }

  removeOpponent(disciplineId, opponentId, onResolve, onReject) {
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
        onReject(err);
      });
  }

  getRounds(disciplineId, onResolve, onReject){
    super.readMultiple('disciplines/' +disciplineId+"/rounds", 0, 100, onResolve, onReject);
  }

  addRound(disciplineId, roundId, onResolve, onReject){
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
        onReject(err);
      });
  }
  removeRound(disciplineId, roundId, onResolve, onReject){
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
        onReject(err);
      });
  }
}

var disciplinesResource = new DisciplinesResource();