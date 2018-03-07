/* global squadsResource, playersResource, tournamentsResource, tournaments */

class Squads {
  constructor() {    
  }
  
  onLoad(){
    this.initAllPlayers();
    this.initSquads();
    this.cancel();
  }
  
  initSquads(){
    var allSquadsLength = document.getElementById("all-squads-table").getAttribute("rows");
    var tournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.all-squads-offset", 0);    
    
    this.updateAllSquads(tournamentId, 0, allSquadsLength);
    
    document.getElementById("all-squads-table").addEventListener("squad-selected", this.squadSelectedFromAllSquads);
  }
  
  initAllPlayers(){
    var allPlayersLength = document.getElementById("all-players-table").getAttribute("rows");
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.all-players-offset", 0);    
    
    this.updateAllPlayers(allPlayersTournamentId, 0, allPlayersLength);
    
    document.getElementById("all-players-table").addEventListener("player-selected", this.playerSelectedFromAllPlayer);
  }
  
  playerSelectedFromAllPlayer(event){    
    playersResource.readOrDelete(event.detail, "GET", squads.addPlayerToSquadResolve, squads.addPlayerToSquadReject);
  }
  addPlayerToSquadResolve(json){
    var retrievedData = sessionStorage.getItem("squads.selected-players-table");
    var playersInSquad;
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
    squads.updateSquad();
    
  }
  addPlayerToSquadReject(json){
    console.log("add player to squad with failure");
  }
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // navigating the tables
  //
  //--------------------------------------------------------------------------------------------------------------------
  prevAllPlayers(){
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset - allPlayersLength;
    if(allPlayersOffset<0){
      allPlayersOffset = 0;
    }
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);
    
    this.updateAllPlayers(allPlayersTournamentId, allPlayersOffset, allPlayersLength);
  }
  nextAllPlayers(){
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var tournamentId = tournaments.getCurrentTournamentId();
    var allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset + allPlayersLength;    
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);
    
    this.updateAllPlayers(tournamentId, allPlayersOffset, allPlayersLength);
  }
  prevAllSquads(){
    var allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    var tournamentId = tournaments.getCurrentTournamentId();
    var allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset - allSquadsRows;
    if(allSquadsOffset <0){
      allSquadsOffset = 0;
    }
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);
    
    this.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  }  
  nextAllSquads(){
    var allSquadsRows = Number(document.getElementById("all-squads-table").getAttribute("rows"));
    var tournamentId = tournaments.getCurrentTournamentId();
    var allSquadsOffset = Number(sessionStorage.getItem("squads.all-squads-offset"));
    allSquadsOffset = allSquadsOffset + allSquadsRows;
    sessionStorage.setItem("squads.all-squads-offset", allSquadsOffset);
    
    this.updateAllSquads(tournamentId, allSquadsOffset, allSquadsRows);
  }
  
  
  updateSquad(){
    var sessionStorageData = sessionStorage.getItem("squads.selected-players-table");
    var selectedPlayersTable = document.getElementById("selected-players-table");
    
    selectedPlayersTable.setAttribute("data", sessionStorageData);
  }
  
  cancel(){
    sessionStorage.setItem("squads.selected-players-table", "[]");
    this.updateSquad();
  }
  
  updateAllSquads(tournamentId, offset, rows){
    tournamentsResource.getSquads(tournamentId, offset, rows, this.updateAllSquadsResolve, this.updateAllSquadsReject);
  }
  updateAllSquadsResolve(json){ 
    document.getElementById("all-squads-table").setAttribute("data", JSON.stringify(json));
  }
  updateAllSquadsReject(json){ }
  
  updateAllPlayers(tournamentId, offset, rows){
    tournamentsResource.getPlayers(tournamentId, offset, rows, this.updateAllPlayersResolve, this.updateAllPlayersReject);
  }  
  updateAllPlayersResolve(json){
    // if json-array is empty and offset > 0, we are one step too far
    var allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    if(json.length===0 && allPlayersOffset>0){
      sessionStorage.setItem("squads.all-players-offset", allPlayersOffset-allPlayersLength);
      return;
    }
    
    // update custom element with data
    var playersTable = document.getElementById("all-players-table");
    playersTable.setAttribute("data", JSON.stringify(json));        
  }  
  updateAllPlayersReject(json){ }
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // create or update the current squad
  //
  //--------------------------------------------------------------------------------------------------------------------
  save(){
    var squad = this.formToSquad();
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
    var tournamentId = tournaments.getCurrentTournamentId();
    var squadId = json.id;
    tournamentsResource.addSquad(tournamentId, squadId, squads.addSquadResolve, squads.addSquadReject);
  }
  createReject(error) { console.log(error); }
  addSquadResolve(json){
    var tournamentId = tournaments.getCurrentTournamentId();
    squads.updateAllSquads(tournamentId, 0, 10);
    squads.cancel();
  }
  addSquadReject(error){ console.log(error); }
  
  update(squad) {
    squadsResource.createOrUpdate(squad, "PUT", this.updateResolve, this.updateReject);
    squads.canel();
  }
  updateResolve(json){ console.log("udpate resolved squad"); }
  updateReject(error) { console.log(error); }
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (the squad-data in this case) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToSquad(){    
    var json = {
      "id": sessionStorage.getItem('squads.current-squad-id'),
      "players": JSON.parse(sessionStorage.getItem("squads.selected-players-table"))
    };
    
    return json;
  }
}

var squads = new Squads();