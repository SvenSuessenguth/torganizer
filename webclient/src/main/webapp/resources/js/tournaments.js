/* global fetch, tournamentsResource */

class Tournaments {
  constructor() {
  }
  
  onload(){
    this.updateTournamentsTable();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for tournaments table
  //
  //--------------------------------------------------------------------------------------------------------------------
  updateTournamentsTable(){
    tournamentsResource.readMultiple(0, 100, this.updateTournamentsTableResolve, this.updateTournamentsTableReject);
  }
  updateTournamentsTableResolve(data){
    document.getElementById("tournamentsCount").innerHTML=data.length;
    let tableBody = document.querySelector('#tournamentsTableBody');

    data.forEach(function(tournament){
      let t = document.querySelector("#tournamentRecord").cloneNode(true);
      let template = t.content;
  
      let nameElement = template.querySelector("#name");
      nameElement.innerHTML = tournament.name;
      nameElement.setAttribute("id", "tournament-"+tournament.id);
      nameElement.onclick = function(e){ tournaments.tournamentSelected(tournament.id); };
        
      tableBody.appendChild(template);
    });
  }
  updateTournamentsTableReject(data){console.log(data);}
  
  tournamentSelected(id) {
    tournamentsResource.readSingle(id, this.showTournamentDetailsResolve, this.showTournamentDetailsReject);
  }
  showTournamentDetailsResolve(data){
    // {"id":1,"name":"dings"}
    document.getElementById("tournamentName").value = data.name;
    sessionStorage.setItem('tournaments-current-tournament-id', data.id);
    sessionStorage.setItem('tournaments-current-tournament-name', data.name);
    menue.update();
  }
  showTournamentDetailsReject(err){
  }


  save(){
    let json = this.formToJSon();
    let tournamentId = this.getCurrentTournamentId();
    let method;
    
    if(tournamentId===null){ method = "POST"; }
    else{ method = "PUT"; }
    
    tournamentsResource.createOrUpdate(json, method, this.createResolve, this.createReject);
  }
  
  createResolve(json){
    sessionStorage.setItem('tournaments-current-tournament-id', json.id);
    sessionStorage.setItem('tournaments-current-tournament-name', json.name);
    window.location.reload(true);
    menue.update();
  }
  createReject(json){}

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel(){
    sessionStorage.removeItem('tournaments-current-tournament-id');
    sessionStorage.removeItem('tournaments-current-tournament-name');
    document.getElementById("tournamentName").value = "";
    document.getElementById("tournamentName").focus();
    menue.update();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // convert to/from json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToJSon(){
    let id= tournaments.getCurrentTournamentId();
    let name = document.getElementById("tournamentName").value;

  return {
      "id": id,
      "name": name
    };
  }

  getCurrentTournamentId(){
    let tournamentId = sessionStorage.getItem('tournaments-current-tournament-id');

    // do not convert NULL to '0'
    if(tournamentId !== null){
      return Number(sessionStorage.getItem('tournaments-current-tournament-id'));
    }
    return tournamentId;
  }
  isActiveTournament(){
    return this.getCurrentTournamentId() !== null;
  }
}

let tournaments = new Tournaments();
