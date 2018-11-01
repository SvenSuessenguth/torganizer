class Crud{
  constructor(){
  }

  get(resource, callback){
    fetch(resource, {
      method: "GET",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    })
    .then(function(response) { return response.json(); })
    .then(function(json) { callback(json); });
  }

  createOrUpdate(resource, json, callback) {
    let body = JSON.stringify(json);

    // if json has an id there is an already persisted instance
    // so using "PUT" for updating the data, otherwise using "POST" for creating
    let method = "PUT";
    if (json.id == null) {
      method = "POST";
    }

    fetch(resource, {
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: body !== "" ? body : undefined
    })
    .then(function(response) { return response.json(); })
    .then(function(json) { callback(json); });
  }
}