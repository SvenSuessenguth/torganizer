/* global fetch, clubsResource */

/*
variables in sessions-storage:
------------------------------------------------------------------------------------------------------------------------

clubs.club.id

clubs.club.name

 */
let clubs = {

  onload : function onload(){
    this.showTable();
  },

  getCurrentClubId : function getCurrentClubId(){
    var clubId = sessionStorage.getItem('clubs.club.id');

    // do not convert NULL to '0'
    if(clubId !== null){
      return Number(sessionStorage.getItem('clubs.club.id'));
    }

    return clubId;
  },

  showTable : function showTable(){
    getMultiple("clubs", 0, 100, clubs.showTableResolve);
  },

  showTableResolve : function showTableResolve(data){
    document.getElementById("count").innerHTML=data.length;
    var tableBody = document.querySelector('#clubsTableBody');

    // clear old ui
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }

    data.forEach(function(club){
      var t = document.querySelector("#clubRecord").cloneNode(true);
      var template = t.content;
  
      var tdName = template.querySelector("#tdName");
      tdName.innerHTML = club.name;
      tdName.setAttribute("id", "club-"+club.id);
      tdName.onclick = function(e){ clubs.showDetails(club.id); };
        
      tableBody.appendChild(template);
    });
  },

  showDetails : function showDetails(id) {
    getSingle("clubs", id, clubs.showDetailResolve);
  },

  showDetailResolve : function showDetailResolve(data){
    document.getElementById("name").setAttribute('value', data.name);
    sessionStorage.setItem('clubs.club.id', data.id);
    sessionStorage.setItem('clubs.club.name', data.name);
  },

  save : function save(){
    var json = this.formToClub();
    createOrUpdate("clubs", json, clubs.saveSuccess);
  },

  saveSuccess : function saveSuccess(json){
    sessionStorage.setItem('clubs.club.id', json.id);
    sessionStorage.setItem('clubs.club.name', json.name);
    clubs.showTable();
  },

  cancel : function cancel(){
    sessionStorage.removeItem('clubs.club.id');
    sessionStorage.removeItem('clubs.club.name');
    document.getElementById("name").value = "";
    document.getElementById("name").focus();
  },

  formToClub : function formToClub(){
    var id= clubs.getCurrentClubId();
    var name = document.getElementById("name").value;
  
    var json = {
      "id": id,
      "name": name
    };
    return json;  
  }
}

