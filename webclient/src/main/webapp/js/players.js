var tableSize = Number(10);

function onLoad(){
  // if current tournament is selected, activate checkbox to auto add new player
  var currentTournamentName = localStorage.getItem('tournaments-current-tournament-name')

  if(currentTournamentName){
    document.getElementById('autoAdd').disabled = false;
    document.getElementById('autoAdd').checked = true;
    document.getElementById('autoAddLabel').innerHTML = currentTournamentName 
  }else{
    document.getElementById('autoAdd').disabled = true;
    document.getElementById('autoAdd').checked = false;
    document.getElementById('autoAddLabel').innerHTML = '-/-'
  }
}

//
//Create new person, send to the server and persist in database.
//After that the table with all persons is updated.
//
function createPlayer(){

  //if(!isFormValid()){ return; }

  fetch('http://localhost:8080/rest/resources/players/create',{
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: playerFormToJSon()
  }).then(function(response) {
    return response;
  }).then(function(data) {
  }).catch(function(err) {
  });
  
  window.location.reload(true);
}

function playerFormToJSon(){
  // {
  //   "id":null,
  //   "status":"ACTIVE",
  //   "person":{
  //     "id":null,
  //     "firstName":"dings",
  //     "lastName":"bums",
  //     "gender":"UNKNOWN",
  //     "dateOfBirthISO":""},
  //   "lastMatch":null
  // }
  var playerId= Number(localStorage.getItem('players-current-player-id'))
  var personId = Number(localStorage.getItem('players-current-person-id'))
  var firstName = document.getElementById("pdFirstName").value
  var lastName = document.getElementById("pdLastName").value
  var dateOfBirth = document.getElementById("pdDateOfBirth").value
  
  var genderElement = document.getElementById("pdGender");
  var gender = genderElement.options[genderElement.selectedIndex].value
  
  var statusElement = document.getElementById("pdStatus");
  var status = statusElement.options[statusElement.selectedIndex].value
  
  var json = JSON.stringify({
    "id": playerId,
    "status": status,
    "person":{
      "firstName": firstName,
      "lastName": lastName,
      "dateOfBirthISO": dateOfBirth,
      "gender": gender
    }
  })
  
  return json;
}

