/* global squadsResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Squads {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
  }
  
  initAllPlayersData(){
    tournamentsResource.getPlayers(1, 0, 10, this.initAllPlayersDataSuccess, this.initAllPlayersDataFailure);
  }
  
  initAllPlayersDataSuccess(json){        
    var playersTable = document.getElementById("all-players-table");
    playersTable.setAttribute("data", JSON.stringify(json));        
  }
  
  initAllPlayersDataFailure(json){
    console.log("getPlayersFailure");
    console.log(JSON.stringify(json));
  }
  
  initSelectedPlayersData(){
  }
  initSelectedPlayersDataSuccess(json){    
  }
  initSelectedPlayersDataFailure(json){
  }
}

var squads = new Squads();
