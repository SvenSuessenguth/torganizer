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
    // rounds.initRoundsDetails();
    //rounds.initRoundsGroup();
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
    if(disciplineId==null){
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

      if(disciplineId==discipline.id.toString()){
        option.selected = "selected";
      }
    });
  },
  showDisciplineSelected : function showDisciplineSelected(){
    let dSelect = document.getElementById("disciplines");
    let disciplineId = dSelect.options[dSelect.selectedIndex].value;
    rounds.roundToForm({});

    if(disciplineId !== "null") {
      sessionStorage.setItem("rounds.discipline.id", disciplineId);
    }
    else{
      sessionStorage.removeItem("rounds.discipline.id");
    }

    sessionStorage.removeItem("rounds.round.id");
    sessionStorage.removeItem("rounds.round.position");
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
    if(numberOfRounds===0 ){
      roundElement.innerText = "-";
    }else{
      // if there is any round, but none is selected, then select the first round
      if(roundPosition===null){
        roundPosition = "0";
      }
      sessionStorage.setItem("rounds.round.position", roundPosition);
      roundElement.innerText = roundPosition;
    }

    // update rounds.round.id
    sessionStorage.removeItem("rounds.round.id");
    json.forEach(function(round){
      if(round.position==roundPosition){
        sessionStorage.setItem("rounds.round.id", round.id);
      }
    });

    // enable/disable prev/next round
    let prevRoundElement = document.getElementById("prevRound");
    let nextRoundElement = document.getElementById("nextRound");
    prevRoundElement.setAttribute("disabled", "disabled");
    nextRoundElement.setAttribute("disabled", "disabled");
    if(numberOfRounds>0 && roundPosition!==null){
      if(roundPosition>0 ){ prevRoundElement.removeAttribute("disabled"); }
      if(roundPosition<(numberOfRounds-1)){ nextRoundElement.removeAttribute("disabled"); }
    }

    rounds.initRoundsDetails();
  },

  prevRound : function prevRound(){
    let roundPosition = Number(sessionStorage.getItem("rounds.round.position"));
    sessionStorage.setItem("rounds.round.position", roundPosition - 1);
    rounds.initRoundsSelection();
  },

  nextRound : function nextRound(){
    let roundPosition = Number(sessionStorage.getItem("rounds.round.position"));
    sessionStorage.setItem("rounds.round.position", roundPosition + 1);
    rounds.initRoundsSelection();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize round data
  // save, delete, cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRoundsDetails : function initRoundsDetails(){
    let roundId = sessionStorage.getItem("rounds.round.id");
    if(roundId==null){ return; }

    getSingle("rounds", roundId, rounds.initRoundsDetailsResolve);
  },

  initRoundsDetailsResolve : function initRoundsDetailsResolve(json){
    rounds.roundToForm(json);
  },

  save : function save(){
    let json = rounds.formToRound();
    createOrUpdate("rounds", json, rounds.saveResolve);
  },
  saveResolve : function saveResolve(json){
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    let roundId = json.id;
    disciplinesResource.addRound(disciplineId, roundId, rounds.addRoundToDisciplineResolve);
    sessionStorage.setItem("rounds.round.id", roundId);
  },
  addRoundToDisciplineResolve : function addRoundToDisciplineResolve(json){
    rounds.initRoundsSelection();
  },

  cancel : function cancel(){
    // reset all ui-elements
    let qualifiedElement = document.getElementById("qualified");
    qualifiedElement.value = '';
    let systemElement = document.getElementById("system");
    systemElement.selectedIndex = 0;

    sessionStorage.removeItem("rounds.round.id");
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting round form/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  roundToForm : function roundToForm(json){
    rounds.cancel();

    if(json==null){ return; }

    sessionStorage.setItem("rounds.round.id", json.id);

    let qualifiedElement = document.getElementById("qualified");
    let qualified = json.qualified;
    if(qualified != null) {
      qualified = Number(qualified);
      qualifiedElement.value= qualified;
    }

    let systemElement = document.getElementById("system");
    let systemName = json.system;
    selectItemByValue(systemElement, systemName);
  },

  formToRound : function formToRound(){
    let id = sessionStorage.getItem('rounds.round.id');
    let systemElement = document.getElementById("system");
    let qualified = document.getElementById("qualified").value;

    if(id!==null){ id = Number(id); }
    if(qualified !== null) { qualified = Number(qualified); }

    return {
      "id": id,
      "system": systemElement.options[systemElement.selectedIndex].value,
      "qualified":qualified
    };
  },
};