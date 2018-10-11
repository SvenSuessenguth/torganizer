/* global squadsResource, playersResource, tournamentsResource, tournaments */

let squads = {

  onLoad : function onLoad(){
    squads.initSquads();
    squads.initAllPlayers();
    squads.cancel();
  },


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for all-squads-table
  //
  //--------------------------------------------------------------------------------------------------------------------
  initSquads : function initSquads(){
    let allSquadsLength = document.getElementById("all-squads-table").getAttribute("rows");
    let tournamentId = tournaments.getId();
    sessionStorage.setItem("squads.all-squads-offset", "0");

    squads.updateAllSquads(tournamentId, 0, allSquadsLength);
    
    document.getElementById("all-squads-table").addEventListener("opponent-selected", squads.squadSelectedFromAllSquads);
  },

  squadSelectedFromAllSquads : function squadSelectedFromAllSquads(event){
    restResourceAdapter.getSingle("squads", event.detail, squads.showSquadResolve);
  },
  showSquadResolve : function showSquadResolve(json){
    squads.squadToForm(json);
  },

  updateAllSquads : function updateAllSquads(tournamentId, offset, rows){
    tournamentsResource.getSquads(tournamentId, offset, rows, squads.updateAllSquadsResolve);
  },
  updateAllSquadsResolve : function updateAllSquadsResolve(json){
    document.getElementById("all-squads-table").setAttribute("data", JSON.stringify(json));
  },

  prevAllSquads : function prevAllSquads(){
    let allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset - allSquadsRows;
    if(allSquadsOffset <0){
      allSquadsOffset = 0;
    }
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);

    squads.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  },
  nextAllSquads : function nextAllSquads(){
    let allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset + allSquadsRows;
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);

    squads.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for all-players-table
  //
  //--------------------------------------------------------------------------------------------------------------------

  initAllPlayers : function initAllPlayers(){
    let allPlayersLength = document.getElementById("all-players-table").getAttribute("rows");
    let allPlayersTournamentId = tournaments.getId();
    sessionStorage.setItem("squads.all-players-offset", 0);

    squads.updateAllPlayers(allPlayersTournamentId, 0, allPlayersLength);
    
    document.getElementById("all-players-table").addEventListener("opponent-selected", squads.playerSelectedFromAllPlayer);
  },

  playerSelectedFromAllPlayer : function playerSelectedFromAllPlayer(event){
    restResourceAdapter.getSingle("players", event.detail, squads.addPlayerToSquadResolve);
  },
  addPlayerToSquadResolve : function addPlayerToSquadResolve(json){
    let retrievedData = sessionStorage.getItem("squads.selected-players-table");
    let playersInSquad;
    if(retrievedData!==null && retrievedData.length!==0){
      playersInSquad = JSON.parse(retrievedData);
    }else{
      playersInSquad = [];
    }
    if(playersInSquad.length===2){
      return;
    }
    
    playersInSquad.push(json);
    sessionStorage.setItem("squads.selected-players-table", JSON.stringify(playersInSquad));
    squads.initSquad();
  },
  addPlayerToSquadReject : function addPlayerToSquadReject(json){
    console.log("add player to squad with failure");
  },

  prevAllPlayers : function prevAllPlayers(){
    let allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    let allPlayersTournamentId = tournaments.getId();
    let allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset - allPlayersLength;
    if(allPlayersOffset<0){
      allPlayersOffset = 0;
    }
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);

    squads.updateAllPlayers(allPlayersTournamentId, allPlayersOffset, allPlayersLength);
  },
  nextAllPlayers : function  nextAllPlayers(){
    let allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset + allPlayersLength;    
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);

    squads.updateAllPlayers(tournamentId, allPlayersOffset, allPlayersLength);
  },

  updateAllPlayers : function updateAllPlayers(tournamentId, offset, rows){
    tournamentsResource.getPlayers(tournamentId, offset, rows, squads.updateAllPlayersResolve, squads.updateAllPlayersReject);
  },
  updateAllPlayersResolve : function updateAllPlayersResolve(json){
    // if json-array is empty and offset > 0, we are one step too far
    let allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    let allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    if(json.length===0 && allPlayersOffset>0){
      sessionStorage.setItem("squads.all-players-offset", allPlayersOffset-allPlayersLength);
      return;
    }

    // update custom element with data
    let playersTable = document.getElementById("all-players-table");
    playersTable.setAttribute("data", JSON.stringify(json));
  },
  updateAllPlayersReject : function updateAllPlayersReject(json){ },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // init squad
  //
  //--------------------------------------------------------------------------------------------------------------------
  initSquad : function initSquad(){
    let sessionStorageData = sessionStorage.getItem("squads.selected-players-table");
    let selectedPlayersTable = document.getElementById("selected-players-table");
    
    selectedPlayersTable.setAttribute("data", sessionStorageData);
    document.getElementById("selected-players-table").addEventListener("opponent-selected", squads.playerSelectedFromSelectedPlayer);
  },

  playerSelectedFromSelectedPlayer : function playerSelectedFromSelectedPlayer(event){
    let playerId = event.detail;
    let players = JSON.parse(sessionStorage.getItem("squads.selected-players-table"));

    players = players.filter(function(player) {
      return player.id !== playerId;
    });
    sessionStorage.setItem("squads.selected-players-table", JSON.stringify(players));

    console.log("remove player from selected squad "+event.detail);
    squads.initSquad();
  },

  cancel : function cancel(){
    sessionStorage.removeItem("squads.current-squad-id");
    sessionStorage.setItem("squads.selected-players-table", "[]");
    squads.initSquad();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // create or update the current squad
  //
  //--------------------------------------------------------------------------------------------------------------------
  save : function save(){
    let squad = squads.formToSquad();
    restResourceAdapter.createOrUpdate("squads", squad, squads.createResolve);
  },
  createResolve : function createResolve(json){
    let tournamentId = tournaments.getId();
    let squadId = json.id;
    tournamentsResource.addSquad(tournamentId, squadId, squads.addSquadResolve);
  },
  addSquadResolve : function addSquadResolve(json){
    let tournamentId = tournaments.getId();
    squads.updateAllSquads(tournamentId, 0, 10);
    squads.cancel();
  },

  update : function update(squad) {
    squadsResource.createOrUpdate(squad, "PUT", squads.updateResolve, squads.updateReject);
    squads.cancel();
  },
  updateResolve : function updateResolve(json){
    let tournamentId = tournaments.getId();
    squads.updateAllSquads(tournamentId, 0, 10);
    squads.cancel();
  },
  updateReject : function updateReject(error) { console.log(error); },
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (the squad-data in this case) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToSquad : function formToSquad(){
    let id = sessionStorage.getItem('squads.current-squad-id');
    if(id!==null){
      id = Number(id);
    }

    let json = {
      "id": id,
      "players": JSON.parse(sessionStorage.getItem("squads.selected-players-table"))
    };
    
    return json;
  },
  squadToForm : function squadToForm(squad){
    sessionStorage.setItem("squads.current-squad-id", squad.id);
    sessionStorage.setItem("squads.selected-players-table", JSON.stringify(squad.players));

    document.getElementById("selected-players-table").setAttribute("data", JSON.stringify(squad.players));
  },
}