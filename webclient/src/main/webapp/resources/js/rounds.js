/* global disciplinesResource, tournamentsResource, tournaments */
/*
rounds.current-round.id           ID of the current round
rounds.current-round.position     position of the current round
rounds.current-round.system       system of the current round
rounds.current-round.qualified    qualified opponents in the current round
rounds.count                      number of rounds
 */

class Rounds {
  constructor() {
  }

  onload() {
    this.initDisciplineName();
    this.initRound();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize discipline
  //
  //--------------------------------------------------------------------------------------------------------------------
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
  // initialize round
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRound(){
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');

    // getting ids for highest round and group (of highest round)
    disciplinesResource.getRounds(disciplineId, this.initRoundResolve, this.initRoundReject);

  }
  initRoundResolve(json){
    // find round with highest position
    let roundWithHighestPostion = null;
    json.forEach(function(round){
      if(roundWithHighestPostion===null || roundWithHighestPostion.position<round.position){
        roundWithHighestPostion = round;
      }
    });

    if(roundWithHighestPostion!==null) {
      sessionStorage.setItem('rounds.current-round.id', roundWithHighestPostion.id);
      sessionStorage.setItem('rounds.current-round.position', roundWithHighestPostion.position);
      sessionStorage.setItem('rounds.current-round.system', roundWithHighestPostion.system);
      sessionStorage.setItem('rounds.current-round.qualified', roundWithHighestPostion.qualified);
      sessionStorage.setItem('rounds.count', json.length);
    }else{
      sessionStorage.removeItem('rounds.current-round.id');
      sessionStorage.removeItem('rounds.current-round.position');
      sessionStorage.removeItem('rounds.current-round.system');
      sessionStorage.removeItem('rounds.current-round.qualified');
      sessionStorage.removeItem('rounds.count');
    }

    rounds.updateRound();
    rounds.roundToForm(roundWithHighestPostion);
  }
  initRoundReject(error){}

  updateRound(){
    let roundsCount = Number(sessionStorage.getItem("rounds.count"));
    let currentRoundPosition = sessionStorage.getItem("rounds.current-round.position");
    let rElement = document.getElementById("round");

    // enable or disable the next/prev-buttons
    document.getElementById("prevRound").disabled = Number(currentRoundPosition)<=0;
    document.getElementById("nextRound").disabled = Number(currentRoundPosition)>=roundsCount-1;

    if(currentRoundPosition===null || roundsCount===0){
      rElement.innerHTML = '-';
      return;
    }
    rElement.innerHTML = currentRoundPosition;
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
    rounds.initRound();
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
