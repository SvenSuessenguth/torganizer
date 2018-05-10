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
    // set current round to first round of discipline or null otherwise
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    disciplinesResource.getRounds(currentDisciplineId, this.setFirstRoundResolve, this.setFirstRoundReject);
  }
  setFirstRoundResolve(jsonRounds){
    sessionStorage.removeItem("rounds.current-round.id");
    sessionStorage.removeItem("rounds.current-round.position");
    sessionStorage.setItem("rounds.count", 0);
    if(jsonRounds.length>0){
      let id = jsonRounds[0].id;
      let position = jsonRounds[0].position;
      sessionStorage.setItem("rounds.current-round.id", id);
      sessionStorage.setItem("rounds.current-round.position", position);
      sessionStorage.setItem("rounds.count", jsonRounds.length);

    }
    rounds.updateRound();
  }
  setFirstRoundReject(error){}


  updateRound(){
    let roundsCount = Number(sessionStorage.getItem("rounds.count"));
    let currentRoundPosition = Number(sessionStorage.getItem("rounds.current-round.position"));
    let rElement = document.getElementById("round");
    if(currentRoundPosition===null){
      rElement.innerHTML = '-';
      return;
    }
    rElement.innerHTML = currentRoundPosition;

    // enable or disable the next/prev-buttons
    document.getElementById("prevRound").disabled = currentRoundPosition<=0;
    document.getElementById("nextRound").disabled = currentRoundPosition>=roundsCount-1;
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
    let count = Number(sessionStorage.getItem("rounds.count"))+1;

    sessionStorage.setItem("rounds.count", count);
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
