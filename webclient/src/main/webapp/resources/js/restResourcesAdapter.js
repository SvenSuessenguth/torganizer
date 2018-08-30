/* global fetch */

function createOrUpdate(resource, json, onResolve) {

  console.log("create:");
  console.log(JSON.stringify(json));

  // if json has an id there is an already persisted instance
  // so using "PUT" for updating the data, otherwise using "POST" for creating
  let method = "PUT";
  if (json.id == null) {
    method = "POST";
  }

  fetch(resourcesUrl() + resource, {
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
        throw new Error('error while creating/updating ${resource} with method ${method} and json ${json}');
    })
    .then(function (json) {
      onResolve(json);
    })
    .catch(function (err) {
      resourceReject(err);
    });
}

function getSingle(resource, id, onResolve) {
  fetch(resourcesUrl() + resource + '/' + id, {
    method: "GET",
    headers: {
      'Accept': 'application/json'
    }
  })
    .then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('error while reading ${resource} with id ${id}');
    })
    .then(function (json) {
      onResolve(json);
    })
    .catch(function (err) {
      resourceReject(err);
    });
}

function getMultiple(resource, offset, maxResults, onResolve) {
  fetch(resourcesUrl() + resource + '?offset=' + offset + '&maxResults=' + maxResults, {
    method: "GET",
    headers: {
      'Accept': 'application/json'
    }
  })
    .then(function (response) {
      if (response.ok)
        return response.json();
      else
        throw new Error('error while reading multiple ${resource} with offset ${offset} and maxResult ${maxResult}');
    })
    .then(function (json) {
      onResolve(json);
    })
    .catch(function (err) {
      resourceReject(err);
    });
}


//
// default for rejecting a resource-call
//
function resourceReject(json) {
  console.log(json);
}