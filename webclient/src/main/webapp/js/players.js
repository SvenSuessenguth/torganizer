var tableSize = Number(10);
var tournaments = new Tournaments();

class Players {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.showPlayersTable();
  }

  createPlayer(){
    if(!isFormValid('playersForm')){ return; }
    
    fetch('http://localhost:8080/rest/resources/players/create',{
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: this.playerFormToJSon()
    }).then(function(response) {
      return response.json();
    }).then(function(player) {
      
      if(document.getElementById('autoAdd').checked){              
        tournaments.addPlayerToCurrentTournament(player.id);
      }
      
      window.location.reload(true);
    }).catch(function(err) {
    });
  }

  updatePlayer() {
    if(!isFormValid('playersForm')){ return; }
    
    fetch('http://localhost:8080/rest/resources/players/update',{
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: this.playerFormToJSon()
    }).then(function(response) {
      return response.json();
    }).then(function(player) {    
      if(document.getElementById('autoAdd').checked){
        var currentTournamentId = sessionStorage.getItem('tournaments-current-tournament-id');      
        addPlayerToTournament(currentTournamentId, player.id);
      }
      
      window.location.reload(true);
    }).catch(function(err) {
      
      window.location.reload(true);
    });
  }



  playerFormToJSon(){
    var playerId= sessionStorage.getItem('players-current-player-id');
    var personId = sessionStorage.getItem('players-current-player-person-id');
    var firstName = document.getElementById("pdFirstName").value;
    var lastName = document.getElementById("pdLastName").value;
    var dateOfBirth = document.getElementById("pdDateOfBirth").value;
    
    var genderElement = document.getElementById("pdGender");
    var gender = genderElement.options[genderElement.selectedIndex].value;
    
    var statusElement = document.getElementById("pdStatus");
    var status = statusElement.options[statusElement.selectedIndex].value;
    
    var json = JSON.stringify({
      "id": playerId,
      "status": status,
      "person":{
        "id": personId,
        "firstName": firstName,
        "lastName": lastName,
        "dateOfBirth": dateOfBirth,
        "gender": gender
      }
    });
    
    console.log("player: "+json);
    return json;
  }

  showPlayersTable(){
    var offset = Number(sessionStorage.getItem('players-table-offset'));
    
    document.getElementById("playersOffset").innerHTML = offset;
    document.getElementById("playersLength").innerHTML = offset + tableSize;

    tournaments.getCurrentSubscribers(offset, tableSize).then(function(data) {
      
      // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
      // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
      var tableBody = document.querySelector('#playersTableBody');
      while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
      }
      
      // daten in die tabelle einfuegen
      data.players.forEach(function(player){
        var t = document.querySelector("#playerRecord").cloneNode(true);
        var template = t.content;
    
        var firstNameElement = template.querySelector("#firstName");
        firstNameElement.innerHTML = player.person.firstName;
        firstNameElement.setAttribute("id", "firstName"+player.id);
        firstNameElement.onclick = function(e){ new Players().showSelectedPlayerDetails(player.id); };
      
        var lastNameElement = template.querySelector("#lastName"); 
        lastNameElement.innerHTML = player.person.lastName;
        lastNameElement.setAttribute("id", "lasttName"+player.id);
      
        tableBody.appendChild(template);
      });
      
      // aktualisieren der Anzahl-Anzeige ueber der tabellen
      tournaments.countCurrentSubscribers().then(function(response){
        document.getElementById("playersCount").innerHTML = response;
      });
    }).catch(function(err) {
    });
  }

  next(){
    var playersCount = Number(document.getElementById("playersCount").innerHTML);
    var currOffset = Number(sessionStorage.getItem('players-table-offset'));
    var newOffset = currOffset + tableSize;
      
    if(newOffset>=playersCount){
      return;
    }
      
    document.getElementById("playersOffset").innerHTML = newOffset;
    sessionStorage.setItem('players-table-offset', newOffset);
    this.showPlayersTable();
  }

  prev(){
    var currOffset = Number(sessionStorage.getItem('players-table-offset'));
    var newOffset = currOffset - tableSize;
      
    if(newOffset<0){
      newOffset = 0;
    }
      
    document.getElementById("playersOffset").innerHTML = newOffset;
    document.getElementById("playersLength").innerHTML = newOffset + tableSize;
    sessionStorage.setItem('players-table-offset', newOffset);
    this.showPlayersTable();
  }

  //
  //show selected players details
  //
  showSelectedPlayerDetails(id) {
    fetch('http://localhost:8080/rest/resources/players/'+id).then(function(response) {
      return response.json();
    }).then(function(player) {
   
      sessionStorage.setItem('players-current-player-id', player.id);
      sessionStorage.setItem('players-current-player-person-id', player.person.id);

      document.getElementById("pdFirstName").setAttribute('value', player.person.firstName);
      document.getElementById("pdLastName").setAttribute('value', player.person.lastName);
      document.getElementById("pdDateOfBirth").setAttribute('value', player.person.dateOfBirth);
      
      var genderElement = document.getElementById("pdGender");
      selectItemByValue(genderElement, player.person.gender);

    }).catch(function(err) {
    });
  }
}

var players = new Players();
