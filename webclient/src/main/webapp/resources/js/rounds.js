/* global disciplinesResource, tournamentsResource, tournaments */

class Rounds {
  constructor() {
  }

  onload() {
    this.initSessionStorage(null);
    this.initDisciplineName();
    this.initRound();
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


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for selection and configuration of a round
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRound() {
    sessionStorage.removeItem("rounds.current-round.id");
    this.updateRound();
  }

  updateRound(){
    // round-id
    let currentRoundId = sessionStorage.getItem("rounds.current-round.id");
    let rElement = document.getElementById("round");
    if(currentRoundId===null){
      rElement.innerHTML = '-';
      return;
    }
    rElement.innerHTML = currentRoundId;

    // enable or disable the next/prev-buttons
    if(currentRoundId>0){
      document.getElementById("prevRound").setAttribute("disabled", "enabled");
    }
  }

  saveRound(){
    let roundJson = this.formToRound();
    let method = "PUT"; // update
    if(roundJson.id===null){
      method = "POST" // create
    }

    roundsResource.createOrUpdate(roundJson, method, this.saveRoundResolve, this.saveRoundReject);
  }
  saveRoundResolve(round){
    sessionStorage.setItem("rounds.current-round.id", round.id);
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    disciplinesResource.addRound(currentDisciplineId, round.id, rounds.addRoundResolve, rounds.addRoundReject);
  }
  saveRoundReject(error){ }
  addRoundResolve(json){
    console.log("round added to discipline "+json.id);
  }
  addRoundReject(error){}

  deleteRound(){

  }
  cancelRound(){

  }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (round-data) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToRound(){
    let id = sessionStorage.getItem('rounds.current-round-id');
    let systemElement = document.getElementById("system");
    let qualified = document.getElementById("qualified").value;

    if(id!==null){
      id = Number(id);
    }

    if(qualified !== null && qualified.length>0) {
      qualified = Number(qualified);
    }

    let json = {
      "id": id,
      "system": systemElement.options[systemElement.selectedIndex].value,
      "qualified":qualified
    };

    return json;
  }
  roundToForm(round){
    sessionStorage.setItem("rounds.current-round-id", round.id);

    let systemElement = document.getElementById("system");
    selectItemByValue(systemElement, round.system);

    document.getElementById("qualified").setAttribute("value", round.qualified);
  }
}

var rounds = new Rounds();
