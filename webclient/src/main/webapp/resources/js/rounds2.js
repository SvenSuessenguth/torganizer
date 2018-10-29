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
      eDisciplines.removeChild(dSelect.firstChild);
    }

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "null";
    option.id = "null";
    if (disciplineId == null) {
      
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
  }
  updateRoundSelection(jRounds){
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
}

var rounds2 = new Rounds();