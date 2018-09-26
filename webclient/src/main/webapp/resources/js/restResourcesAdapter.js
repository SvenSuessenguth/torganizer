/* global fetch */

let restResourceAdapter = {

  createOrUpdate: function createOrUpdate(resource, json, onResolve) {

    let url = resourcesUrl() + resource;
    let body = JSON.stringify(json);
    // if json has an id there is an already persisted instance
    // so using "PUT" for updating the data, otherwise using "POST" for creating
    let method = "PUT";
    if (json.id == null) {
      method = "POST";
    }

    restResourceAdapter.callRestResource(url, method, body, onResolve);
  },

  getSingle: function getSingle(resource, id, onResolve) {
    let url = resourcesUrl() + resource + '/' + id;

    restResourceAdapter.callRestResource(url, "GET", "", onResolve);
  },

  getMultiple: function getMultiple(resource, offset, maxResults, onResolve) {
    let url = resourcesUrl() + resource + '?offset=' + offset + '&maxResults=' + maxResults;

    restResourceAdapter.callRestResource(url, "GET", "", onResolve);
  },

  getMultipleResources: function getMultipleResources(url, offset, maxResults, onResolve) {
    let completeUrl = url + '?offset=' + offset + '&maxResults=' + maxResults;

    restResourceAdapter.callRestResource(completeUrl, "GET", "", onResolve);
  },

  getResources: function getResources(url, onResolve) {
    restResourceAdapter.callRestResource(url, "GET", "", onResolve);
  },

  callRestResource : function callRestResource(url, method, body, onResolve){
    restResourceAdapter.clearMessages();

    fetch(url, {
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: (body!==""?body:undefined)
    })
      .then(function (response) {
        // text is a stream and therefore on-time-readable only
        // https://github.com/whatwg/fetch/issues/196
        let text = response.text();
        if (response.ok)
          text.then(function (respJson) {
            onResolve(JSON.parse(respJson));
          })
        else
          text.then(function (respJson) {
            restResourceAdapter.processMessages(JSON.parse(respJson));
          })
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  },

  clearMessages: function clearMessages() {
    // unmark elements
    let elements = document.querySelectorAll(".violation");
    elements.forEach(element => {
      element.classList.remove("violation");
    });

    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    let ul = document.getElementById("violations");
    while (ul.firstChild) {
      ul.removeChild(ul.firstChild);
    }
  },

  processMessages: function processMessages(json) {
    restResourceAdapter.showMessages(json);
    restResourceAdapter.markElements(json);
  },

  showMessages: function showMessages(json) {
    let ul = document.getElementById("violations");
    json.violations.forEach(violation => {
      let li = document.createElement("li");
      li.setAttribute("class", "violation");
      li.appendChild(document.createTextNode(violation.message));
      ul.appendChild(li);
    });
  },

  markElements: function markElements(json) {
    json.violations.forEach(violation => {
      let element = document.getElementById(violation.propertyPath);
      element.classList.add("violation");
    });
  },

  resourceError: function resourceError(err) {
    console.log(err);
  },
}