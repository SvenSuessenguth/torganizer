/* global fetch */

class CrudResource {
  constructor() {
  }

  createOrUpdate(resource, json, method, onResolve, onReject) {

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
          onReject(err);
      });
  }

  readSingle(resource, id, onResolve, onReject) {
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
        onReject(err);
      });
  }

  readMultiple(resource, offset, maxResults, onResolve, onReject) {
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
        onReject(err);
      });
  }

  delete(json) {
  }
}

//
// default for rejecting a resource-call
//
function resourceReject(json){
    console.log(json);
}