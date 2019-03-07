/* global squadsResource, playersResource, tournamentsResource, tournaments */

let header = {

  onload : function onload(){
    header.update();
  },

  update : function update(){
    document.getElementById("tournamentName").innerText = sessionStorage.getItem("tournaments.tournament.name");
  },
};