/* global squadsResource, playersResource, tournamentsResource, tournaments */

let header = {

  onload : function onload(){
    menue.update();
  },

  update : function update(){
    document.getElementById("tournamentName").innerText = sessionStorage.getItem("tournaments.tournament.name");
  },
};