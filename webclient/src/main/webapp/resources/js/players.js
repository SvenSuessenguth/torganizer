/* global playersResource, tournamentsResource, tournaments */

let tableSize = Number(10);

window.onload = function(){
  players.onLoad();
}

class Players {
  constructor() {    
  }
  
  onLoad(){
    this.updatePlayersTable();
    this.updateClubsSelectBox();
    this.cancel();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  // ---------------------------------------------------------------------------
  save(){
    if(sessionStorage.getItem('players.player.id')===null){
      this.create();      
    }else{
      this.update();
    }
  }
  //--------------------------------------------------------------------------------------------------------------------
  create(){
    let json = this.formToPlayer();
    playersResource.createOrUpdate(json, "POST", this.createResolve, resourceReject);
  }
  createResolve(json){
    let tournamentId = tournaments.getCurrentTournamentId();
    // this can't be used inside a promise
    // https://stackoverflow.com/questions/32547735/javascript-promises-how-to-access-variable-this-inside-a-then-scope
    tournamentsResource.addPlayer(tournamentId, json.id, players.addPlayerResolve, resourceReject);
  }
  addPlayerResolve(json){
    players.updatePlayersTable();
    players.cancel();
  }
  //--------------------------------------------------------------------------------------------------------------------
  update() {
    let json = this.formToPlayer();
    playersResource.createOrUpdate(json, "PUT", this.updateResolve, resourceReject);
  }
  updateResolve(json){
    players.updatePlayersTable();
    players.cancel();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // delete
  //
  //--------------------------------------------------------------------------------------------------------------------
  delete(){
    let player = this.formToPlayer();
    let tournamentId = tournaments.getCurrentTournamentId();
    tournamentsResource.removePlayer(tournamentId, player.id, this.deleteResolve, resourceReject);
  }
  deleteResolve(json){
    players.updatePlayersTable();
    players.cancel();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel(){
    let currentPlayerId = sessionStorage.getItem('players.player.id');
    console.log("players.player.id to cancel : "+currentPlayerId);
    if(currentPlayerId!==null){
      sessionStorage.removeItem('players.player.id');
    }
    let currentPersonId = sessionStorage.getItem('players.player.person.id');
    if(currentPersonId!== null){
      sessionStorage.removeItem('players.player.person.id');
    }

    document.getElementById("first-name").value = '';
    document.getElementById("last-name").value = '';
    document.getElementById("date-of-birth").valueAsDate = null;
    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");
    let statusElement = document.getElementById("status");
    selectItemByValue(statusElement, "ACTIVE");
    document.getElementById("first-name").focus();

    let clubsElement = document.getElementById("clubs");
    sessionStorage.removeItem('players.player.club.id');
    selectItemByValue(clubsElement, "select");
  }

  // ---------------------------------------------------------------------------
  //
  // update selectbox for clubs
  //
  // ---------------------------------------------------------------------------
  updateClubsSelectBox(){
    clubsResource.readMultiple(0,100, players.updateClubsSelectBoxResolve, resourceReject);
  }
  updateClubsSelectBoxResolve(json){
    let dSelect = document.getElementById("clubs");
    let clubId = sessionStorage.getItem('players.club.id');

    // remove all optione before adding new ones
    while (dSelect.options.length > 0) {
      dSelect.remove(0);
    }

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "select";
    option.id= "select";
    if(clubId===null){
      option.selected = 'selected';
    }
    dSelect.appendChild(option);


    // add an option for every discipline
    json.forEach(function (club) {
      let option = document.createElement("option");
      option.text = club.name;
      option.value = club.id;
      option.id = club.id;
      dSelect.appendChild(option);

      if(clubId===club.id.toString()){
        option.selected = "selected";
      }
    });
  }

  changeClub(){
    let dSelect = document.getElementById("clubs");
    let clubId = dSelect.options[dSelect.selectedIndex].value;

    sessionStorage.setItem('players.player.club.id', clubId);
  }

  // ---------------------------------------------------------------------------
  //
  // update the table of all players
  //
  // ---------------------------------------------------------------------------
  updatePlayersTable() {
    let offset = Number(sessionStorage.getItem('players.players-table.offset'));
    let tournamentId = tournaments.getCurrentTournamentId();
    
    document.getElementById("players-offset").innerHTML = offset.toString();
    document.getElementById("players-length").innerHTML = offset.toString() + tableSize;

    tournamentsResource.getPlayers(tournamentId, offset, tableSize, this.updatePlayersTableResolve, resourceReject);
    tournamentsResource.countPlayers(tournamentId, this.countPlayersTableResolve, resourceReject);
  }  
  updatePlayersTableResolve(json){ players.updatePlayersTableInternal(json); }
  countPlayersTableResolve(json){ document.getElementById("players-count").innerHTML = json; }

  updatePlayersTableInternal(json){
    // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    let tableBody = document.querySelector('#players-table-body');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
      
    // daten in die tabelle einfuegen
    json.forEach(function(player){
      let t = document.querySelector("#player-record").cloneNode(true);
      let template = t.content;
     
      let playerRow = template.querySelector("#player-row");
      playerRow.onclick = function(){ new Players().showDetails(player.id); };
      
      let firstNameElement = template.querySelector("#first-name");
      firstNameElement.innerHTML = player.person.firstName;
      firstNameElement.setAttribute("id", "first-name"+player.id);      
      
      let lastNameElement = template.querySelector("#last-name");
      lastNameElement.innerHTML = player.person.lastName;
      lastNameElement.setAttribute("id", "last-name"+player.id);
      
      tableBody.appendChild(template);
    });
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // navigating the table
  //
  //--------------------------------------------------------------------------------------------------------------------
  prev(){
    let currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    let newOffset = currOffset - tableSize;
      
    if(newOffset<0){
      newOffset = 0;
    }
      
    document.getElementById("players-offset").innerHTML = newOffset.toString();
    document.getElementById("players-length").innerHTML = newOffset.toString() + tableSize;
    sessionStorage.setItem('players.players-table.offset', newOffset.toString());
    this.updatePlayersTable();
  }
  
  next(){
    let playersCount = Number(document.getElementById("players-count").innerHTML);
    let currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    let newOffset = currOffset + tableSize;
      
    if(newOffset>=playersCount){
      return;
    }
      
    document.getElementById("players-offset").innerHTML = newOffset.toString();
    sessionStorage.setItem('players.players-table.offset', newOffset.toString());
    this.updatePlayersTable();
  }

  // ---------------------------------------------------------------------------
  //
  // show selected player
  //
  // ---------------------------------------------------------------------------
  showDetails(id) {
    playersResource.readOrDelete(id, "GET", this.playerToForm, this.showDetailsReject);
  }  
  showDetailsReject(json){}
  
  //--------------------------------------------------------------------------------------------------------------------
  // 
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToPlayer(){
    let genderElement = document.getElementById("gender");
    let statusElement = document.getElementById("status");
    let playerId = sessionStorage.getItem('players.player.id');
    if(playerId !== null){
      playerId = Number(playerId);
    }
    let clubId =  sessionStorage.getItem('players.player.club.id');
    if(clubId !== null) {
      clubId = Number(clubId);
    }

    // association between club and player is done by clubId, threfore the name of the club is irrelevant and can be NULL
    return {
      "id": playerId,
      "status": statusElement.options[statusElement.selectedIndex].value,
      "person":{
        "id": sessionStorage.getItem('players.player.person.id'),
        "firstName": document.getElementById("first-name").value,
        "lastName": document.getElementById("last-name").value,
        "dateOfBirth": document.getElementById("date-of-birth").value,
        "gender": genderElement.options[genderElement.selectedIndex].value
      },
      "club":{
        "id":clubId,
        "name:":null
      }
    };
  }
  
  playerToForm(json){
    sessionStorage.setItem('players.player.id', json.id);
    sessionStorage.setItem('players.player.person.id', json.person.id);
    sessionStorage.setItem('players.player.club.id', json.club.id);

    document.getElementById("first-name").value = json.person.firstName;
    document.getElementById("last-name").value = json.person.lastName;
    document.getElementById("date-of-birth").value = json.person.dateOfBirth;
      
    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, json.person.gender);
    
    let statusElement = document.getElementById("status");
    selectItemByValue(statusElement, json.status);

    let clubElement = document.getElementById("clubs");
    let clubId = json.club.id;
    if(clubId===null){
      clubId = "select";
    }else{
      clubId = clubId.toString();
    }
    selectItemByValue(clubElement, clubId);
  }
}

let players = new Players();
