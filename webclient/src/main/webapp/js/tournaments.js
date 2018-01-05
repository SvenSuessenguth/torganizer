/* global fetch, tournamentResource */

class Tournaments {
  constructor() {
  }
  
  onload(){
    includeFragments();
    this.showTournamentsTable();    
  }
 
  getCurrentTournamentId(){
    return sessionStorage.getItem('tournaments-current-tournament-id');
  }
  isActiveTournament(){
    return this.getCurrentTournamentId() !== null;
  }
  
  showTournamentsTable(){
    tournamentResource.readMultiple(0,100,this.showTournamentsTableSuccess, this.showTournamentsTableFailure);
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

  createTournament(){
	
    if(!isFormValid("tournamentsForm")){
      return;
    }
	
    fetch('http://localhost:8080/rest/resources/tournaments',{
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: tournamentDataToJSon()
    })
    .then(function(response){return response.json();})
    .then(function(json) {
      sessionStorage.setItem('tournaments-current-tournament-id', json.id);
      sessionStorage.setItem('tournaments-current-tournament-name', json.name);
    }).catch(function(err) {
    });

    // neuladen des gesamten Dokumentes nach einem 'post'
    window.location.reload(true);
  }

  addPlayerToCurrentTournament(playerId) {
    var tournamentId = sessionStorage.getItem('tournaments-current-tournament-id');
    
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers/'+playerId,{
      method: "POST",
      headers: {
        'Accept': 'application/json',
      }
    }).then(function(response) {
      return response.json();
    }).then(function(count) {
      console.log("currently "+count+" subscribers for tournament "+tournamentId);    
    }).catch(function(err) {
    });
  }
  
  getCurrentSubscribers(offset, length) {
    var tournamentId = Number(sessionStorage.getItem('tournaments-current-tournament-id'));
    
    var subscribers = fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers?offset='+offset+'&length='+length)
    .then(function(response) {
      return response.json();
    });
    
    return subscribers;
  }
  
  countCurrentSubscribers() {
    var tournamentId = Number(sessionStorage.getItem('tournaments-current-tournament-id'));
    
    var count = fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers/count').then(function(response) {
      return response.json();
    });
    
    return count;
  }
}

function tournamentDataToJSon(){
  var id= tournaments.getCurrentTournamentId();
  var name = document.getElementById("tournamentName").value;
  
  var json = JSON.stringify({
    "id": Number(id),
    "name": name
  });
  
  return json;  
}

var tournaments = new Tournaments();
