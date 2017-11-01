// Laden der Turniere bei der Anzeige der Seite
function onload(){
  fetch('http://localhost:8080/rest/resources/tournaments').then(function(response) {
	return response.json();
  }).then(function(data) {
    // {"tournaments":[{"id":1,"name":"dings"}]}
    document.getElementById("tournamentsCount").innerHTML=data.tournaments.length;

    var personRecordTemplate = document.querySelector("#tournamentsRecord");
    var tableBody = document.querySelector('#tournamentsTableBody');
	
    data.tournaments.forEach(function(tournament){
    var t = document.querySelector("#tournamentRecord").cloneNode(true)		
    var template = t.content
	
    var nameElement = template.querySelector("#name");
    nameElement.innerHTML = tournament.name;
    nameElement.setAttribute("id", "tournament-"+tournament.id)
    nameElement.onclick = function(e){ showTournamentDetails(tournament.id); }
		
    tableBody.appendChild(template);
  });
  }).catch(function(err) {
  });
}

// Anzeigen der Turnierdaten des angeklickten Turniers aus der Tabelle
function showTournamentDetails(id){
  fetch('http://localhost:8080/rest/resources/tournaments/'+id).then(function(response) {
    return response.json();
  }).then(function(tournament) {
    // {"id":1,"name":"dings"}
    document.getElementById("tdName").setAttribute('value', tournament.name)
    localStorage.setItem('tournaments-current-tournament-id', tournament.id)
    localStorage.setItem('tournaments-current-tournament-name', tournament.name)
  }).catch(function(err) {
  });
}

// Anlegen einer neuen Person, mit den eingegebenen Daten
function newTournament(){
  var name = document.getElementById("tdName").value
  
  fetch('http://localhost:8080/rest/resources/tournaments/new?name='+name).then(function(response) {
    return response.json();
  }).then(function(tournament) {
    localStorage.setItem('tournaments-current-tournament-id', tournament.id)
    localStorage.setItem('tournaments-current-tournament-name', tournament.name)
  }).catch(function(err) {
  });

  // neuladen des gesamten Dokumentes nach einem 'post'
  window.location.reload(true);
}
