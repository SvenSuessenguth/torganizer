/* global fetch */

class TournamentsResource extends CrudResource{
  constructor() {
    super();
  }

  createOrUpdate(json, method, onResolve, onReject) {
    super.createOrUpdate("tournaments", json, method, onResolve, onReject);
  }

  readSingle(tournamentId, onResolve, onReject) {
    super.readSingle("tournaments", tournamentId, onResolve, onReject);
  }

  readMultiple(offset, length, onResolve, onReject) {
    super.readMultiple("tournaments", offset, length, onResolve, onReject);
  }

  delete(json) {
    super.delete(json);
  }

  getPlayers(tournamentId, offset, length, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/players?offset=' + offset + '&length=' + length)
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
        onReject(err);
      });
  }

  addPlayer(tournamentId, playerId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/players?pid=' + playerId, {
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
        onReject("???");
      });
  }

  removePlayer(tournamentId, playerId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/players/' + playerId, {
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
        onReject("???");
      });
  }

  countPlayers(tournamentId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/players/count')
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
        onReject("???");
      });
  }

  getSquads(tournamentId, offset, length, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/squads?offset=' + offset + '&length=' + length)
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
        onReject("???");
      });
  }

  addSquad(tournamentId, squadId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/squads?sid=' + squadId, {
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
        onReject("???");
      });
  }

  countSquads(tournamentId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/squads/count')
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
        onReject("???");
      });
  }

  addDiscipline(tournamentId, disciplineId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/disciplines?did=' + disciplineId, {
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
        onReject("???");
      });
  }

  getDisciplines(tournamentId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/disciplines')
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
        onReject("???");
      });
  }
  getDiscipline(tournamentId, disciplineId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/disciplines/' + disciplineId)
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
        onReject("???");
      });
  }

  assignableOpponents(tournamentId, disciplineId, offset, maxResults, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/tournaments/' + tournamentId + '/assignable-opponents?disciplineId=' + disciplineId+"&offset="+offset+"&maxResults="+maxResults)
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
        onReject("???");
      });
  }

}

var tournamentsResource = new TournamentsResource();