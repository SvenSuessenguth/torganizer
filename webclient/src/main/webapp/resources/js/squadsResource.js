/* global fetch */

class SquadsResource extends CrudResource{
  constructor() {
    super();
  }
  
  createOrUpdate(json, method, onResolve, onReject){
    super.createOrUpdate("squads", json, method, onResolve, onReject);
  }
  
  readSingle(id, onResolve, onReject){
    super.readSingle("squads", id, onResolve, onReject);
  }
}

var squadsResource = new SquadsResource();