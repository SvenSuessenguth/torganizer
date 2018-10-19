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
    let clubId = sessionStorage.getItem('clubs.club.id');

    // do not convert NULL to '0'
    if(clubId !== null){
      return Number(sessionStorage.getItem('clubs.club.id'));
    }

    return clubId;
  },

  showTable : function showTable(){
	  restResourceAdapter.getMultiple("clubs", 0, 100, clubs.showTableResolve);
  },

  showTableResolve : function showTableResolve(data){
    document.getElementById("count").innerHTML=data.length;
    let tableBody = document.querySelector('#clubsTableBody');

    // clear old ui
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }

    data.forEach(function(club){
      let t = document.querySelector("#clubRecord").cloneNode(true);
      let template = t.content;
  
      let tdName = template.querySelector("#tdName");
      tdName.innerHTML = club.name;
      tdName.setAttribute("id", "club-"+club.id);
      tdName.onclick = function(e){ clubs.showDetails(club.id); };
        
      tableBody.appendChild(template);
    });
  },

  showDetails : function showDetails(id) {
	restResourceAdapter.getSingle("clubs", id, clubs.showDetailResolve);
  },

  showDetailResolve : function showDetailResolve(data){
    document.getElementById("name").setAttribute('value', data.name);
    sessionStorage.setItem('clubs.club.id', data.id);
    sessionStorage.setItem('clubs.club.name', data.name);
  },

  save : function save(){
    let json = this.formToClub();
    restResourceAdapter.createOrUpdate("clubs", json, clubs.saveSuccess);
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
    let id= clubs.getCurrentClubId();
    let name = document.getElementById("name").value;
  
    return  {
      "id": id,
      "name": name
    };
  }
};

