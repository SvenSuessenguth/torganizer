/* global squadsResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Squads {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.initAllPlayersData();
  }
  
  initAllPlayersData(){
    var allPlayersDataLength = document.getElementById("all-players-table").getAttribute("rows");
    var allPlayersDataTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.allPlayersDataOffset", 0);    
    
    this.showAllPlayersData(allPlayersDataTournamentId, 0, allPlayersDataLength);
  }
  prevAllPlayersData(){
    var allPlayersDataLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersDataTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersDataOffset = Number(sessionStorage.getItem("squads.allPlayersDataOffset"));
    allPlayersDataOffset = allPlayersDataOffset - allPlayersDataLength;
    if(allPlayersDataOffset<0){
      allPlayersDataOffset = 0;
    }
    sessionStorage.setItem("squads.allPlayersDataOffset", allPlayersDataOffset);
    
    this.showAllPlayersData(allPlayersDataTournamentId, allPlayersDataOffset, allPlayersDataLength);
  }
  nextAllPlayersData(){
    var allPlayersDataLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersDataTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersDataOffset = Number(sessionStorage.getItem("squads.allPlayersDataOffset"));
    allPlayersDataOffset = allPlayersDataOffset + allPlayersDataLength;    
    sessionStorage.setItem("squads.allPlayersDataOffset", allPlayersDataOffset);
    
    this.showAllPlayersData(allPlayersDataTournamentId, allPlayersDataOffset, allPlayersDataLength);
  }
  
  showAllPlayersData(tournamentId, offset, rows){
    tournamentsResource.getPlayers(tournamentId, offset, rows, this.showAllPlayersDataSuccess, this.showAllPlayersDataFailure);
  }
  showAllPlayersDataSuccess(json){        
    var playersTable = document.getElementById("all-players-table");
    playersTable.setAttribute("data", JSON.stringify(json));        
  }  
  showAllPlayersDataFailure(json){
  }
  
  initSelectedPlayersData(){
  }
  initSelectedPlayersDataSuccess(json){    
  }
  initSelectedPlayersDataFailure(json){
  }
}

var squads = new Squads();
