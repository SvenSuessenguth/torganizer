/* global fetch */

class   DisciplinesResource {
  constructor() {
  }

  createOrUpdate(json, method, onResolve, onReject) {
    fetch(resourcesUrl()+'disciplines', {
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(json)
    })
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error(response.status);
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        onReject(err);
      });
  }

  readSingle(id, onResolve, onReject) {
    fetch(resourcesUrl()+'disciplines/' + id).then(function (response) {
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
}

var disciplinesResource = new DisciplinesResource();