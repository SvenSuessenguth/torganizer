var tableSize = Number(10);

function onLoad(){
  // if current tournament is selected, activate checkbox to auto add new player
  var currentTournamentName = localStorage.getItem('tournaments-current-tournament-name')
  var currentTournamentId = localStorage.getItem('tournaments-current-tournament-id')

  if(currentTournamentName){
    document.getElementById('autoAdd').disabled = false;
    document.getElementById('autoAdd').checked = true;
    document.getElementById('autoAddLabel').innerHTML = currentTournamentName 
  }else{
    document.getElementById('autoAdd').disabled = true;
    document.getElementById('autoAdd').checked = false;
    document.getElementById('autoAddLabel').innerHTML = '-/-'
  }
  
  // store number of players
  var countTournamentPlayers = getTournamentNumberOfPlayers(currentTournamentId);
  localStorage.setItem('tournament-number-of-players', countTournamentPlayers);
  
  // show first set of players
  showPlayersTable();
}

function createPlayer(){

  if(!isFormValid('playersForm')){ return; }
  
  fetch('http://localhost:8080/rest/resources/players/create',{
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: playerFormToJSon()
  }).then(function(response) {
    return response.json();
  }).then(function(player) {    
    if(document.getElementById('autoAdd').checked){
      var currentTournamentId = localStorage.getItem('tournaments-current-tournament-id');      
      addPlayerToTournament(currentTournamentId, player.id);
    }
    
    window.location.reload(true);
  }).catch(function(err) {
    
    window.location.reload(true);
  });
}

function updatePlayer(){

  if(!isFormValid('playersForm')){ return; }
  
  fetch('http://localhost:8080/rest/resources/players/update',{
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: playerFormToJSon()
  }).then(function(response) {
    return response.json();
  }).then(function(player) {    
    if(document.getElementById('autoAdd').checked){
      var currentTournamentId = localStorage.getItem('tournaments-current-tournament-id');      
      addPlayerToTournament(currentTournamentId, player.id);
    }
    
    window.location.reload(true);
  }).catch(function(err) {
    
    window.location.reload(true);
  });
}



function playerFormToJSon(){
  var playerId= Number(localStorage.getItem('players-current-player-id'))
  var personId = Number(localStorage.getItem('players-current-player-person-id'))
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
      "id": personId,
      "firstName": firstName,
      "lastName": lastName,
      "dateOfBirthISO": dateOfBirth,
      "gender": gender
    }
  })
  
  console.log("player: "+json)
  return json;
}

function showPlayersTable(){
  var offset = Number(localStorage.getItem('players-table-offset'))
  
  document.getElementById("playersOffset").innerHTML = offset
  document.getElementById("playersLength").innerHTML = offset + tableSize

  fetch('http://localhost:8080/rest/resources/players?offset='+offset+'&length='+tableSize).then(function(response) {
  return response.json();
  }).then(function(data) {
    
    // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    var tableBody = document.querySelector('#playersTableBody');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
    
    data.players.forEach(function(player){
      var t = document.querySelector("#playerRecord").cloneNode(true)   
      var template = t.content
  
      var firstNameElement = template.querySelector("#firstName");
      firstNameElement.innerHTML = player.person.firstName;
      firstNameElement.setAttribute("id", "firstName"+player.id)
      firstNameElement.onclick = function(e){ showSelectedPlayerDetails(player.id); }
    
      var lastNameElement = template.querySelector("#lastName"); 
      lastNameElement.innerHTML = player.person.lastName;
      lastNameElement.setAttribute("id", "lasttName"+player.id)
    
      tableBody.appendChild(template);
    });
  }).catch(function(err) {
  });
}

function next(){
  var playersCount = Number(document.getElementById("playersCount").innerHTML)
  var currOffset = Number(localStorage.getItem('players-table-offset'))
  var newOffset = currOffset + tableSize
	  
  if(newOffset>=playersCount){
    return;
  }
	  
  document.getElementById("playersOffset").innerHTML = newOffset
  localStorage.setItem('players-table-offset', newOffset)
  showPlayersTable();
}

function prev(){
  var currOffset = Number(localStorage.getItem('players-table-offset'))
  var newOffset = currOffset - tableSize
	  
  if(newOffset<0){
    newOffset = 0;
  }
	  
  document.getElementById("playersOffset").innerHTML = newOffset
  document.getElementById("playersLength").innerHTML = newOffset + tableSize
  localStorage.setItem('players-table-offset', newOffset)
  showPlayersTable();
}

//
//show selected players details
//
function showSelectedPlayerDetails(id){
fetch('http://localhost:8080/rest/resources/players/'+id).then(function(response) {
 return response.json();
}).then(function(player) {
 
 localStorage.setItem('players-current-player-id', player.id)
 localStorage.setItem('players-current-player-person-id', player.person.id)

 document.getElementById("pdFirstName").setAttribute('value', player.person.firstName)
 document.getElementById("pdLastName").setAttribute('value', player.person.lastName)
 document.getElementById("pdDateOfBirth").setAttribute('value', player.person.dateOfBirthISO)
 
 var genderElement = document.getElementById("pdGender");
 selectItemByValue(genderElement, player.person.gender);

}).catch(function(err) {
});
}