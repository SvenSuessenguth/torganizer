/* global fetch */

class TournamentsResource {
  constructor() {
  }

  getPlayers(tournamentId, offset, maxResults, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/players?offset=' + offset + '&maxResults=' + maxResults)
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  addPlayer(tournamentId, playerId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/players?pid=' + playerId, {
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  removePlayer(tournamentId, playerId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/players/' + playerId, {
      method: "DELETE",
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  countPlayers(tournamentId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/players/count')
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  getSquads(tournamentId, offset, maxResults, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/squads?offset=' + offset + '&maxResults=' + maxResults)
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  addSquad(tournamentId, squadId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/squads?sid=' + squadId, {
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  countSquads(tournamentId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/squads/count')
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  addDiscipline(tournamentId, disciplineId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/disciplines?did=' + disciplineId, {
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  getDisciplines(tournamentId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/disciplines')
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  getDiscipline(tournamentId, disciplineId, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/disciplines/' + disciplineId)
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

  assignableOpponents(tournamentId, disciplineId, offset, maxResults, onResolve) {
    fetch(resourcesUrl() + 'tournaments/' + tournamentId + '/assignable-opponents?disciplineId=' + disciplineId + "&offset=" + offset + "&maxResults=" + maxResults)
      .then(function (response) {
        if (response.ok)
          return response.json();
        else
          throw new Error('Fehlerhandling noch nicht spezifiziert');
      })
      .then(function (json) {
        onResolve(json);
      })
      .catch(function (err) {
        restResourceAdapter.resourceError(err);
      });
  }

}

var tournamentsResource = new TournamentsResource();