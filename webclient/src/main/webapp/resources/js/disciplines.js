/* global disciplinesResource, tournamentsResource, tournaments */

class Disciplines {
  constructor() {
  }

  onload() {
    this.initSessionStorage(null);
    this.initSelect();
    this.initOpponents();
    this.initAssignableOpponents();
    this.initRestrictions();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize with values from sessionStorage
  //
  //--------------------------------------------------------------------------------------------------------------------
  // initilize with default values only if nothing is already in sessionStorage
  initSessionStorage(selectedDisciplineId){
    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');

    if(currentDisciplineId===null) {
      sessionStorage.removeItem('disciplines.current-discipline.id');

      sessionStorage.setItem('disciplines.opponents-table.offset', 0);
      sessionStorage.setItem('disciplines.assignable-opponents-table.offset', 0);

      sessionStorage.removeItem('disciplines.current-age-restriction.id');
      sessionStorage.removeItem('disciplines.current-gender-restriction.id');
      sessionStorage.removeItem('disciplines.current-opponent-type-restriction.id');
    }
  }
  initSelect(){
    let tournamentId = tournaments.getId();
    tournamentsResource.getDisciplines(tournamentId, this.initSelectResolve, this.initSelectReject)
  }
  initSelectResolve(json) {
    let dSelect = document.getElementById("disciplines");
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');

    // remove all optione before adding new ones
    while (dSelect.options.length > 0) {
      dSelect.remove(0);
    }

    // add an empty option to force an onselect event
    let option = document.createElement("option");
    option.text = "select...";
    option.value = "select";
    option.id= "select";
    if(disciplineId===null){
      option.selected = 'selected';
    }
    dSelect.appendChild(option);


    // add an option for every discipline
    json.forEach(function (discipline) {
      let option = document.createElement("option");
      option.text = discipline.name;
      option.value = discipline.id;
      option.id = discipline.id;
      dSelect.appendChild(option);

      if(disciplineId===discipline.id.toString()){
        option.selected = "selected";
      }
    });
  }
  initSelectReject(json) {}

  showSelectDiscipline() {
    let dSelect = document.getElementById("disciplines");
    let disciplineId = dSelect.options[dSelect.selectedIndex].value;
    this.showDiscipline(disciplineId);
  }
  showDiscipline(disciplineId){
    if(disciplineId==="select"){
      disciplines.cancel();
      return;
    }

    let tournamentId = tournaments.getId();
    let opponentsOffset = sessionStorage.getItem('disciplines.opponents-table.offset');
    let opponentsMaxResults = document.getElementById("opponents-table").getAttribute("rows");
    let assignableOpponentsOffset = sessionStorage.getItem('disciplines.assignable-opponents-table.offset');
    let assignableOpponentsMaxResults = document.getElementById("assignable-opponents-table").getAttribute("rows");

    sessionStorage.setItem('disciplines.current-discipline.id', disciplineId);

    disciplinesResource.readSingle(disciplineId, this.showSelectedDisciplineResolve, this.showSelectedDisciplineReject);
    disciplinesResource.getOpponents(disciplineId, opponentsOffset, opponentsMaxResults, disciplines.updateOpponentsResolve, disciplines.updateOpponentsReject);
    tournamentsResource.assignableOpponents(tournamentId, disciplineId, assignableOpponentsOffset, assignableOpponentsMaxResults, this.updateAssignableOpponentsResolve, this.updateAssignableOpponentsReject);
  }

  showSelectedDisciplineResolve(json) {
    disciplines.disciplineToForm(json);
  }
  showSelectedDisciplineReject(json) {}


  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  initOpponents(){
    this.updateOpponents();
    document.getElementById("opponents-table").addEventListener("opponent-selected", this.opponentSelectedFromOpponents);
  }

  opponentSelectedFromOpponents(event){
    console.log("remove opponent "+event.detail+" from discipline "+sessionStorage.getItem('disciplines.current-discipline.id'));
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline.id'));
    let opponentId = event.detail;
    disciplinesResource.removeOpponent(disciplineId, opponentId, disciplines.removeOpponentResolve, disciplines.removeOpponentReject);
  }
  removeOpponentResolve(json){
    disciplines.updateOpponents();
    disciplines.initAssignableOpponents();
  }
  removeOpponentReject(json){}

  updateOpponents(){
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline.id'));
    let maxResults = document.getElementById("opponents-table").getAttribute("rows");
    let offset = sessionStorage.getItem('disciplines.current-discipline.offset');

    disciplinesResource.getOpponents(disciplineId, offset, maxResults, disciplines.updateOpponentsResolve, disciplines.updateOpponentsReject);
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
    sessionStorage.setItem("disciplines.assignable-opponents-table.offset", 0);
    this.updateAssignableOpponents();
    document.getElementById("assignable-opponents-table").addEventListener("opponent-selected", this.opponentSelectedFromAssignableOpponents);
  }

  updateAssignableOpponents(){
    let tournamentId = tournaments.getId();
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    let maxResults = document.getElementById("assignable-opponents-table").getAttribute("rows");
    let offset = sessionStorage.getItem('disciplines.assignable-opponents-table.offset');

    if(disciplineId===null){
      return;
    }
    disciplineId = Number(disciplineId);

    tournamentsResource.assignableOpponents(tournamentId, disciplineId, offset, maxResults, this.updateAssignableOpponentsResolve, this.updateAssignableOpponentsReject );
  }
  updateAssignableOpponentsResolve(json){
    document.getElementById("assignable-opponents-table").setAttribute("data", JSON.stringify(json));
  }
  updateAssignableOpponentsReject(json){}


  opponentSelectedFromAssignableOpponents(event){
    console.log("add opponent "+event.detail+" to discipline "+Number(sessionStorage.getItem('disciplines.current-discipline.id')));
    let disciplineId = Number(sessionStorage.getItem('disciplines.current-discipline.id'));
    let opponentId = event.detail;
    disciplinesResource.addOpponent(disciplineId, opponentId, disciplines.addOpponentResolve, disciplines.addOpponentReject);
  }
  addOpponentResolve(json){
    disciplines.initAssignableOpponents();
    disciplines.initOpponents();
  }
  addOpponentReject(text){ console.log("error while adding opponent"); }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize restrictions
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRestrictions(){
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    if(disciplineId===null){ return;}
    else{ disciplineId = Number(disciplineId); }

    disciplinesResource.readSingle(disciplineId, this.initRestrictionsResolve, this.initRestrictionsReject);
  }
  initRestrictionsResolve(json){
    disciplines.disciplineToForm(json);
  }
  initRestrictionsReject(json){}

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save() {
    let discipline = this.formToDiscipline();
    if (discipline.id === null) {
      this.create(discipline);
    } else {
      this.update(discipline);
    }
  }
  create(discipline) {
    disciplinesResource.createOrUpdate(discipline, "POST", disciplines.createResolve, disciplines.createReject);
  }
  createResolve(json) {
    let tournamentId = tournaments.getId();
    let disciplineId = json.id;
    sessionStorage.setItem('disciplines.current-discipline.id', disciplineId);

    tournamentsResource.addDiscipline(tournamentId, disciplineId, disciplines.addDisciplineResolve, disciplines.addDisciplineReject);
  }
  createReject(json) { }
  addDisciplineResolve(json) {
    disciplines.initSelect();
    disciplines.initOpponents();
    disciplines.initAssignableOpponents();
    disciplines.initRestrictions();
  }
  addDisciplineReject(json) {}

  update(discipline) {
    disciplinesResource.createOrUpdate(discipline, "PUT", disciplines.updateResolve, disciplines.updateReject);
  }
  updateResolve(json) {
    disciplines.initSelect();
    disciplines.initOpponents();
    disciplines.initAssignableOpponents();
    disciplines.initRestrictions();
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
    sessionStorage.removeItem('disciplines.current-discipline.id');
    sessionStorage.setItem('disciplines.opponents-table.offset', 0);
    sessionStorage.setItem('disciplines.assignable-opponents-table.offset', 0);

    // cancel select
    document.getElementById("disciplines").selectedIndex = 0;

    // cancel age-restriction
    document.getElementById("min-date-of-birth").valueAsDate = null;
    document.getElementById("max-date-of-birth").valueAsDate = null;
    sessionStorage.removeItem('disciplines.current-age-restriction.id');

    // cancel gender-restriction
    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");
    sessionStorage.removeItem('disciplines.current-gender-restriction.id');

    // cancel type-restriction
    let opponmentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponmentTypeElement, "PLAYER");
    sessionStorage.removeItem('disciplines.current-opponent-type-restriction.id');

    // clear tables
    document.getElementById("opponents-table").setAttribute("data", "[]");
    document.getElementById("assignable-opponents-table").setAttribute("data", "[]");
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToDiscipline() {
    let disciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    if(disciplineId!==null){
      disciplineId = Number(disciplineId);
    }
    return {
      "id": disciplineId,
      "name": document.getElementById("name").value,
      "restrictions": [
        this.formToAgeRestriction(),
        this.formToGenderRestriction(),
        this.formToOpponentTypeRestriction()
      ]
    };
  }
  disciplineToForm(discipline) {
    sessionStorage.setItem('disciplines.current-discipline.id', discipline.id);
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
    let ageRestrictionId = sessionStorage.getItem('disciplines.current-age-restriction.id');
    ageRestrictionId = ageRestrictionId===null?null:Number(ageRestrictionId);

    return {
      "id": ageRestrictionId,
      "discriminator": "A",
      "minDateOfBirth": document.getElementById("min-date-of-birth").value,
      "maxDateOfBirth": document.getElementById("max-date-of-birth").value
    };
  }
  ageRestrictionToForm(ageRestriction) {
    sessionStorage.setItem('disciplines.current-age-restriction.id', ageRestriction.id);
    document.getElementById("min-date-of-birth").value = ageRestriction.minDateOfBirth;
    document.getElementById("max-date-of-birth").value = ageRestriction.maxDateOfBirth;
  }
  formToGenderRestriction() {
    let genderElement = document.getElementById("gender");
    let genderRestrictionId = sessionStorage.getItem('disciplines.current-gender-restriction.id');
    genderRestrictionId = genderRestrictionId===null?null:Number(genderRestrictionId);

    return {
      "id": genderRestrictionId,
      "discriminator": "G",
      "gender": genderElement.options[genderElement.selectedIndex].value
    };
  }
  genderRestrictionToForm(genderRestriction) {
    sessionStorage.setItem('disciplines.current-gender-restriction.id', genderRestriction.id);

    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, genderRestriction.gender);
  }
  formToOpponentTypeRestriction() {
    let opponentTypeElement = document.getElementById("opponent-type");
    let opponentTypeRestricionId = sessionStorage.getItem('disciplines.current-opponent-type-restriction.id');
    opponentTypeRestricionId = opponentTypeRestricionId===null?null:Number(opponentTypeRestricionId);

    return {
      "id": opponentTypeRestricionId,
      "discriminator": "O",
      "opponentType": opponentTypeElement.options[opponentTypeElement.selectedIndex].value
    };
  }
  opponentTypeRestrictionToForm(opponentTypeRestriction) {
    sessionStorage.setItem('disciplines.current-opponent-type-restriction.id', opponentTypeRestriction.id);

    let opponentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponentTypeElement, opponentTypeRestriction.opponentType);
  }
}

var disciplines = new Disciplines();
