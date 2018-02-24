/* global squadsResource, playersResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Squads {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.initAllPlayers();
    this.initSquads();
    this.updateSquad();
  }
  
  initSquads(){
    var allSquadsLength = document.getElementById("all-squads-table").getAttribute("rows");
    var allSquadsTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.allSquadsOffset", 0);    
    
    this.updateAllSquads(allSquadsTournamentId, 0, allSquadsLength);
    
    document.getElementById("all-squads-table").addEventListener("squad-selected", this.squadSelectedFromAllSquads);
  }
  
  initAllPlayers(){
    var allPlayersLength = document.getElementById("all-players-table").getAttribute("rows");
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.allPlayersOffset", 0);    
    
    this.updateAllPlayers(allPlayersTournamentId, 0, allPlayersLength);
    
    document.getElementById("all-players-table").addEventListener("player-selected", this.playerSelectedFromAllPlayer);
  }
  
  playerSelectedFromAllPlayer(event){    
    playersResource.readOrDelete(event.detail, "GET", squads.addPlayerToSquadSuccess, squads.addPlayerToSquadFailure);
  }
  addPlayerToSquadSuccess(json){
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
  addPlayerToSquadFailure(json){
    console.log("add player to squad with failure");
  }
  
  prevAllPlayers(){
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersOffset = Number(sessionStorage.getItem("squads.allPlayersOffset"));
    allPlayersOffset = allPlayersOffset - allPlayersLength;
    if(allPlayersOffset<0){
      allPlayersOffset = 0;
    }
    sessionStorage.setItem("squads.allPlayersOffset", allPlayersOffset);
    
    this.updateAllPlayers(allPlayersTournamentId, allPlayersOffset, allPlayersLength);
  }
  nextAllPlayers(){
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersOffset = Number(sessionStorage.getItem("squads.allPlayersOffset"));
    allPlayersOffset = allPlayersOffset + allPlayersLength;    
    sessionStorage.setItem("squads.allPlayersOffset", allPlayersOffset);
    
    this.updateAllPlayers(allPlayersTournamentId, allPlayersOffset, allPlayersLength);
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
    tournamentsResource.getSquads(tournamentId, offset, rows, this.updateAllSquadsSuccess, this.updateAllSquadsFailure);
  }
  updateAllSquadsSuccess(json){
    console.log("updateAllSquadsSuccess");
  }
  updateAllSquadsFailure(json){
  }
  
  updateAllPlayers(tournamentId, offset, rows){
    tournamentsResource.getPlayers(tournamentId, offset, rows, this.updateAllPlayersSuccess, this.updateAllPlayersFailure);
  }
  
  updateAllPlayersSuccess(json){
    // if json-array is empty and offset > 0, we are one step too far
    var allPlayersOffset = Number(sessionStorage.getItem("squads.allPlayersOffset"));
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    if(json.length===0 && allPlayersOffset>0){
      sessionStorage.setItem("squads.allPlayersOffset", allPlayersOffset-allPlayersLength);
      return;
    }
    
    // update custom element with data
    var playersTable = document.getElementById("all-players-table");
    playersTable.setAttribute("data", JSON.stringify(json));        
  }  
  updateAllPlayersFailure(json){
  }
}

var squads = new Squads();