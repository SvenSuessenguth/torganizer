/* global fetch, tournamentsResource */

var tournaments = {
  onload: function onload() {
    tournaments.updateTable();
  },

//--------------------------------------------------------------------------------------------------------------------
//
// initialize
//
//--------------------------------------------------------------------------------------------------------------------
  updateTable: function updateTournamentsTable() {
    getMultiple("tournaments", 0, 100, tournaments.updateTableResolve);
  },

  updateTableResolve: function updateTournamentsTableResolve(data) {
    document.getElementById("count").innerHTML = data.length;
    let table = document.getElementById("tournaments");
    let tbody = table.getElementsByTagName("tbody")[0];

    // clear old ui
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    while (tbody.firstChild) {
      tbody.removeChild(tbody.firstChild);
    }

    // show new ui
    data.forEach(function (tournament) {
      let row = tbody.insertRow(tbody.rows.length);
      let cell = row.insertCell(0);
      cell.innerHTML = tournament.name;
      cell.setAttribute("id", "tournament-" + tournament.id);
      cell.onclick = function (e) {
        tournaments.select(tournament.id);
      };
    });
  },

  select: function tournamentSelected(id) {
    getSingle("tournaments", id, tournaments.selectResolve);
  },

  selectResolve: function showTournamentDetailsResolve(data) {
    // {"id":1,"name":"dings"}
    document.getElementById("name").value = data.name;
    sessionStorage.setItem('tournaments-current-tournament-id', data.id);
    sessionStorage.setItem('tournaments-current-tournament-name', data.name);
    menue.update();
  },

//--------------------------------------------------------------------------------------------------------------------
//
// save
//
//--------------------------------------------------------------------------------------------------------------------
  save: function save() {
    let json = tournaments.formToJSon();
    createOrUpdate("tournaments", json, tournaments.createResolve);
  },

  createResolve: function createResolve(json) {
    sessionStorage.setItem('tournaments-current-tournament-id', json.id);
    sessionStorage.setItem('tournaments-current-tournament-name', json.name);
    tournaments.updateTable();
    menue.update();
  },

//--------------------------------------------------------------------------------------------------------------------
//
// cancel
//
//--------------------------------------------------------------------------------------------------------------------
  cancel: function cancel() {
    sessionStorage.removeItem('tournaments-current-tournament-id');
    sessionStorage.removeItem('tournaments-current-tournament-name');
    document.getElementById("name").value = "";
    document.getElementById("name").focus();
    menue.update();
  },

//--------------------------------------------------------------------------------------------------------------------
//
// convert to/from json/form
//
//--------------------------------------------------------------------------------------------------------------------
  formToJSon: function formToJSon() {

    return {
      "id": tournaments.getCurrentTournamentId(),
      "name": document.getElementById("name").value
    };
  },

  getCurrentTournamentId: function getCurrentTournamentId() {
    let tournamentId = sessionStorage.getItem('tournaments-current-tournament-id');

    // do not convert NULL to '0'
    if (tournamentId !== null) {
      return Number(sessionStorage.getItem('tournaments-current-tournament-id'));
    }
    return tournamentId;
  }
};