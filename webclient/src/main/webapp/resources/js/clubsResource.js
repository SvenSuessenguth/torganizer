/* global fetch */

class ClubsResource {
  constructor() {
  }

  createOrUpdate(json, method, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs', {
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: json
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

  readSingle(clubId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId)
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

  readMultiple(offset, length, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs?offset=' + offset + '&length=' + length, {
      method: "GET",
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

  delete(json) {
  }

  getPlayers(clubId, offset, length, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/players?offset=' + offset + '&length=' + length)
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

  addPlayer(clubId, playerId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/players?pid=' + playerId, {
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

  removePlayer(clubId, playerId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/players/' + playerId, {
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

  countPlayers(clubId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/players/count')
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

  getSquads(clubId, offset, length, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/squads?offset=' + offset + '&length=' + length)
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

  addSquad(clubId, squadId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/squads?sid=' + squadId, {
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

  countSquads(clubId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/squads/count')
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

  addDiscipline(clubId, disciplineId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/disciplines?did=' + disciplineId, {
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

  getDisciplines(clubId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/disciplines')
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
  getDiscipline(clubId, disciplineId, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/disciplines/' + disciplineId)
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

  assignableOpponents(clubId, disciplineId, offset, maxResults, onResolve, onReject) {
    fetch('http://localhost:8080/rest/resources/clubs/' + clubId + '/assignable-opponents?disciplineId=' + disciplineId+"&offset="+offset+"&maxResults="+maxResults)
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

var clubsResource = new ClubsResource();