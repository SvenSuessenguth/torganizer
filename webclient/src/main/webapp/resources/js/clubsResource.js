/* global fetch */

class ClubsResource extends CrudResource{
  constructor() {
    super();
  }

  createOrUpdate(json, method, onResolve, onReject) {
    super.createOrUpdate("clubs", json, method, onResolve, onReject);
  }

  readSingle(clubId, onResolve, onReject) {
    super.readSingle("clubs", clubId, onResolve, onReject);
  }

  readMultiple(offset, maxResults, onResolve, onReject) {
    super.readMultiple("clubs", offset, maxResults, onResolve, onReject);
  }
}

var clubsResource = new ClubsResource();