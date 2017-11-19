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
    return this.getCurrentTournamentId() != null;
  }
  
  showTournamentsTable(){
    fetch('http://localhost:8080/rest/resources/tournaments').then(function(response) {
      return response.json();
      }).then(function(data) {
        // {"tournaments":[{"id":1,"name":"dings"}]}
        document.getElementById("tournamentsCount").innerHTML=data.tournaments.length;

        var tableBody = document.querySelector('#tournamentsTableBody');
    
        data.tournaments.forEach(function(tournament){
        var t = document.querySelector("#tournamentRecord").cloneNode(true);
        var template = t.content;
    
        var nameElement = template.querySelector("#name");
        nameElement.innerHTML = tournament.name;
        nameElement.setAttribute("id", "tournament-"+tournament.id);
        nameElement.onclick = function(e){ tournaments.showTournamentDetails(tournament.id); };
        
        tableBody.appendChild(template);
      });
    }).catch(function(err) {
    });
  }
  
  showTournamentDetails(id) {
    fetch('http://localhost:8080/rest/resources/tournaments/'+id).then(function(response) {
      return response.json();
    }).then(function(tournament) {
      // {"id":1,"name":"dings"}
      document.getElementById("tournamentName").setAttribute('value', tournament.name);
      sessionStorage.setItem('tournaments-current-tournament-id', tournament.id);
      sessionStorage.setItem('tournaments-current-tournament-name', tournament.name);
      
      window.location.reload(true);
    }).catch(function(err) {
    });
  }

  createTournament(){
    var name = document.getElementById("tournamentName").value;
    
    fetch('http://localhost:8080/rest/resources/tournaments/create?name='+name).then(function(response) {
      return response.json();
    }).then(function(tournament) {
      sessionStorage.setItem('tournaments-current-tournament-id', tournament.id);
      sessionStorage.setItem('tournaments-current-tournament-name', tournament.name);
      
      window.location.reload(true);
    }).catch(function(err) {
    });
  }
  
  addPlayerToCurrentTournament(playerId) {
    var tournamentId = sessionStorage.getItem('tournaments-current-tournament-id');
    
    fetch('http://localhost:8080/rest/resources/tournaments/'+tournamentId+'/subscribers/add/'+playerId).then(function(response) {
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

var tournaments = new Tournaments();
