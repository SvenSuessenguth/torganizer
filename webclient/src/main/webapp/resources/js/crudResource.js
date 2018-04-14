/* global fetch */

class CrudResource {
  constructor() {
  }

  createOrUpdate(resource, json, method, onResolve, onReject) {

    console.log(JSON.stringify(json));

    fetch('http://localhost:8080/rest/resources/'+resource, {
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
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        onReject("???");
      });
  }

  readSingle(resource, id, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/'+resource+'/' + id)
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

  readMultiple(resource, offset, maxResults, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/'+resource+'?offset=' + offset + '&maxResults=' + maxResults, {
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

  delete(json) {
  }
}