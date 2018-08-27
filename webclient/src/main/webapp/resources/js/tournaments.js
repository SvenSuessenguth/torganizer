/* global fetch, tournamentsResource */

var tournaments = {
  onload: function onload() {
    tournaments.updateTournamentsTable();
  },

//--------------------------------------------------------------------------------------------------------------------
//
// initialize and actions for tournaments table
//
//--------------------------------------------------------------------------------------------------------------------
  updateTournamentsTable: function updateTournamentsTable() {
    fetch(resourcesUrl() + "tournaments?offset=0&maxResults=100", {getHeader})
      .then(function (response) {
        if (response.ok) {
          return response.json();
        }
        else {
          throw new Error('Fehlerhandling noch nicht spezifiziert');
        }
      })
      .then(function (json) {
        tournaments.updateTournamentsTableResolve(json);
      })
      .catch(function (err) {
        resourceReject(err);
      });
  },

  updateTournamentsTableResolve: function updateTournamentsTableResolve(data) {
    document.getElementById("tournamentsCount").innerHTML = data.length;
    let tableBody = document.querySelector('#tournamentsTableBody');

    // clear old ui
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }

    // show new ui
    data.forEach(function (tournament) {
      let t = document.querySelector("#tournamentRecord").cloneNode(true);
      let template = t.content;

      let nameElement = template.querySelector("#name");
      nameElement.innerHTML = tournament.name;
      nameElement.setAttribute("id", "tournament-" + tournament.id);
      nameElement.onclick = function (e) {
        tournaments.tournamentSelected(tournament.id);
      };

      tableBody.appendChild(template);
    });
  },

  tournamentSelected: function tournamentSelected(id) {
    fetch(resourcesUrl() + "tournaments/" + id, {
      getHeader
    }).then(function (response) {
      if (response.ok) {
        return response.json();
      }
      else {
        throw new Error('no error handling specified...');
      }
    }).then(function (json) {
      tournaments.showTournamentDetailsResolve(json);
    })
      .catch(function (err) {
        resourceReject(err);
      });
  },

  showTournamentDetailsResolve: function showTournamentDetailsResolve(data) {
    // {"id":1,"name":"dings"}
    document.getElementById("tournamentName").value = data.name;
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
    tournaments.updateTournamentsTable();
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
    document.getElementById("tournamentName").value = "";
    document.getElementById("tournamentName").focus();
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
      "name": document.getElementById("tournamentName").value
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