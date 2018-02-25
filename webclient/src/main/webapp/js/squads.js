/* global squadsResource, playersResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Squads {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.initAllPlayers();
    this.initSquads();
    this.cancel();
  }
  
  initSquads(){
    var allSquadsLength = document.getElementById("all-squads-table").getAttribute("rows");
    var allSquadsTournamentId = tournaments.getCurrentTournamentId();    
    sessionStorage.setItem("squads.all-squads-offset", 0);    
    
    this.updateAllSquads(allSquadsTournamentId, 0, allSquadsLength);
    
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
  
  prevAllPlayers(){
    var allPlayersLength = Number(document.getElementById("all-players-table").getAttribute("rows"));
    var allPlayersTournamentId = tournaments.getCurrentTournamentId();
    var allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
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
    var allPlayersOffset = Number(sessionStorage.getItem("squads.all-players-offset"));
    allPlayersOffset = allPlayersOffset + allPlayersLength;    
    sessionStorage.setItem("squads.all-players-offset", allPlayersOffset);
    
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
    tournamentsResource.getSquads(tournamentId, offset, rows, this.updateAllSquadsResolve, this.updateAllSquadsReject);
  }
  updateAllSquadsResolve(json){ }
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
    var squad = this.inputToJSon();
    
    if(squad===null || squad.id==='' || squad.id==='null' || squad.id===null)
    {
      squadsResource.createOrUpdate(squad, "POST", this.createResolve, this.createReject);
    }
    else{ this.update(); }
  }
  createResolve(json){ console.log("create resolved squad"); }
  createReject(json) { console.log("create rejected squad"); }
  updateResolve(json){ console.log("udpate resolved squad"); }
  updateReject(json) { console.log("update rejected squad"); }
  
  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (the squad-data in this case) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  inputToJSon(){    
    var json = {
      "id": sessionStorage.getItem('squads.current-squad-id'),
      "players": JSON.parse(sessionStorage.getItem("squads.selected-players-table"))
    };
    
    console.log("squad: "+json);
    return json;
  }
}

var squads = new Squads();