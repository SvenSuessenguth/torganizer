/* global playersResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Players {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.updatePlayersTable();
  }

  create(){
    var json = this.inputToJSon();
    playersResource.create(json, this.createSuccess, this.createFailure);
  }
  createSuccess(json){
    var tournamentId = tournaments.getCurrentTournamentId();
    // this can't be used inside a promise
    // https://stackoverflow.com/questions/32547735/javascript-promises-how-to-access-variable-this-inside-a-then-scope
    tournamentsResource.addPlayer(tournamentId, json.id, players.addPlayerSuccess, players.addPlayerFailure);
    window.location.reload(true);
  }
  createFailure(json){}
  addPlayerSuccess(json){console.log("successfully added player to tournament");}
  addPlayerFailure(json){console.log("failure adding player to tournament");}

  update() {
    var json = this.inputToJSon();
    playersResource.update(json, this.updateSuccess, this.updateFailure);
    window.location.reload(true);
  }
  updateSuccess(json){}
  updateSuccessFailure(json){}

  inputToJSon(){
    var playerId= sessionStorage.getItem('players-current-player-id');
    var personId = sessionStorage.getItem('players-current-player-person-id');
    var firstName = document.getElementById("pdFirstName").value;
    var lastName = document.getElementById("pdLastName").value;
    var dateOfBirth = document.getElementById("pdDateOfBirth").value;
    
    var genderElement = document.getElementById("pdGender");
    var gender = genderElement.options[genderElement.selectedIndex].value;
    
    var statusElement = document.getElementById("pdStatus");
    var status = statusElement.options[statusElement.selectedIndex].value;
    
    var json = JSON.stringify({
      "id": playerId,
      "status": status,
      "person":{
        "id": personId,
        "firstName": firstName,
        "lastName": lastName,
        "dateOfBirth": dateOfBirth,
        "gender": gender
      }
    });
    
    console.log("player: "+json);
    return json;
  }

  updatePlayersTable(){
    var offset = Number(sessionStorage.getItem('players-table-offset'));
    var tournamentId = tournaments.getCurrentTournamentId();
    
    document.getElementById("playersOffset").innerHTML = offset;
    document.getElementById("playersLength").innerHTML = offset + tableSize;

    tournamentsResource.getPlayers(tournamentId, offset, tableSize, this.getPlayersResolve, this.getPlayersReject);
  }
  // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
  // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
  getPlayersResolve(json){
    var tableBody = document.querySelector('#playersTableBody');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
      
    // daten in die tabelle einfuegen
    json.forEach(function(player){
      var t = document.querySelector("#playerRecord").cloneNode(true);
      var template = t.content;
    
      var firstNameElement = template.querySelector("#firstName");
      firstNameElement.innerHTML = player.person.firstName;
      firstNameElement.setAttribute("id", "firstName"+player.id);
      firstNameElement.onclick = function(e){ new Players().showDetails(player.id); };
      
      var lastNameElement = template.querySelector("#lastName"); 
      lastNameElement.innerHTML = player.person.lastName;
      lastNameElement.setAttribute("id", "lasttName"+player.id);
      
      tableBody.appendChild(template);
    });
  }
  getPlayersReject(json){}

  next(){
    var playersCount = Number(document.getElementById("playersCount").innerHTML);
    var currOffset = Number(sessionStorage.getItem('players-table-offset'));
    var newOffset = currOffset + tableSize;
      
    if(newOffset>=playersCount){
      return;
    }
      
    document.getElementById("playersOffset").innerHTML = newOffset;
    sessionStorage.setItem('players-table-offset', newOffset);
    this.updatePlayersTable();
  }

  prev(){
    var currOffset = Number(sessionStorage.getItem('players-table-offset'));
    var newOffset = currOffset - tableSize;
      
    if(newOffset<0){
      newOffset = 0;
    }
      
    document.getElementById("playersOffset").innerHTML = newOffset;
    document.getElementById("playersLength").innerHTML = newOffset + tableSize;
    sessionStorage.setItem('players-table-offset', newOffset);
    this.updatePlayersTable();
  }

  //
  //show selected players details
  //
  showDetails(id) {
    playersResource.readSingle(id, this.showDetailsSuccess, this.showDetailsFailure);
  }
  showDetailsSuccess(json){
    sessionStorage.setItem('players-current-player-id', json.id);
    sessionStorage.setItem('players-current-player-person-id', json.person.id);

    document.getElementById("pdFirstName").setAttribute('value', json.person.firstName);
    document.getElementById("pdLastName").setAttribute('value', json.person.lastName);
    document.getElementById("pdDateOfBirth").setAttribute('value', json.person.dateOfBirth);
      
    var genderElement = document.getElementById("pdGender");
    selectItemByValue(genderElement, json.person.gender);
  }
  showDetailsFailure(json){}
}

var players = new Players();
