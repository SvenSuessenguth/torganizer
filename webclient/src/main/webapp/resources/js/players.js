/* global playersResource, tournamentsResource, tournaments */

/*
variables in sessions-storage:
------------------------------------------------------------------------------------------------------------------------

players.player.id
  id of the currently selected player

players.player.person.id
  id of the person associated with the currently selected player

players.player.club.id
  id of the club associated with the currently selected player

players.players-table.offset
  offset for getting players shown in the table of all players

 */

const defaultTableSize = Number(10);

var players = {
  onload : function onload(){
    players.updatePlayersTable();
    players.updateClubsSelectBox();
    players.cancel();

    document.getElementById("players").addEventListener("opponent-selected", players.showDetails);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save: function save(){

    let json = this.formToPlayer();

    if(sessionStorage.getItem('players.player.id')===null){
      this.create(json);
    }else{
      this.update(json);
    }
  },

  //--------------------------------------------------------------------------------------------------------------------
  create : function create(json){
    restResourceAdapter.createOrUpdate("players", json, players.createResolve);
  },
  createResolve: function createResolve(json){
    let tournamentId = tournaments.getId();
    // this can't be used inside a promise
    // https://stackoverflow.com/questions/32547735/javascript-promises-how-to-access-variable-this-inside-a-then-scope
    tournamentsResource.addPlayer(tournamentId, json.id, players.addPlayerResolve, restResourceAdapter.processMessages);
  },
  addPlayerResolve : function addPlayerResolve(json){
    players.updatePlayersTable();
    players.cancel();
  },
  //--------------------------------------------------------------------------------------------------------------------
  update : function update(json) {
    restResourceAdapter.createOrUpdate("players", json, players.updateResolve);
  },
  updateResolve : function updateResolve(json){
    players.updatePlayersTable();
    players.cancel();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // delete
  //
  //--------------------------------------------------------------------------------------------------------------------
  deletePlayer : function deletePlayer(){
    let player = this.formToPlayer();
    let tournamentId = tournaments.getId();
    tournamentsResource.removePlayer(tournamentId, player.id, players.deleteResolve, processMessages);
  },
  deleteResolve : function deleteResolve(json){
    players.updatePlayersTable();
    players.cancel();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel : function cancel(){
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
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // update selectbox for clubs
  //
  //--------------------------------------------------------------------------------------------------------------------
  updateClubsSelectBox : function updateClubsSelectBox(){
    restResourceAdapter.getMultiple("clubs", 0, 100, players.updateClubsSelectBoxResolve);
  },
  updateClubsSelectBoxResolve : function updateClubsSelectBoxResolve(json){
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
  },

  changeClub : function changeClub(){
    let dSelect = document.getElementById("clubs");
    let clubId = dSelect.options[dSelect.selectedIndex].value;

    sessionStorage.setItem('players.player.club.id', clubId);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // update the table of all players
  //
  //--------------------------------------------------------------------------------------------------------------------
  updatePlayersTable : function updatePlayersTable() {
    let offset = Number(sessionStorage.getItem('players.players-table.offset'));
    let tournamentId = tournaments.getId();

    document.getElementById("players-offset").innerHTML = offset;
    document.getElementById("players-length").innerHTML = offset + defaultTableSize;

    tournamentsResource.getPlayers(tournamentId, offset, defaultTableSize, players.updatePlayersTableResolve);
    tournamentsResource.countPlayers(tournamentId, players.countPlayersTableResolve);
  },

  updatePlayersTableResolve : function updatePlayersTableResolve(json){
    document.querySelector("#players").setAttribute("data", JSON.stringify(json));
  },
  countPlayersTableResolve : function countPlayersTableResolve(json){ document.getElementById("players-count").innerHTML = json; },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // navigating the table
  //
  //--------------------------------------------------------------------------------------------------------------------
  prev : function prev(){
    let currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    let newOffset = currOffset - defaultTableSize;

    if(newOffset<0){
      newOffset = 0;
    }

    document.getElementById("players-offset").innerHTML = newOffset.toString();
    document.getElementById("players-length").innerHTML = newOffset.toString() + defaultTableSize;
    sessionStorage.setItem('players.players-table.offset', newOffset.toString());
    players.updatePlayersTable();
  },

  next: function next(){
    let playersCount = Number(document.getElementById("players-count").innerHTML);
    let currOffset = Number(sessionStorage.getItem('players.players-table.offset'));
    let newOffset = currOffset + defaultTableSize;

    if(newOffset>=playersCount){
      return;
    }

    document.getElementById("players-offset").innerHTML = newOffset.toString();
    sessionStorage.setItem('players.players-table.offset', newOffset.toString());
    players.updatePlayersTable();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // show selected player
  //
  //--------------------------------------------------------------------------------------------------------------------
  showDetails : function showDetails(event) {
    restResourceAdapter.getSingle("players", event.detail, players.playerToForm);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToPlayer : function formToPlayer(){
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
  },

  playerToForm : function playerToForm(json){
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
  },
};