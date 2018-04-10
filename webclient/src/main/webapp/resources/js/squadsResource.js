/* global fetch */

class SquadsResource extends Resource{
  constructor() {
    super();
  }
  
  createOrUpdate(json, method, onResolve, onReject){
    super.createOrUpdate("squads", json, onResolve, onReject);
  }
  
  readSingle(id, onResolve, onReject){
    super.readSingle("squads", id, onResolve, onReject);
  }
}

var squadsResource = new SquadsResource();