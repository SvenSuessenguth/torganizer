let groupsResource = {
  addOpponent: function addOpponent(groupId, opponentId, onResolve) {

    fetch(restResourceAdapter.resourcesUrl() + 'groups/' + groupId + "/opponents?opponentId=" + opponentId, {
      method: "POST",
      headers: {
        'Accept': 'application/json'
      }
    }).then(function (response) {
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
      })
  },
}

