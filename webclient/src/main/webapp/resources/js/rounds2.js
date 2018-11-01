class Rounds {
  constructor(){
    this.crud = new Crud();
  }

  onload(){
    this.prepareUpdateDisciplineSelection();
    this.prepareUpdateRoundSelection();
  }

  prepareUpdateDisciplineSelection(){
    let tournamentId = tournaments.getId();
    let url = resourcesUrl() + `tournaments/${tournamentId}/disciplines`;

    this.crud.get(url, this.updateDisciplineSelection.bind(this));
  }
  updateDisciplineSelection(jDisciplines){
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
    if (disciplineId == null) {
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

      if (disciplineId == jDiscipline.id.toString()) {
        option.selected = "selected";
      }
    });
  }

  prepareUpdateRoundSelection(){
    let disciplineId = sessionStorage.getItem('rounds.discipline.id');
    let url = resourcesUrl() + `disciplines/${disciplineId}/rounds`;

    if(disciplineId!=null) {
      this.crud.get(url, this.updateRoundSelection.bind(this));
    }else {
      this.clear();
    }
  }
  updateRoundSelection(jRounds){
    if(jRounds.length==undefined || jRounds.length==0 ){
      document.querySelector("#numberOfRounds").innerHTML = '0';
      document.querySelector("#prevRound").setAttribute("disabled", "disabled");
      document.querySelector("#nextRound").setAttribute("disabled", "disabled");
      document.querySelector("#round").innerHTML = '-';

      sessionStorage.removeItem('rounds.round.id');
      sessionStorage.removeItem('rounds.round.position');

      return;
    }

    let lastRound = jRounds[jRounds.length-1];
    sessionStorage.setItem('rounds.round.id', lastRound.id);
    sessionStorage.setItem('rounds.round.position', lastRound.position);

    document.querySelector("#nextRound").setAttribute("disabled", "disabled");
    document.querySelector("#round").innerHTML = Number(lastRound.position) + 1;
    document.querySelector("#numberOfRounds").innerHTML = jRounds.length;
    if(jRounds.length>1) {
      document.querySelector("#prevRound").setAttribute("disabled", "enabled");
    }
  }

  prepareUpdateRoundSystemDefinition(){
  }
  updateRoundSystemDefinition(jRound){
  }

  prepareUpdateAssignableOpponents(){
  }
  updateAssignableOpponents(jOpponents){
  }

  prepareUpdateAssignedOpponents(){
  }
  updateAssignedOpponents(jOpponents){
  }

  clear(){
    this.updateRoundSelection({});
    this.updateRoundSystemDefinition({});
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Actions
  //
  //--------------------------------------------------------------------------------------------------------------------
  selectDiscipline() {
    let eDisciplines = document.querySelector("#disciplines");
    let disciplineId = eDisciplines.options[eDisciplines.selectedIndex].value;
    
    if (disciplineId !== "null") {
      sessionStorage.setItem("rounds.discipline.id", disciplineId);
    }
    else {
      sessionStorage.removeItem("rounds.discipline.id");
    }

    sessionStorage.removeItem("rounds.round.id");
    sessionStorage.removeItem("rounds.round.position");

    this.prepareUpdateRoundSelection();
  }

  save(){
    let round = this.formToRound();
    let url = resourcesUrl() + `rounds`;

    this.crud.createOrUpdate(url, round, this.saveFollowUp.bind(this));
  }
  saveFollowUp(jRound){
    let disciplineId = sessionStorage.getItem("rounds.discipline.id");
    let roundId = jRound.id;
    disciplinesResource.addRound(disciplineId, roundId, this.addRoundToDisciplineResolve.bind(this));
    sessionStorage.setItem("rounds.round.id", roundId);
  }
  addRoundToDisciplineResolve(jDiscipline){
    this.prepareUpdateRoundSelection();
  }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting round form/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  roundToForm(jRound) {
    if (jRound == null) { return; }

    sessionStorage.setItem("rounds.round.id", jRound.id);

    let qualifiedElement = document.getElementById("qualified");
    let qualified = jRound.qualified;
    if (qualified != null) {
      qualified = Number(qualified);
      qualifiedElement.value = qualified;
    }

    let systemElement = document.getElementById("system");
    let systemName = jRound.system;
    selectItemByValue(systemElement, systemName);
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

var rounds2 = new Rounds();