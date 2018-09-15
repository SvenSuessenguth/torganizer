/* global disciplinesResource, tournamentsResource, tournaments */
/*
rounds.round.id           ID of the current round
rounds.round.position     position of the current round
rounds.round.system       system of the current round
rounds.round.qualified    qualified opponents in the current round
rounds.count              number of rounds
rounds.discipline.id      id of the currently selected discipline to show rounds for
 */

let rounds =  {

  onload : function onload() {
    rounds.initDisciplinesSelection();
    rounds.initRoundsSelection();
    //rounds.initRoundsData();
    //rounds.initRoundsGroups();
    //rounds.initRoundsGroups();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize discipline selection
  //
  //--------------------------------------------------------------------------------------------------------------------
  initDisciplinesSelection : function initDisciplinesSelection(){
    getMultiple("disciplines", 0, 999, rounds.initDisciplinesSelectionResolve);
  },
  initDisciplinesSelectionResolve : function initDisciplinesSelectionResolve(disciplines){
    let dSelect = document.getElementById("disciplines");
    let disciplineId = sessionStorage.getItem('rounds.discipline.id');

    // remove all optione before adding new ones
    while (dSelect.firstChild) {
      dSelect.removeChild(dSelect.firstChild);
    }

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "null";
    option.id= "null";
    if(disciplineId===null){
      option.selected = "null";
    }
    dSelect.appendChild(option);


    // add an option for every discipline
    disciplines.forEach(function (discipline) {
      let option = document.createElement("option");
      option.text = discipline.name;
      option.value = discipline.id;
      option.id = discipline.id;
      dSelect.appendChild(option);

      if(disciplineId===discipline.id.toString()){
        option.selected = "selected";
      }
    });
  },
  showDisciplineSelected : function showDisciplineSelected(){
    let dSelect = document.getElementById("disciplines");
    let disciplineId = dSelect.options[dSelect.selectedIndex].value;

    if(disciplineId !== "null") {
      sessionStorage.setItem("rounds.discipline.id", disciplineId);
    }
    else{
      sessionStorage.removeItem("rounds.discipline.id");
    }

    rounds.initRoundsSelection();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize round selection
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRoundsSelection : function initRoundsSelection(){
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    if(disciplineId==null){
      return;
    }
    disciplinesResource.getRounds(disciplineId, rounds.initRoundsSelectionResolve);
  },
  initRoundsSelectionResolve : function initRoundsSelectionResolve(json){
    // update number of rounds
    let numberOfRounds = json.length;
    let norElement = document.getElementById("numberOfRounds");
    norElement.innerText = numberOfRounds;

    // update rounds.round.position
    let roundPosition = sessionStorage.getItem("rounds.round.position");
    let roundElement = document.getElementById("round");
    if(numberOfRounds==0 ){
      roundElement.innerText = "-"
      return;
    }

    // enable/disable prev/next round


  }
};