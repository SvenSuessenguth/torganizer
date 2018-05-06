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
      onReject("???");
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
        onReject("???");
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
        onReject("???");
      });
  }

  getRounds(disciplineId, onResolve, onReject){
    fetch(resourcesUrl()+'disciplines/' +disciplineId+"/rounds",{
      method: "GET",
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
        onReject("???");
      });
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
        onReject("???");
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
        onReject("???");
      });
  }
}

var disciplinesResource = new DisciplinesResource();