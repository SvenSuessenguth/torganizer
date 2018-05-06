/* global fetch */

class RoundsResource extends CrudResource {
  constructor() {
    super();
  }

  createOrUpdate(json, method, onResolve, onReject){
    super.createOrUpdate("rounds", json, method, onResolve, onReject);
  }
}

var roundsResource = new RoundsResource();