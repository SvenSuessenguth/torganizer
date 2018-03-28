/* global disciplinesResource, tournamentsResource, tournaments */

class Disciplines {
  constructor() {
  }

  onload() {
    this.initSelection();
    this.initOpponents();
    this.initAssignableOpponents();

    this.cancel();
  }
  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for disciplines-selection
  //
  //--------------------------------------------------------------------------------------------------------------------
  initSelection(){
    let tournamentId = tournaments.getCurrentTournamentId();
    tournamentsResource.getDisciplines(tournamentId, this.initDisciplinesResolve, this.initDisciplinesReject)
  }
  initDisciplinesResolve(disciplines) {
    let dSelect = document.getElementById("disciplines");

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "-";
    dSelect.appendChild(option);

    disciplines.forEach(function (discipline) {
      let option = document.createElement("option");
      option.text = discipline.name;
      option.value = discipline.id;
      dSelect.appendChild(option);
    });
  }
  initDisciplinesReject(json) {}

  showSelectDiscipline() {
    let dSelect = document.getElementById("disciplines");
    let disciplineId = dSelect.options[dSelect.selectedIndex].value;
    let tournamentId = tournaments.getCurrentTournamentId();

    // select empty
    if(disciplineId==="-"){
      disciplines.cancel();
      return;
    }

    sessionStorage.setItem('disciplines.current-discipline-id', disciplineId);

    disciplinesResource.readSingle(disciplineId, this.showSelectedDisciplineResolve, this.showSelectedDisciplineReject);
    disciplinesResource.getOpponents(disciplineId, disciplines.updateOpponentsResolve, disciplines.updateOpponentsReject);
    tournamentsResource.assignableOpponents(tournamentId, disciplineId, this.updateAssignableOpponentsResolve, this.updateAssignableOpponentsReject);
  }
  showSelectedDisciplineResolve(discipline) {
    disciplines.disciplineToForm(discipline);
  }
  showSelectedDisciplineReject(json) {}


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  initOpponents(){
    let opponentsLength = document.getElementById("opponents-table").getAttribute("rows");
    let tournamentId = tournaments.getCurrentTournamentId();
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline-id'));
    sessionStorage.setItem("disciplines.opponents-offset", "0");

    this.updateOpponents(disciplineId, 0, opponentsLength);

    document.getElementById("opponents-table").addEventListener("opponent-selected", this.opponentSelectedFromOpponents);
  }

  opponentSelectedFromOpponents(event){
    console.log("remove opponent "+event.detail+" from discipline "+sessionStorage.getItem('disciplines.current-discipline-id'));
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline-id'));
    let opponentId = event.detail;
    disciplinesResource.removeOpponent(disciplineId, opponentId, disciplines.removeOpponentResolve, disciplines.removeOpponentReject);
  }
  removeOpponentResolve(json){}
  removeOpponentReject(json){}

  updateOpponents(disciplineId, offset, rows){
    disciplinesResource.getOpponents(disciplineId, offset, rows, disciplines.updateOpponentsResolve, disciplines.updateOpponentsReject);
  }
  updateOpponentsResolve(json){
    document.getElementById("opponents-table").setAttribute("data", JSON.stringify(json));
  }
  updateOpponentsReject(text){ }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for assignableOpponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  initAssignableOpponents() {
    let assignableOpponentsLength = document.getElementById("assignable-opponents-table").getAttribute("rows");
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline-id'));
    let tournamentId = tournaments.getCurrentTournamentId();
    sessionStorage.setItem("disciplines.assignable-opponents-offset", 0);

    this.updateAssignableOpponents(tournamentId, disciplineId, 0, assignableOpponentsLength);

    document.getElementById("assignable-opponents-table").addEventListener("opponent-selected", this.opponentSelectedFromAssignableOpponents);
  }

  updateAssignableOpponents(tournamentId, disciplineId, offset, maxResults){
    tournamentsResource.assignableOpponents(tournamentId, disciplineId, this.updateAssignableOpponentsResolve, this.updateAssignableOpponentsReject );
  }
  updateAssignableOpponentsResolve(json){
    document.getElementById("assignable-opponents-table").setAttribute("data", JSON.stringify(json));
  }
  updateAssignableOpponentsReject(json){}


