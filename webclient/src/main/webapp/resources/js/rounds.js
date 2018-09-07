/* global disciplinesResource, tournamentsResource, tournaments */
/*
rounds.round.id           ID of the current round
rounds.round.position     position of the current round
rounds.round.system       system of the current round
rounds.round.qualified    qualified opponents in the current round
rounds.count              number of rounds
 */

let rounds =  {

  onload : function onload() {
    rounds.initDisciplineName();
    rounds.initRounds();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize discipline
  //
  //--------------------------------------------------------------------------------------------------------------------
  initDisciplineName : function initDisciplineName(){
    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    if(currentDisciplineId===null){return;}

    getSingle("disciplines", currentDisciplineId, rounds.initDisciplineNameResolve);
  },
  initDisciplineNameResolve : function initDisciplineNameResolve(discipline){
    let name = discipline.name;
    let dnElement = document.getElementById("disciplineName");
    dnElement.innerHTML = name;
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize round
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRounds : function initRounds(){
    let disciplineId = sessionStorage.getItem('disciplines.discipline.id');

    // getting ids for highest round and group (of highest round)
    disciplinesResource.getRounds(disciplineId, rounds.initRoundsResolve);
  },
  initRoundsResolve : function initRoundsResolve(json){
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
      sessionStorage.setItem('rounds.round.id', roundWithHighestPostion.id);
      sessionStorage.setItem('rounds.round.position', roundWithHighestPostion.position);
      sessionStorage.setItem('rounds.round.system', roundWithHighestPostion.system);
      sessionStorage.setItem('rounds.round.qualified', roundWithHighestPostion.qualified);
      sessionStorage.setItem('rounds.count', json.length);
    }else{
      // discipline does not have any round
      sessionStorage.removeItem('rounds.round.id');
      sessionStorage.removeItem('rounds.round.position');
      sessionStorage.removeItem('rounds.round.system');
      sessionStorage.removeItem('rounds.round.qualified');
      sessionStorage.removeItem('rounds.count');
    }

    rounds.updateRoundElement();
    rounds.roundToForm(roundWithHighestPostion);
  },

  // show current round data from sessionStorage
  updateRoundElement : function updateRoundElement(){
    let roundsCount = Number(sessionStorage.getItem("rounds.count"));
    let currentRoundPosition = sessionStorage.getItem("rounds.round.position");
    let rElement = document.getElementById("round");

    // enable or disable the next/prev-buttons
    document.getElementById("prevRound").disabled = Number(currentRoundPosition)<=0;
    document.getElementById("nextRound").disabled = Number(currentRoundPosition)>=roundsCount-1;

    if(currentRoundPosition===null || roundsCount===0){
      rElement.innerHTML = '-';
      return;
    }
    rElement.innerHTML = Number(currentRoundPosition) + 1;
  },

  saveRound : function saveRound(){
    let round = rounds.formToRound();
    createOrUpdate("rounds", round, rounds.saveRoundResolve);
  },
  saveRoundResolve : function saveRoundResolve(round){
    sessionStorage.setItem("rounds.round.id", round.id);
    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    disciplinesResource.addRound(currentDisciplineId, round.id, rounds.addRoundResolve);
  },
  addRoundResolve : function addRoundResolve(round){
    console.log("add rounds resolve");
    let count = Number(sessionStorage.getItem("rounds.count"))+1;

    sessionStorage.setItem("rounds.count", count);
    rounds.initRounds();
    rounds.roundToForm(round);
  },

  deleteRound : function deleteRound(){
  },
  cancelRound : function cancelRound(){
    let defaultRound = {
      "id": null,
      "system": "ROUND_ROBIN",
      "qualified":null
    };
    rounds.roundToForm(defaultRound);

    sessionStorage.removeItem("rounds.round.position");
    sessionStorage.removeItem("rounds.round.id");
    rounds.updateRoundElement();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // selecting/showing previous round (if possible)
  //
  //--------------------------------------------------------------------------------------------------------------------
  prevRound : function prevRound(){
    let currentPosition = Number(sessionStorage.getItem("rounds.round.position"));
    if(currentPosition <= 0){
      return;
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    disciplinesResource.getRounds(currentDisciplineId, rounds.prevRoundResolve);
  },
  prevRoundResolve : function prevRoundResolve(json){
    let currentPosition = sessionStorage.getItem("rounds.round.position");

    // find round with previous position
    let roundWithPreviousPostion = null;
    json.forEach(function(round){
      if(round.position === currentPosition-1){
        roundWithPreviousPostion = round;
      }
    });

    rounds.updatePage(roundWithPreviousPostion);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // selecting/showing next round (if possible)
  //
  //--------------------------------------------------------------------------------------------------------------------
  nextRound : function nextRound(){
    let currentPosition = sessionStorage.getItem("rounds.round.position");
    let roundsCount = sessionStorage.getItem('rounds.count');
    if(currentPosition >= roundsCount-1){
      return;
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    disciplinesResource.getRounds(currentDisciplineId, rounds.nextRoundResolve);
  },
  nextRoundResolve : function nextRoundResolve(json){
    let currentPosition = Number(sessionStorage.getItem("rounds.round.position"));

    // find round with next position
    let roundWithNextPostion = null;
    let nextPosition = currentPosition+1;
    json.forEach(function(round){
      if(round.position === nextPosition){
        roundWithNextPostion = round;
      }
    });

    rounds.updatePage(roundWithNextPostion);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // update view/sessionStorage with data
  //
  //--------------------------------------------------------------------------------------------------------------------
  updatePage : function updatePage(round){
    rounds.updateSessionStorage(round);
    rounds.updateRoundElement();
    rounds.roundToForm(round);
  },
  updateSessionStorage : function updateSessionStorage(round){
    sessionStorage.setItem('rounds.round.id', round.id);
    sessionStorage.setItem('rounds.round.position', round.position);
    sessionStorage.setItem('rounds.round.system', round.system);
    sessionStorage.setItem('rounds.round.qualified', round.qualified);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting the input-data (round-data) to/from json
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToRound : function formToRound(){
    let id = sessionStorage.getItem('rounds.round.id');
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
  },
  roundToForm : function roundToForm(round){
    if(round===null){
      return;
    }

    // reset all ui-elements
    let qualifiedElement = document.getElementById("qualified");
    qualifiedElement.value = '';
    let systemElement = document.getElementById("system");
    systemElement.selectedIndex = 0;

    if(round==null){
      sessionStorage.removeItem("rounds.current.id");
      return;
    }

    sessionStorage.setItem("rounds.round.id", round.id);

    let qualified = round.qualified;
    if(qualified !== null) {
      qualified = Number(qualified);
      qualifiedElement.value= qualified;
    }

    let systemName = round.system;
    selectItemByValue(systemElement, systemName);
  },
}