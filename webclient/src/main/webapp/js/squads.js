/* global squadsResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Squads {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.initAllPlayers();
  }
  
  initAllPlayers(){
    var allPlayersLength = document.getElementById("all-players-table").getAttribute("rows");
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.allPlayersOffset", 0);    
    
    this.updateAllPlayers(allPlayersTournamentId, 0, allPlayersLength);
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
