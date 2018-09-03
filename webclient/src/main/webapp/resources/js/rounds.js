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
    this.initRounds();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize discipline
  //
  //--------------------------------------------------------------------------------------------------------------------
  initDisciplineName(){
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    if(currentDisciplineId===null){return;}

    disciplinesResource.readSingle(currentDisciplineId, this.initDisciplineNameResolve, processMessages);
  }
  initDisciplineNameResolve(discipline){
    let name = discipline.name;
    let dnElement = document.getElementById("disciplineName");
    dnElement.innerHTML = name;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize round
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRounds(){
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');

    // getting ids for highest round and group (of highest round)
    disciplinesResource.getRounds(disciplineId, this.initRoundsResolve, processMessages);
  }
  initRoundsResolve(json){
    // show number of rounds
    let numberOfRoundsElem = document.getElementById("numberOfRounds");
    numberOfRoundsElem.innerHTML = json.length;

    // find round with highest position
    let roundWithHighestPostion = null;
    json.forEach(function(round){
      if(roundWithHighestPostion===null || roundWithHighestPostion.position<round.position){
        roundWithHighestPostion = round;
      }
    });

    if(roundWithHighestPostion!==null) {
      // discipline has at least on round
      sessionStorage.setItem('rounds.current-round.id', roundWithHighestPostion.id);
      sessionStorage.setItem('rounds.current-round.position', roundWithHighestPostion.position);
      sessionStorage.setItem('rounds.current-round.system', roundWithHighestPostion.system);
      sessionStorage.setItem('rounds.current-round.qualified', roundWithHighestPostion.qualified);
      sessionStorage.setItem('rounds.count', json.length);
    }else{
      // discipline does not have any round
      sessionStorage.removeItem('rounds.current-round.id');
      sessionStorage.removeItem('rounds.current-round.position');
      sessionStorage.removeItem('rounds.current-round.system');
      sessionStorage.removeItem('rounds.current-round.qualified');
      sessionStorage.removeItem('rounds.count');
    }

    rounds.updateRoundElement();
    rounds.roundToForm(roundWithHighestPostion);
  }

  // show current round data from sessionStorage
  updateRoundElement(){
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
    rElement.innerHTML = Number(currentRoundPosition) + 1;
  }

  saveRound(){
    let roundJson = this.formToRound();
    let method = "PUT"; // update
    if(roundJson.id===null){
      method = "POST" // create
    }

    roundsResource.createOrUpdate(roundJson, method, this.saveRoundResolve, processMessages);
  }
  saveRoundResolve(round){
    sessionStorage.setItem("rounds.current-round.id", round.id);
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    disciplinesResource.addRound(currentDisciplineId, round.id, rounds.addRoundResolve, processMessages);
  }
  addRoundResolve(round){
      console.log("add rounds resolve");
    let count = Number(sessionStorage.getItem("rounds.count"))+1;

    sessionStorage.setItem("rounds.count", count);
    rounds.initRounds();
    rounds.roundToForm(round);
  }

  deleteRound(){
  }
  cancelRound(){
    let defaultRound = {
      "id": null,
      "system": "ROUND_ROBIN",
      "qualified":null
    };
    this.roundToForm(defaultRound);

    sessionStorage.removeItem("rounds.current-round.position");
    sessionStorage.removeItem("rounds.current-round.id");
    this.updateRoundElement();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // selecting/showing previous round (if possible)
  //
  //--------------------------------------------------------------------------------------------------------------------
  prevRound(){
    let currentPosition = Number(sessionStorage.getItem("rounds.current-round.position"));
    if(currentPosition <= 0){
      return;
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    disciplinesResource.getRounds(currentDisciplineId, this.prevRoundResolve, processMessages);
  }
  prevRoundResolve(json){
    let currentPosition = sessionStorage.getItem("rounds.current-round.position");

    // find round with previous position
    let roundWithPreviousPostion = null;
    json.forEach(function(round){
      if(round.position === currentPosition-1){
        roundWithPreviousPostion = round;
      }
    });

    rounds.updatePage(roundWithPreviousPostion);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // selecting/showing next round (if possible)
  //
  //--------------------------------------------------------------------------------------------------------------------
  nextRound(){
    let currentPosition = sessionStorage.getItem("rounds.current-round.position");
    let roundsCount = sessionStorage.getItem('rounds.count');
    if(currentPosition >= roundsCount-1){
      return;
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    disciplinesResource.getRounds(currentDisciplineId, this.nextRoundResolve, processMessages);
  }
  nextRoundResolve(json){
    let currentPosition = Number(sessionStorage.getItem("rounds.current-round.position"));

    // find round with next position
    let roundWithNextPostion = null;
    let nextPosition = currentPosition+1;
    json.forEach(function(round){
      if(round.position === nextPosition){
        roundWithNextPostion = round;
      }
    });

    rounds.updatePage(roundWithNextPostion);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // update view/sessionStorage with data
  //
  //--------------------------------------------------------------------------------------------------------------------
  updatePage(round){
    rounds.updateSessionStorage(round);
    rounds.updateRoundElement();
    rounds.roundToForm(round);
  }
  updateSessionStorage(round){
    sessionStorage.setItem('rounds.current-round.id', round.id);
    sessionStorage.setItem('rounds.current-round.position', round.position);
    sessionStorage.setItem('rounds.current-round.system', round.system);
    sessionStorage.setItem('rounds.current-round.qualified', round.qualified);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (round-data) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToRound(){
    let id = sessionStorage.getItem('rounds.current-round.id');
    let systemElement = document.getElementById("system");
    let qualified = document.getElementById("qualified").value;

    if(id!==null){
      id = Number(id);
    }

    if(qualified !== null) {
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
    if(round===null){
      return;
    }

    // reset all ui-elements
    let qualifiedElement = document.getElementById("qualified");
    qualifiedElement.value = '';
    let systemElement = document.getElementById("system");
    systemElement.selectedIndex = 0;

    if(round==null){
      sessionStorage.removeItem("rounds.current-round.id");
      return;
    }

    sessionStorage.setItem("rounds.current-round.id", round.id);

    let qualified = round.qualified;
    if(qualified !== null) {
      qualified = Number(qualified);
      qualifiedElement.value= qualified;
    }

    let systemName = round.system;
    selectItemByValue(systemElement, systemName);
  }
}

let rounds = new Rounds();
