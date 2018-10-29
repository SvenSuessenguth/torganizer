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
}