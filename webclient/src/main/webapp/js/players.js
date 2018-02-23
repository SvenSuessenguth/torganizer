/* global playersResource, tournamentsResource, tournaments */

var tableSize = Number(3);

class Players {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.updatePlayersTable();
  }

  // ---------------------------------------------------------------------------
  //
  // create new player
  //
  // ---------------------------------------------------------------------------
  create(){
    var json = this.inputToJSon();
    playersResource.create(json, this.createResolve, this.createReject);
  }
  createResolve(json){
    var tournamentId = tournaments.getCurrentTournamentId();
    // this can't be used inside a promise
    // https://stackoverflow.com/questions/32547735/javascript-promises-how-to-access-variable-this-inside-a-then-scope
    tournamentsResource.addPlayer(tournamentId, json.id, players.addPlayerResolve, players.addPlayerReject);    
  }
  createReject(json){}
  addPlayerResolve(json){
    console.log("successfully added player to tournament");
    this.updatePlayersTable();
  }
  addPlayerReject(json){console.log("failure adding player to tournament");}

  // ---------------------------------------------------------------------------
  //
  // update existing player
  //
  // ---------------------------------------------------------------------------
  update() {
    var json = this.inputToJSon();
    playersResource.update(json, this.updateResolve, this.updateReject);    
  }
  updateResolve(json){players.updatePlayersTable();}
  updateReject(json){}
  
  // ---------------------------------------------------------------------------
  //
  // delete existing player
  //
  // ---------------------------------------------------------------------------
  delete(){
    var player = this.inputToJSon();
    var tournamentId = tournaments.getCurrentTournamentId();
    tournamentsResource.removePlayer(tournamentId, player.id, this.deleteResolve, this.deleteReject);    
  }
  deleteResolve(json){players.updatePlayersTable();}
  deleteReject(json){}

  // ---------------------------------------------------------------------------
  //
  // clear and convert form data to/from json
  //
  // ---------------------------------------------------------------------------
  inputToJSon(){
    var genderElement = document.getElementById("gender");
    var statusElement = document.getElementById("status");
    
    var json = {
      "id": sessionStorage.getItem('players-current-player-id'),
      "status": statusElement.options[statusElement.selectedIndex].value,
      "person":{
        "id": sessionStorage.getItem('players.current-player.person.id'),
        "firstName": document.getElementById("first-name").value,
        "lastName": document.getElementById("last-name").value,
        "dateOfBirth": document.getElementById("date-of-birth").value,
        "gender": genderElement.options[genderElement.selectedIndex].value
      }
    };
    
    console.log("player: "+json);
    return json;
  }
  
  inputFromJson(json){
    sessionStorage.setItem('players-current-player-id', json.id);
    sessionStorage.setItem('players-current-player-person-id', json.person.id);

    document.getElementById("first-name").setAttribute('value', json.person.firstName);
    document.getElementById("last-name").setAttribute('value', json.person.lastName);
    document.getElementById("date-of-birth").setAttribute('value', json.person.dateOfBirth);
      
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, json.person.gender);
  }
  
  inputCancel(){
    sessionStorage.setItem('players-current-player-id', null);
    sessionStorage.setItem('players-current-player-person-id', null);

    document.getElementById("first-name").setAttribute('value', "");
    document.getElementById("last-name").setAttribute('value', "");
    document.getElementById("date-of-birth").setAttribute('value', null);      
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");
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

  // ---------------------------------------------------------------------------
  //
  // show selected player
  //
  // ---------------------------------------------------------------------------
  showDetails(id) {
    playersResource.readSingle(id, this.showDetailsResolve, this.showDetailsReject);
  }
  showDetailsResolve(json){
    sessionStorage.setItem('players-current-player-id', json.id);
    sessionStorage.setItem('players-current-player-person-id', json.person.id);

    document.getElementById("first-name").setAttribute('value', json.person.firstName);
    document.getElementById("last-name").setAttribute('value', json.person.lastName);
    document.getElementById("date-of-birth").setAttribute('value', json.person.dateOfBirth);
      
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, json.person.gender);
  }
  showDetailsReject(json){}
}

var players = new Players();
