/* global fetch */

class CrudResource {
  constructor() {
  }

  readSingle(resource, id, onResolve) {
    fetch(restResourceAdapter.resourcesUrl()+resource+'/' + id)
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
        restResourceAdapter.resourceError(err);
      });
  }

  readMultiple(resource, offset, maxResults, onResolve) {
    fetch(restResourceAdapter.resourcesUrl()+resource+'?offset=' + offset + '&maxResults=' + maxResults, {
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
        restResourceAdapter.resourceError(err);
      });
  }

  delete(json) {
  }
}