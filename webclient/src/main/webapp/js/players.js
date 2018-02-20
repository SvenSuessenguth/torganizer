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
    var personId = sessionStorage.getItem('players.current-player.person.id');
    var firstName = document.getElementById("first-name").value;
    var lastName = document.getElementById("last-name").value;
    var dateOfBirth = document.getElementById("date-of-birth").value;
    
    var genderElement = document.getElementById("gender");
    var gender = genderElement.options[genderElement.selectedIndex].value;
    
    var statusElement = document.getElementById("status");
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

  updatePlayersTable() {
    var offset = Number(sessionStorage.getItem('players.players-table.offset'));
    var tournamentId = tournaments.getCurrentTournamentId();
    
    document.getElementById("players-offset").innerHTML = offset;
    document.getElementById("players-length").innerHTML = offset + tableSize;

    tournamentsResource.getPlayers(tournamentId, offset, tableSize, this.updatePlayersTableResolve, this.updatePlayersTableReject);
  }  
  updatePlayersTableResolve(json){ players.updatePlayersTableInternal(json); }
  updatePlayersTableReject(json){ console.log(json); }
  updatePlayersTableInternal(json){
    // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    var tableBody = document.querySelector('#players-table-body');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
      
    // daten in die tabelle einfuegen
    json.forEach(function(player){
      var t = document.querySelector("#player-record").cloneNode(true);
      var template = t.content;
    
      var firstNameElement = template.querySelector("#first-name");
      firstNameElement.innerHTML = player.person.firstName;
      firstNameElement.setAttribute("id", "first-name"+player.id);
      firstNameElement.onclick = function(e){ new Players().showDetails(player.id); };
      
      var lastNameElement = template.querySelector("#last-name"); 
      lastNameElement.innerHTML = player.person.lastName;
      lastNameElement.setAttribute("id", "last-name"+player.id);
      
      tableBody.appendChild(template);
    });
  }

  next(){
    var playersCount = Number(document.getElementById("players-count").innerHTML);
    var currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    var newOffset = currOffset + tableSize;
      
    if(newOffset>=playersCount){
      return;
    }
      
    document.getElementById("players-offset").innerHTML = newOffset;
    sessionStorage.setItem('players.players-table.offset', newOffset);
    this.updatePlayersTable();
  }

  prev(){
    var currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    var newOffset = currOffset - tableSize;
      
    if(newOffset<0){
      newOffset = 0;
    }
      
    document.getElementById("players-offset").innerHTML = newOffset;
    document.getElementById("players-length").innerHTML = newOffset + tableSize;
    sessionStorage.setItem('players.players-table.offset', newOffset);
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

    document.getElementById("first-name").setAttribute('value', json.person.firstName);
    document.getElementById("last-name").setAttribute('value', json.person.lastName);
    document.getElementById("date-of-birth").setAttribute('value', json.person.dateOfBirth);
      
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, json.person.gender);
  }
  showDetailsFailure(json){}
}

var players = new Players();
