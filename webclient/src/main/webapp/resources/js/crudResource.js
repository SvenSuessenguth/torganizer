/* global fetch */

class CrudResource {
  constructor() {
  }

  createOrUpdate(resource, json, method, onResolve) {

    console.log("create:");
    console.log(JSON.stringify(json));

    fetch(resourcesUrl()+resource, {
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
        resourceError(err);
      });
  }

  readSingle(resource, id, onResolve) {
    fetch(resourcesUrl()+resource+'/' + id)
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

  readMultiple(resource, offset, maxResults, onResolve) {
    fetch(resourcesUrl()+resource+'?offset=' + offset + '&maxResults=' + maxResults, {
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
        resourceError(err);
      });
  }

  delete(json) {
  }
}