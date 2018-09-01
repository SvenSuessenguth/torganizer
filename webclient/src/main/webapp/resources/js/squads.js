/* global squadsResource, playersResource, tournamentsResource, tournaments */

class Squads {
  constructor() {    
  }
  
  onLoad(){
    this.initSquads();
    this.initAllPlayers();
    this.cancel();
  }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for all-squads-table
  //
  //--------------------------------------------------------------------------------------------------------------------
  initSquads(){
    let allSquadsLength = document.getElementById("all-squads-table").getAttribute("rows");
    let tournamentId = tournaments.getId();
    sessionStorage.setItem("squads.all-squads-offset", "0");
    
    this.updateAllSquads(tournamentId, 0, allSquadsLength);
    
    document.getElementById("all-squads-table").addEventListener("opponent-selected", this.squadSelectedFromAllSquads);
  }

  squadSelectedFromAllSquads(event){
    squadsResource.readSingle(event.detail, squads.showSquadResolve, squads.showSquadReject);
  }
  showSquadResolve(json){
    squads.squadToForm(json);
  }
  showSquadReject(json){}

  updateAllSquads(tournamentId, offset, rows){
    tournamentsResource.getSquads(tournamentId, offset, rows, this.updateAllSquadsResolve, this.updateAllSquadsReject);
  }
  updateAllSquadsResolve(json){
    document.getElementById("all-squads-table").setAttribute("data", JSON.stringify(json));
  }
  updateAllSquadsReject(json){ }

  prevAllSquads(){
    let allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset - allSquadsRows;
    if(allSquadsOffset <0){
      allSquadsOffset = 0;
    }
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);

    this.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  }
  nextAllSquads(){
    let allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset + allSquadsRows;
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);

    this.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for all-players-table
  //
  //--------------------------------------------------------------------------------------------------------------------

  initAllPlayers(){
    let allPlayersLength = document.getElementById("all-players-table").getAttribute("rows");
    let allPlayersTournamentId = tournaments.getId();
    sessionStorage.setItem("squads.all-players-offset", 0);    
    
    this.updateAllPlayers(allPlayersTournamentId, 0, allPlayersLength);
    
    document.getElementById("all-players-table").addEventListener("opponent-selected", this.playerSelectedFromAllPlayer);
  }


  playerSelectedFromAllPlayer(event){    
    playersResource.readOrDelete(event.detail, "GET", squads.addPlayerToSquadResolve, squads.addPlayerToSquadReject);
  }
  addPlayerToSquadResolve(json){
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
  }
  addPlayerToSquadReject(json){
    console.log("add player to squad with failure");
  }

  prevAllPlayers(){
    let allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    let allPlayersTournamentId = tournaments.getId();
    let allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset - allPlayersLength;
    if(allPlayersOffset<0){
      allPlayersOffset = 0;
    }
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);
    
    this.updateAllPlayers(allPlayersTournamentId, allPlayersOffset, allPlayersLength);
  }
  nextAllPlayers(){
    let allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    let tournamentId = tournaments.getId();
    let allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset + allPlayersLength;    
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);
    
    this.updateAllPlayers(tournamentId, allPlayersOffset, allPlayersLength);
  }

  updateAllPlayers(tournamentId, offset, rows){
    tournamentsResource.getPlayers(tournamentId, offset, rows, this.updateAllPlayersResolve, this.updateAllPlayersReject);
  }
  updateAllPlayersResolve(json){
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
  }
  updateAllPlayersReject(json){ }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // init squad
  //
  //--------------------------------------------------------------------------------------------------------------------
  initSquad(){
    let sessionStorageData = sessionStorage.getItem("squads.selected-players-table");
    let selectedPlayersTable = document.getElementById("selected-players-table");
    
    selectedPlayersTable.setAttribute("data", sessionStorageData);
    document.getElementById("selected-players-table").addEventListener("opponent-selected", this.playerSelectedFromSelectedPlayer);
  }

  playerSelectedFromSelectedPlayer(event){
    let playerId = event.detail;
    let players = JSON.parse(sessionStorage.getItem("squads.selected-players-table"));

    players = players.filter(function(player) {
      return player.id !== playerId;
    });
    sessionStorage.setItem("squads.selected-players-table", JSON.stringify(players));

    console.log("remove player from selected squad "+event.detail);
    squads.initSquad();
  }

  cancel(){
    sessionStorage.removeItem("squads.current-squad-id");
    sessionStorage.setItem("squads.selected-players-table", "[]");
    this.initSquad();
  }

  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // create or update the current squad
  //
  //--------------------------------------------------------------------------------------------------------------------
  save(){
    let squad = this.formToSquad();
    if(squad===null || squad.id==='' || squad.id==='null' || squad.id===null) {
      this.create(squad);
    } else {
      this.update(squad);
    }
  }
  create(squad){
    squadsResource.createOrUpdate(squad, "POST", this.createResolve, this.createReject); 
  }
  createResolve(json){
    let tournamentId = tournaments.getId();
    let squadId = json.id;
    tournamentsResource.addSquad(tournamentId, squadId, squads.addSquadResolve, squads.addSquadReject);
  }
  createReject(error) { console.log(error); }
  addSquadResolve(json){
    let tournamentId = tournaments.getId();
    squads.updateAllSquads(tournamentId, 0, 10);
    squads.cancel();
  }
  addSquadReject(error){ console.log(error); }
  
  update(squad) {
    squadsResource.createOrUpdate(squad, "PUT", this.updateResolve, this.updateReject);
    squads.cancel();
  }
  updateResolve(json){
    let tournamentId = tournaments.getId();
    squads.updateAllSquads(tournamentId, 0, 10);
    squads.cancel();
  }
  updateReject(error) { console.log(error); }
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (the squad-data in this case) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToSquad(){
    let id = sessionStorage.getItem('squads.current-squad-id');
    if(id!==null){
      id = Number(id);
    }

    let json = {
      "id": id,
      "players": JSON.parse(sessionStorage.getItem("squads.selected-players-table"))
    };
    
    return json;
  }
  squadToForm(squad){
    sessionStorage.setItem("squads.current-squad-id", squad.id);
    sessionStorage.setItem("squads.selected-players-table", JSON.stringify(squad.players));

    document.getElementById("selected-players-table").setAttribute("data", JSON.stringify(squad.players));
  }

}

var squads = new Squads();