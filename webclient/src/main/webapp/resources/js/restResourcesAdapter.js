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
      // text is a stream and therefore on-time-readable only
      // https://github.com/whatwg/fetch/issues/196
      let text = response.text();
      if (response.ok)
        text.then(function (respJson) { onResolve(JSON.parse(respJson)); })
      else
        text.then(function (respJson) { resourceReject(JSON.parse(respJson)); })
    })
    .catch(function (err) { resourceError(err); });
}

function getSingle(resource, id, onResolve) {
  fetch(resourcesUrl() + resource + '/' + id, {
    method: "GET",
    headers: {
      'Accept': 'application/json'
    }
  })
    .then(function (response) {
      // text is a stream and therefore on-time-readable only
      // https://github.com/whatwg/fetch/issues/196
      let text = response.text();
      if (response.ok)
        text.then(function (respJson) { onResolve(JSON.parse(respJson)); })
      else
        text.then(function (respJson) { resourceReject(JSON.parse(respJson)); })
    })
    .catch(function (err) { resourceError(err); });
}

function getMultiple(resource, offset, maxResults, onResolve) {
  fetch(resourcesUrl() + resource + '?offset=' + offset + '&maxResults=' + maxResults, {
    method: "GET",
    headers: {
      'Accept': 'application/json'
    }
  })
    .then(function (response) {
      // text is a stream and therefore on-time-readable only
      // https://github.com/whatwg/fetch/issues/196
      let text = response.text();
      if (response.ok)
        text.then(function (respJson) { onResolve(JSON.parse(respJson)); })
      else
        text.then(function (respJson) { resourceReject(JSON.parse(respJson)); })
    })
    .catch(function (err) { resourceError(err); });
}

//
// default for rejecting a resource-call
//
function resourceReject(json) {
  console.log(json);
}

//
//  handling errrs
//
function resourceError(err){
  console.log(err);
}