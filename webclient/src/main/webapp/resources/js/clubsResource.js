/* global fetch */

class ClubsResource {
  constructor() {
  }

  createOrUpdate(json, method, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs', {
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: json
    })
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

  readSingle(clubId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId)
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

  readMultiple(onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs', {
      method: "GET",
      headers: {
        'Accept': 'application/json'
      }
    })
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
}

var clubsResource = new ClubsResource();