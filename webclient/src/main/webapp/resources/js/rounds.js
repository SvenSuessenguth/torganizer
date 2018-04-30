/* global disciplinesResource, tournamentsResource, tournaments */

class Rounds {
  constructor() {
  }

  onload() {
    this.initSessionStorage(null);
    this.initDisciplineName();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize with values from sessionStorage
  //
  //--------------------------------------------------------------------------------------------------------------------
  // initilize with default values only if nothing is already in sessionStorage
  initSessionStorage(selectedDisciplineId){
  }
  initDisciplineName(){
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    if(currentDisciplineId===null){return;}

    disciplinesResource.readSingle(currentDisciplineId, this.initDisciplineNameResolve, this.initDisciplineNameReject);
  }
  initDisciplineNameResolve(discipline){
    let name = discipline.name;
    let dnElement = document.getElementById("disciplineName");
    dnElement.innerHTML = name;
  }
  initDisciplineNameReject(json){ }
}

var rounds = new Rounds();
