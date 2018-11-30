class Rounds {
  constructor() {
    this.crud = new Crud();
  }

  onload() {
    this.prepareUpdateDisciplineSelection();
    this.prepareUpdateRoundSelection();
  }

  prepareUpdateDisciplineSelection() {
    let tournamentId = tournaments.getId();
    let url = resourcesUrl() + `tournaments/${tournamentId}/disciplines`;

    this.crud.get(url, this.updateDisciplineSelection.bind(this));
  }

  updateDisciplineSelection(jDisciplines) {
    let disciplineId = sessionStorage.getItem('rounds.discipline.id');
    let eDisciplines = document.querySelector("#disciplines");

    // remove all optione before adding new ones
    while (eDisciplines.firstChild) {
      eDisciplines.removeChild(eDisciplines.firstChild);
    }

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "null";
    option.id = "null";
    if (disciplineId == null || disciplineId == undefined) {
      option.selected = "null";
    }
    eDisciplines.appendChild(option);

    // add an option for every discipline
    jDisciplines.forEach(function (jDiscipline) {
      let option = document.createElement("option");
      option.text = jDiscipline.name;
      option.value = jDiscipline.id;
      option.id = jDiscipline.id;
      eDisciplines.appendChild(option);

      if (Number(disciplineId) === jDiscipline.id) {
        option.selected = "selected";
      }
    });
  }

  prepareUpdateRoundSelection() {
    let disciplineId = sessionStorage.getItem('rounds.discipline.id');
    let url = resourcesUrl() + `disciplines/${disciplineId}/rounds`;

    if (disciplineId != null) {
      this.crud.get(url, this.updateRoundSelectionLast.bind(this));
    } else {
      document.querySelector("#prevRound").setAttribute("disabled", "disabled");
      document.querySelector("#nextRound").setAttribute("disabled", "disabled");
      document.querySelector("#numberOfRounds").innerHTML = '';

      this.cancel();
    }
  }

  updateRoundSelectionLast(jAllRounds) {
    if (jAllRounds.length === undefined || jAllRounds.length === 0) {
      document.querySelector("#numberOfRounds").innerHTML = '0';
      document.querySelector("#prevRound").setAttribute("disabled", "disabled");
      document.querySelector("#nextRound").setAttribute("disabled", "disabled");
      this.updateRoundsSelectionSingle(undefined);
    }

    let lastRound = jAllRounds[jAllRounds.length - 1];
    this.updateRoundsSelectionSingle(lastRound);
    document.querySelector("#numberOfRounds").innerHTML = jAllRounds.length;
    document.querySelector("#nextRound").setAttribute("disabled", "disabled");
    if (jAllRounds.length > 1) {
      document.querySelector("#prevRound").removeAttribute("disabled");
    }
  }

  updateRoundsSelectionSingle(jRound) {
    if (jRound === undefined) {
      document.querySelector("#round").innerHTML = '-';
      sessionStorage.removeItem('rounds.round.id');
      sessionStorage.removeItem('rounds.round.position');

      return;
    }

    document.querySelector("#round").innerHTML = Number(jRound.position) + 1;
    sessionStorage.setItem('rounds.round.id', jRound.id);
    sessionStorage.setItem('rounds.round.position', jRound.position);

    this.prepareUpdateAssignableOpponents();

    this.roundToForm(jRound);
  }


  prepareUpdateRoundSystemDefinition() {
  }

  updateRoundSystemDefinition(jRound) {
  }

  prepareUpdateAssignableOpponents() {
    let roundId = sessionStorage.getItem("rounds.round.id");
    if(roundId==undefined){
      this.updateAssignableOpponents(JSON.parse("[]"));
      return;
    }

    let url = resourcesUrl() + `rounds/${roundId}/opponents-assignable-to-group`;
    this.crud.get(url, this.updateAssignableOpponents.bind(this));
  }

  updateAssignableOpponents(jOpponents) {
    console.log(JSON.stringify(jOpponents));
    let element = document.querySelector("#assignable");
    element.setAttribute("data", JSON.stringify(jOpponents));
  }

  prepareUpdateGroups(){

  }
  updateGroups(jGroups){
    // remove old groups
    let eGroups = document.querySelector("#groups");
    while (eGroups.firstChild) {
      eGroups.removeChild(eGroups.firstChild);
    }

    // show groups
    if(jGroups.length>0) {
      jGroups.forEach(function (jGroup) {
        eGroups.appendChild(document.createElement("opponents-table",))
      });
    }
  }
  //--------------------------------------------------------------------------------------------------------------------
  //
  //                                                                                                             Actions
  //
  //--------------------------------------------------------------------------------------------------------------------
  selectDiscipline() {
    let eDisciplines = document.querySelector("#disciplines");
    let disciplineId = eDisciplines.options[eDisciplines.selectedIndex].value;

    if (disciplineId !== "null") {
      sessionStorage.setItem("rounds.discipline.id", disciplineId);
      document.querySelector("#save").removeAttribute("disabled");
      document.querySelector("#delete").removeAttribute("disabled");
      document.querySelector("#cancel").removeAttribute("disabled");
    }
    else {
      sessionStorage.removeItem("rounds.discipline.id");
      document.querySelector("#save").setAttribute("disabled", "disabled");
      document.querySelector("#delete").setAttribute("disabled", "disabled");
      document.querySelector("#cancel").setAttribute("disabled", "disabled");
    }

    sessionStorage.removeItem("rounds.round.id");
    sessionStorage.removeItem("rounds.round.position");

    this.prepareUpdateRoundSelection();
    this.updateAssignableOpponents(JSON.parse("[]"));
  }

  //------------------------------------------------------------------------------------------------------------- save -
  save() {
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    if(disciplineId === null){
      return;
    }

    let jRound = this.formToRound();
    let url = resourcesUrl() + `rounds`;

    let callback = null;
    if (jRound.id != null) {
      callback = this.updateRoundsSelectionSingle.bind(this);
    }
    else {
      callback = this.addRoundToDiscipline.bind(this);
    }

    this.crud.createOrUpdate(url, jRound, callback);
  }

  addRoundToDiscipline(jRound) {
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    let roundId = jRound.id;
    disciplinesResource.addRound(disciplineId, roundId, this.addRoundToDisciplineResolve.bind(this));
    sessionStorage.setItem("rounds.round.id", roundId);
  }

  addRoundToDisciplineResolve(jDiscipline) {
    this.prepareUpdateRoundSelection();
  }

  //--------------------------------------------------------------------------------------------------- previous round -
  prevRound(){
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    if(disciplineId===null) { return; }

    let currentPosition = sessionStorage.getItem("rounds.round.position");
    let previousPosition = Number(currentPosition) - 1;
    if(previousPosition < 0){ previousPosition = 0; }

    let url = resourcesUrl() + `disciplines/${disciplineId}/round-by-position/${previousPosition}`;
    this.crud.get(url, this.updateRoundsSelectionSingle.bind(this));

    let ePrevRound = document.querySelector("#prevRound");
    let eNextRound = document.querySelector("#nextRound");

    if(currentPosition>0){ eNextRound.removeAttribute("disabled"); }
    else { eNextRound.setAttribute("disabled", "disabled"); }

    if(previousPosition <= 0) { ePrevRound.setAttribute("disabled", "disabled"); }
    else { ePrevRound.removeAttribute("disabled");}

    this.prepareUpdateAssignableOpponents();
  }

  //------------------------------------------------------------------------------------------------------- next round -
  nextRound(){
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    if(disciplineId===null) { return; }

    let currentPosition = sessionStorage.getItem("rounds.round.position");
    let nextPosition = Number(currentPosition) + 1;

    let url = resourcesUrl() + `disciplines/${disciplineId}/round-by-position/${nextPosition}`;
    this.crud.get(url, this.updateRoundsSelectionSingle.bind(this));

    let numberOfRounds = Number(document.querySelector("#numberOfRounds").innerHTML);
    let ePrevRound = document.querySelector("#prevRound");
    let eNextRound = document.querySelector("#nextRound");

    if(nextPosition < (numberOfRounds-1)){ eNextRound.removeAttribute("disabled"); }
    else { eNextRound.setAttribute("disabled", "disabled"); }

    if(nextPosition <= 0) { ePrevRound.setAttribute("disabled", "disabled"); }
    else { ePrevRound.removeAttribute("disabled");}

    this.prepareUpdateAssignableOpponents();
  }


  //----------------------------------------------------------------------------------------------------------- cancel -
  cancel() {
    this.updateRoundsSelectionSingle(undefined);
    this.roundToForm(undefined);
  }

  //------------------------------------------------------------------------------------------------------- new groups -
  newGroup(){
    let roundId = sessionStorage.getItem("rounds.round.id");

    let url = resourcesUrl() + `rounds/${roundId}/group`;
    this.crud.post(url, undefined, this.updateGroups.bind(this));
  }

  //----------------------------------------------------------------------------------------------------- delete group -
  deleteGroup(id){
    let roundId = sessionStorage.getItem("rounds.round.id");

    let url = resourcesUrl() + `rounds/${roundId}/group`;
    this.crud.delete(url, undefined, this.updateGroups.bind(this));
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  //                                                                                  converting round form/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  roundToForm(jRound) {
    let eQualified = document.getElementById("qualified");
    let eSystem = document.getElementById("system");

    if (jRound == null) {
      eQualified.value = '';
      selectFirstItem(eSystem);
    } else {
      eQualified.value = Number(jRound.qualified);

      let systemName = jRound.system;
      selectItemByValue(eSystem, systemName);
    }
  }

  formToRound() {
    let id = sessionStorage.getItem('rounds.round.id');
    let systemElement = document.getElementById("system");
    let qualified = document.getElementById("qualified").value;

    if (id !== null) {
      id = Number(id);
    }
    if (qualified !== null) {
      qualified = Number(qualified);
    }

    return {
      "id": id,
      "system": systemElement.options[systemElement.selectedIndex].value,
      "qualified": qualified
    };
  }
}

let rounds2 = new Rounds();