/* global playersResource, tournamentsResource, tournaments */

var tableSize = Number(10);

class Players {
  constructor() {    
  }
  
  onLoad(){
    includeFragments();
    this.updatePlayersTable();
  }

  create(){
    var json = this.inputToJSon();
    playersResource.create(json, this.createSuccess, this.createFailure);
  }
  createSuccess(json){
    var tournamentId = tournaments.getCurrentTournamentId();
    // this can't be used inside a promise
    // https://stackoverflow.com/questions/32547735/javascript-promises-how-to-access-variable-this-inside-a-then-scope
    var players = new Players();
    tournamentsResource.addSubscriber(tournamentId, json.id, players.addSubscriberSuccess, players.addSubScriberFailure);
    window.location.reload(true);
  }
  createFailure(json){}
  addSubscriberSuccess(json){console.log("successfully added subscriber to tournament")}
  addSubScriberFailure(json){console.log("failure adding subscriber to tournament")}

  update() {
    var json = this.inputToJSon();
    playersResource.update(json, this.updateSuccess, this.updateFailure);
    window.location.reload(true);
  }
  updateSuccess(json){}
  updateSuccessFailure(json){}

  inputToJSon(){
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

  updatePlayersTable(){
    var offset = Number(sessionStorage.getItem('players-table-offset'));
    var tournamentId = tournaments.getCurrentTournamentId();
    
    document.getElementById("playersOffset").innerHTML = offset;
    document.getElementById("playersLength").innerHTML = offset + tableSize;

    tournamentsResource.getSubscribers(tournamentId, offset, tableSize, this.getSubscribersSuccess, this.getSubscribersFailure);
  }
  // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
  // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
  getSubscribersSuccess(json){
    var tableBody = document.querySelector('#playersTableBody');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
      
    // daten in die tabelle einfuegen
    json.forEach(function(player){
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
  }
  getSubscribersFailure(json){}

  next(){
    var playersCount = Number(document.getElementById("playersCount").innerHTML);
    var currOffset = Number(sessionStorage.getItem('players-table-offset'));
    var newOffset = currOffset + tableSize;
      
    if(newOffset>=playersCount){
      return;
    }
      
    document.getElementById("playersOffset").innerHTML = newOffset;
    sessionStorage.setItem('players-table-offset', newOffset);
    this.updatePlayersTable();
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
    this.updatePlayersTable();
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