  opponentSelectedFromAssignableOpponents(event){
    console.log("add opponent "+event.detail+" to discipline "+Number(sessionStorage.getItem('disciplines.current-discipline-id')));
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline-id'));
    let opponentId = event.detail;
    disciplinesResource.addOpponent(disciplineId, opponentId, disciplines.addOpponentResolve, disciplines.addOpponentReject);
  }
  addOpponentResolve(json){console.log("opponent added "+JSON.stringify(json)); }
  addOpponentReject(text){ console.log("error while adding opponent"); }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save() {
    let discipline = this.formToDiscipline();
    if (discipline === null || discipline.id === '' || discipline.id === 'null' || discipline.id === null) {
      this.create(discipline);
    } else {
      this.update(discipline);
    }
  }
  create(discipline) {
    disciplinesResource.createOrUpdate(discipline, "POST", this.createResolve, this.createReject);
  }
  createResolve(json) {
    let tournamentId = tournaments.getCurrentTournamentId();
    let disciplineId = json.id;
    tournamentsResource.addDiscipline(tournamentId, disciplineId, disciplines.addDisciplineResolve, disciplines.addDisciplineReject);
  }
  createReject(json) { }
  addDisciplineResolve(json) {
    disciplines.cancel();
    disciplines.init();
  }
  addDisciplineReject(json) {}

  update(discipline) {
    disciplinesResource.createOrUpdate(discipline, "PUT", this.updateResolve, this.updateReject);
  }
  updateResolve(json) {
    disciplines.cancel();
    disciplines.init();
  }
  updateReject(json) { }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel() {
    // cancel core data
    document.getElementById("name").value = "";

    // cancel age-restriction
    document.getElementById("min-date-of-birth").valueAsDate = null;
    document.getElementById("max-date-of-birth").valueAsDate = null;

    // cancel gender-restriction
    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");

    // cancel type-restriction
    let opponmentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponmentTypeElement, "PLAYER");

    // reset ids
    sessionStorage.removeItem('disciplines.current-discipline-id');
    sessionStorage.removeItem('disciplines.current-age-restriction-id');
    sessionStorage.removeItem('disciplines.current-gender-restriction-id');
    sessionStorage.removeItem('disciplines.current-opponent-type-restriction-id');
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToDiscipline() {
    return {
      "id": Number(sessionStorage.getItem('disciplines.current-discipline-id')),
      "name": document.getElementById("name").value,
      "restrictions": [
        this.formToAgeRestriction(),
        this.formToGenderRestriction(),
        this.formToOpponentTypeRestriction()
      ]
    };
  }
  disciplineToForm(discipline) {
    sessionStorage.setItem('disciplines.current-discipline-id', discipline.id);
    document.getElementById("name").value = discipline.name;

    // call method by type of restriction
    let restrictions = discipline.restrictions;
    restrictions.forEach(function (restriction) {
      if (restriction.discriminator === 'A') {
        disciplines.ageRestrictionToForm(restriction);
      }
      if (restriction.discriminator === 'G') {
        disciplines.genderRestrictionToForm(restriction);
      }
      if (restriction.discriminator === 'O') {
        disciplines.opponentTypeRestrictionToForm(restriction);
      }
    });
  }
  formToAgeRestriction() {
    return {
      "id": Number(sessionStorage.getItem('disciplines.current-age-restriction-id')),
      "discriminator": "A",
      "minDateOfBirth": document.getElementById("min-date-of-birth").value,
      "maxDateOfBirth": document.getElementById("max-date-of-birth").value
    };
  }
  ageRestrictionToForm(ageRestriction) {
    sessionStorage.setItem('disciplines.current-age-restriction-id', ageRestriction.id);
    document.getElementById("min-date-of-birth").value = ageRestriction.minDateOfBirth;
    document.getElementById("max-date-of-birth").value = ageRestriction.maxDateOfBirth;
  }
  formToGenderRestriction() {
    let genderElement = document.getElementById("gender");

    return {
      "id": Number(sessionStorage.getItem('disciplines.current-gender-restriction-id')),
      "discriminator": "G",
      "gender": genderElement.options[genderElement.selectedIndex].value
    };
  }
  genderRestrictionToForm(genderRestriction) {
    sessionStorage.setItem('disciplines.current-gender-restriction-id', genderRestriction.id);

    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, genderRestriction.gender);
  }
  formToOpponentTypeRestriction() {
    let opponentTypeElement = document.getElementById("opponent-type");

    return {
      "id": Number(sessionStorage.getItem('disciplines.current-opponent-type-restriction-id')),
      "discriminator": "O",
      "opponentType": opponentTypeElement.options[opponentTypeElement.selectedIndex].value
    };
  }
  opponentTypeRestrictionToForm(opponentTypeRestriction) {
    sessionStorage.setItem('disciplines.current-opponent-type-restriction-id', opponentTypeRestriction.id);

    let opponentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponentTypeElement, opponentTypeRestriction.opponentType);
  }
}

var disciplines = new Disciplines();
