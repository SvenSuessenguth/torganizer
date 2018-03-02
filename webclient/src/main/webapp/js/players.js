/* global playersResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Players {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.updatePlayersTable();
    this.cancel();
  }

  // ---------------------------------------------------------------------------
  //
  // decide wether to create or to update a player
  //
  // ---------------------------------------------------------------------------
  createOrUpdate(){
    if(sessionStorage.getItem('players-current-player-id')===null){
      this.create();      
    }else{
      this.update();
    }
  }

  // ---------------------------------------------------------------------------
  //
  // create new player
  //
  // ---------------------------------------------------------------------------
  create(){
    var json = this.inputToJSon();
    playersResource.createOrUpdate(json, "POST", this.createResolve, this.createReject);
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
    players.updatePlayersTable();
    players.cancel();
  }
  addPlayerReject(json){console.log("failure adding player to tournament");}

  // ---------------------------------------------------------------------------
  //
  // update existing player
  //
  // ---------------------------------------------------------------------------
  update() {
    var json = this.inputToJSon();
    playersResource.createOrUpdate(json, "PUT", this.updateResolve, this.updateReject);    
  }
  updateResolve(json){
    players.updatePlayersTable();
    players.cancel();
  }
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
  deleteResolve(json){
    players.updatePlayersTable();
    players.cancel();
  }
  deleteReject(json){}

  // ---------------------------------------------------------------------------
  //
  // convert form data to/from json and cancel (clear the form
  //
  // ---------------------------------------------------------------------------
  inputToJSon(){
    var genderElement = document.getElementById("gender");
    var statusElement = document.getElementById("status");
    
    var json = {
      "id": sessionStorage.getItem('players-current-player-id'),
      "status": statusElement.options[statusElement.selectedIndex].value,
      "person":{
        "id": sessionStorage.getItem('players-current-player-person-id'),
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

    document.getElementById("first-name").value = json.person.firstName;
    document.getElementById("last-name").value = json.person.lastName;
    document.getElementById("date-of-birth").value = json.person.dateOfBirth;
      
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, json.person.gender);
    
    var statusElement = document.getElementById("status");
    selectItemByValue(statusElement, json.status);
  }

  cancel(){
    var currentPlayerId = sessionStorage.getItem('players-current-player-id');
    console.log("players-current-player-id to cancel : "+currentPlayerId);
    if(currentPlayerId!==null){
      sessionStorage.removeItem('players-current-player-id');
    }
    var currentPersonId = sessionStorage.getItem('players-current-player-person-id'); 
    if(currentPersonId!== null){
      sessionStorage.removeItem('players-current-player-person-id');
    }

    document.getElementById("first-name").value = '';
    document.getElementById("last-name").value = '';
    document.getElementById("date-of-birth").valueAsDate = null;
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");
    var statusElement = document.getElementById("status");
    selectItemByValue(statusElement, "ACTIVE");
    document.getElementById("first-name").focus();
  }
  
  // ---------------------------------------------------------------------------
  //
  // update the table with loaded data and hader above the table
  //
  // ---------------------------------------------------------------------------
  updatePlayersTable() {
    var offset = Number(sessionStorage.getItem('players.players-table.offset'));
    var tournamentId = tournaments.getCurrentTournamentId();
    
    document.getElementById("players-offset").innerHTML = offset;
    document.getElementById("players-length").innerHTML = offset + tableSize;

    tournamentsResource.getPlayers(tournamentId, offset, tableSize, this.updatePlayersTableResolve, this.updatePlayersTableReject);
    tournamentsResource.countPlayers(tournamentId, this.countPlayersTableResolve, this.countPlayersTableReject);
  }  
  updatePlayersTableResolve(json){ players.updatePlayersTableInternal(json); }
  updatePlayersTableReject(json){ console.log(json); }
  countPlayersTableResolve(json){ document.getElementById("players-count").innerHTML = json; }
  countPlayersTableReject(json){ console.log(json); }
  
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
     
      var playerRow = template.querySelector("#player-row");
      playerRow.onclick = function(e){ new Players().showDetails(player.id); };
      
      var firstNameElement = template.querySelector("#first-name");
      firstNameElement.innerHTML = player.person.firstName;
      firstNameElement.setAttribute("id", "first-name"+player.id);      
      
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
    playersResource.readOrDelete(id, "GET", this.inputFromJson, this.showDetailsReject);
  }  
  showDetailsReject(json){}
}

var players = new Players();
