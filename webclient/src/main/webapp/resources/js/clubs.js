/* global fetch, clubsResource */

class Clubs {
  constructor() {
  }
  
  onload(){
    this.showClubsTable();    
  }
 
  getCurrentClubId(){
    var clubId = sessionStorage.getItem('clubs-current-club-id');
    
    // do not convert NULL to '0'
    if(clubId !== null){
    return Number(sessionStorage.getItem('clubs-current-club-id'));
    }
    return clubId;
  }
  isActiveClub(){
    return this.getCurrentClubId() !== null;
  }
  
  showClubsTable(){
    clubsResource.readMultiple(0, 100, this.showClubsTableSuccess, this.showClubsTableFailure);
  }
  showClubsTableSuccess(data){    
    document.getElementById("clubsCount").innerHTML=data.length;
    var tableBody = document.querySelector('#clubsTableBody');
    
    data.forEach(function(club){
      var t = document.querySelector("#clubRecord").cloneNode(true);
      var template = t.content;
  
      var nameElement = template.querySelector("#name");
      nameElement.innerHTML = club.name;
      nameElement.setAttribute("id", "club-"+club.id);
      nameElement.onclick = function(e){ clubs.showClubDetails(club.id); };
        
      tableBody.appendChild(template);
    });
  }
  showClubsTableFailure(data){console.log(data);}
  
  showClubDetails(id) {
    fetch('http://localhost:8080/rest/resources/clubs/'+id).then(function(response) {
      return response.json();
    }).then(function(data) {
      // {"id":1,"name":"dings"}
      document.getElementById("clubName").setAttribute('value', data.name);
      sessionStorage.setItem('clubs-current-club-id', data.id);
      sessionStorage.setItem('clubs-current-club-name', data.name);
    }).catch(function(err) {
    });
  }
  
  save(){
    var json = this.inputToJSon();
    var clubId = this.getCurrentClubId();
    var method;
    
    if(clubId===null){ method = "POST"; }
    else{ method = "PUT"; }
    
    clubsResource.createOrUpdate(json, method, this.createSuccess, this.createFailure);
  }
  
  createSuccess(json){
    sessionStorage.setItem('clubs-current-club-id', json.id);
    sessionStorage.setItem('clubs-current-club-name', json.name);
    window.location.reload(true);
  }
  createFailure(json){}

  updateSuccess(json){
    sessionStorage.setItem('clubs-current-club-id', json.id);
    sessionStorage.setItem('clubs-current-club-name', json.name);
    window.location.reload(true);
  }
  updateFailure(json){}
  
  cancel(){
    sessionStorage.removeItem('clubs-current-club-id');
    sessionStorage.removeItem('clubs-current-club-name');
    document.getElementById("clubName").value = "";
    document.getElementById("clubName").focus();
  }
  
  inputToJSon(){
    var id= clubs.getCurrentClubId();
    var name = document.getElementById("clubName").value;
  
    var json = JSON.stringify({
      "id": id,
      "name": name
    });  
    return json;  
  }
}

var clubs = new Clubs();
