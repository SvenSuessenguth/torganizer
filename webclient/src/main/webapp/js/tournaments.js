/* global fetch, tournamentsResource */

class Tournaments {
  constructor() {
  }
  
  onload(){
    includeFragments();
    this.showTournamentsTable();    
  }
 
  getCurrentTournamentId(){
    return Number(sessionStorage.getItem('tournaments-current-tournament-id'));
  }
  isActiveTournament(){
    return this.getCurrentTournamentId() !== null;
  }
  
  showTournamentsTable(){
    tournamentsResource.readMultiple(0, 100, this.showTournamentsTableSuccess, this.showTournamentsTableFailure);
  }
  showTournamentsTableSuccess(data){    
    document.getElementById("tournamentsCount").innerHTML=data.length;
    var tableBody = document.querySelector('#tournamentsTableBody');
    
    data.forEach(function(tournament){
      var t = document.querySelector("#tournamentRecord").cloneNode(true);
      var template = t.content;
  
      var nameElement = template.querySelector("#name");
      nameElement.innerHTML = tournament.name;
      nameElement.setAttribute("id", "tournament-"+tournament.id);
      nameElement.onclick = function(e){ tournaments.showTournamentDetails(tournament.id); };
        
      tableBody.appendChild(template);
    });
  }
  showTournamentsTableFailure(data){console.log(data);}
  
  showTournamentDetails(id) {
    fetch('http://localhost:8080/rest/resources/tournaments/'+id).then(function(response) {
      return response.json();
    }).then(function(data) {
      // {"id":1,"name":"dings"}
      document.getElementById("tournamentName").setAttribute('value', data.name);
      sessionStorage.setItem('tournaments-current-tournament-id', data.id);
      sessionStorage.setItem('tournaments-current-tournament-name', data.name);
    }).catch(function(err) {
    });
  }
  
  save(){
    var json = this.inputToJSon();
    var tournamentId = this.getCurrentTournamentId();
    var method = "UNKNOWN";
    
    if(tournamentId===null){ method = "POST"; }
    else{ method = "PUT"; }
    
    tournamentsResource.createOrUpdate(json, method, this.createSuccess, this.createFailure);
  }
  
  createSuccess(json){
    sessionStorage.setItem('tournaments-current-tournament-id', json.id);
    sessionStorage.setItem('tournaments-current-tournament-name', json.name);
    window.location.reload(true);
  }
  createFailure(json){}

  updateSuccess(json){
    sessionStorage.setItem('tournaments-current-tournament-id', json.id);
    sessionStorage.setItem('tournaments-current-tournament-name', json.name);
    window.location.reload(true);
  }
  updateFailure(json){}
  
  inputToJSon(){
    var id= tournaments.getCurrentTournamentId();
    var name = document.getElementById("tournamentName").value;
  
    var json = JSON.stringify({
      "id": Number(id),
      "name": name
    });  
    return json;  
  }
}

var tournaments = new Tournaments();
