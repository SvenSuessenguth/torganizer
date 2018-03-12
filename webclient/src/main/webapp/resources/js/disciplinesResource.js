/* global fetch */

class DisciplinesResource {
  constructor() {
  }

  createOrUpdate(json, method, onSuccess, onFailure) {
    fetch('http://localhost:8080/rest/resources/disciplines', {
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
        onSuccess(json);
      })
      .catch(function (err) {
        onFailure(err);
      });
  }

  readSingle(id, onSuccess, onFailure) {
    fetch('http://localhost:8080/rest/resources/disciplines/' + id).then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('Fehlerhandling noch nicht spezifiziert');
    })
      .then(function (json) {
        onSuccess(json);
      })
      .catch(function (err) {
        onFailure("???");
      });
  }
}

var disciplinesResource = new DisciplinesResource();