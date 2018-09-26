/* global disciplinesResource, tournamentsResource, tournaments */

/*
variables in sessions-storage:
------------------------------------------------------------------------------------------------------------------------
disciplines.discipline.id
disciplines.opponents-table.offset
disciplines.assignable-opponents-table.offset
disciplines.age-restriction.id
disciplines.gender-restriction.id
disciplines.opponent-type-restriction.id

*/
let disciplines = {

  onload : function onload() {
    disciplines.initSessionStorage(null);
    disciplines.initUI();
  },

  initUI : function initUI(){
    disciplines.initSelect();
    disciplines.initOpponents();
    disciplines.initAssignableOpponents();
    disciplines.initRestrictions();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize with values from sessionStorage
  //
  //--------------------------------------------------------------------------------------------------------------------
  // initilize with default values only if nothing is already in sessionStorage
  initSessionStorage : function initSessionStorage(selectedDisciplineId){
    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');

    if(currentDisciplineId===null) {
      sessionStorage.removeItem('disciplines.discipline.id');

      sessionStorage.setItem('disciplines.opponents-table.offset', 0);
      sessionStorage.setItem('disciplines.assignable-opponents-table.offset', 0);

      sessionStorage.removeItem('disciplines.age-restriction.id');
      sessionStorage.removeItem('disciplines.gender-restriction.id');
      sessionStorage.removeItem('disciplines.opponent-type-restriction.id');
    }
  },
  initSelect : function initSelect(){
    let tournamentId = tournaments.getId();
    tournamentsResource.getDisciplines(tournamentId, disciplines.initSelectResolve)
  },
  initSelectResolve : function initSelectResolve(json) {
    let dSelect = document.getElementById("disciplines");
    let disciplineId = sessionStorage.getItem('disciplines.discipline.id');

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
  },

  showSelectDiscipline : function showSelectDiscipline() {
    let dSelect = document.getElementById("disciplines");
    let disciplineId = dSelect.options[dSelect.selectedIndex].value;
    disciplines.showDiscipline(disciplineId);
  },
  showDiscipline : function showDiscipline(disciplineId){
    if(disciplineId==="select"){
      disciplines.cancel();
      return;
    }

    let tournamentId = tournaments.getId();
    let opponentsOffset = sessionStorage.getItem('disciplines.opponents-table.offset');
    let opponentsMaxResults = document.getElementById("opponents-table").getAttribute("rows");
    let assignableOpponentsOffset = sessionStorage.getItem('disciplines.assignable-opponents-table.offset');
    let assignableOpponentsMaxResults = document.getElementById("assignable-opponents-table").getAttribute("rows");

    sessionStorage.setItem('disciplines.discipline.id', disciplineId);

    restResourceAdapter.getSingle("disciplines", disciplineId, disciplines.showSelectedDisciplineResolve);
    disciplinesResource.getOpponents(disciplineId, opponentsOffset, opponentsMaxResults, disciplines.updateOpponentsResolve);
    tournamentsResource.assignableOpponents(tournamentId, disciplineId, assignableOpponentsOffset, assignableOpponentsMaxResults, disciplines.updateAssignableOpponentsResolve);
  },
  showSelectedDisciplineResolve : function showSelectedDisciplineResolve(json) {
    disciplines.disciplineToForm(json);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  initOpponents : function initOpponents(){
    disciplines.updateOpponents();
    document.getElementById("opponents-table").addEventListener("opponent-selected", disciplines.opponentSelectedFromOpponents);
  },

  opponentSelectedFromOpponents : function opponentSelectedFromOpponents(event){
    console.log("remove opponent "+event.detail+" from discipline "+sessionStorage.getItem('disciplines.discipline.id'));
    let disciplineId = Number(sessionStorage.getItem('disciplines.discipline.id'));
    let opponentId = event.detail;
    disciplinesResource.removeOpponent(disciplineId, opponentId, disciplines.removeOpponentResolve);
  },
  removeOpponentResolve : function removeOpponentResolve(json){
    disciplines.updateOpponents();
    disciplines.initAssignableOpponents();
  },

  updateOpponents : function updateOpponents(){
    let disciplineId = Number(sessionStorage.getItem('disciplines.discipline.id'));
    let maxResults = document.getElementById("opponents-table").getAttribute("rows");
    let offset = sessionStorage.getItem('disciplines.discipline.offset');

    disciplinesResource.getOpponents(disciplineId, offset, maxResults, disciplines.updateOpponentsResolve);
  },
  updateOpponentsResolve : function updateOpponentsResolve(json){
    document.getElementById("opponents-table").setAttribute("data", JSON.stringify(json));
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize and actions for assignableOpponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  initAssignableOpponents : function initAssignableOpponents() {
    sessionStorage.setItem("disciplines.assignable-opponents-table.offset", 0);
    disciplines.updateAssignableOpponents();
    document.getElementById("assignable-opponents-table").addEventListener("opponent-selected", disciplines.opponentSelectedFromAssignableOpponents);
  },

  updateAssignableOpponents : function updateAssignableOpponents(){
    let tournamentId = tournaments.getId();
    let disciplineId = sessionStorage.getItem('disciplines.discipline.id');
    let maxResults = document.getElementById("assignable-opponents-table").getAttribute("rows");
    let offset = sessionStorage.getItem('disciplines.assignable-opponents-table.offset');

    if(disciplineId===null){
      return;
    }
    disciplineId = Number(disciplineId);

    tournamentsResource.assignableOpponents(tournamentId, disciplineId, offset, maxResults, disciplines.updateAssignableOpponentsResolve);
  },
  updateAssignableOpponentsResolve : function updateAssignableOpponentsResolve(json){
    document.getElementById("assignable-opponents-table").setAttribute("data", JSON.stringify(json));
  },

  opponentSelectedFromAssignableOpponents : function opponentSelectedFromAssignableOpponents(event){
    let disciplineId = Number(sessionStorage.getItem('disciplines.discipline.id'));
    console.log("add opponent "+event.detail+" to discipline "+ disciplineId);
    let opponentId = event.detail;
    disciplinesResource.addOpponent(disciplineId, opponentId, disciplines.addOpponentResolve);
  },
  addOpponentResolve : function addOpponentResolve(json){
    disciplines.initAssignableOpponents();
    disciplines.initOpponents();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // initialize restrictions
  //
  //--------------------------------------------------------------------------------------------------------------------
  initRestrictions : function initRestrictions(){
    let disciplineId = sessionStorage.getItem('disciplines.discipline.id');
    if(disciplineId===null){ return;}
    else{ disciplineId = Number(disciplineId); }

    restResourceAdapter.getSingle("disciplines", disciplineId, disciplines.initRestrictionsResolve);
  },
  initRestrictionsResolve : function initRestrictionsResolve(json){
    disciplines.disciplineToForm(json);
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save : function save() {
    let discipline = disciplines.formToDiscipline();
    if (discipline.id === null) {
      disciplines.create(discipline);
    } else {
      disciplines.update(discipline);
    }
  },
  create : function create(discipline) {
    restResourceAdapter.createOrUpdate("disciplines", discipline, disciplines.createResolve);
  },
  createResolve : function createResolve(json) {
    let tournamentId = tournaments.getId();
    let disciplineId = json.id;
    sessionStorage.setItem('disciplines.discipline.id', disciplineId);
    tournamentsResource.addDiscipline(tournamentId, disciplineId, disciplines.addDisciplineResolve);
  },
  addDisciplineResolve : function addDisciplineResolve(json) {
    disciplines.initUI();
  },

  update : function update(discipline) {
    restResourceAdapter.createOrUpdate("disciplines", discipline, disciplines.updateResolve);
  },
  updateResolve : function updateResolve(json) {
    disciplines.initUI();
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel : function cancel() {
    // cancel core data
    document.getElementById("name").value = "";
    sessionStorage.removeItem('disciplines.discipline.id');
    sessionStorage.setItem('disciplines.opponents-table.offset', 0);
    sessionStorage.setItem('disciplines.assignable-opponents-table.offset', 0);

    // cancel select
    document.getElementById("disciplines").selectedIndex = 0;

    // cancel age-restriction
    document.getElementById("min-date-of-birth").valueAsDate = null;
    document.getElementById("max-date-of-birth").valueAsDate = null;
    sessionStorage.removeItem('disciplines.age-restriction.id');

    // cancel gender-restriction
    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");
    sessionStorage.removeItem('disciplines.gender-restriction.id');

    // cancel type-restriction
    let opponmentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponmentTypeElement, "PLAYER");
    sessionStorage.removeItem('disciplines.opponent-type-restriction.id');

    // clear tables
    document.getElementById("opponents-table").setAttribute("data", "[]");
    document.getElementById("assignable-opponents-table").setAttribute("data", "[]");
  },

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToDiscipline : function formToDiscipline() {
    let disciplineId = sessionStorage.getItem('disciplines.discipline.id');
    if(disciplineId!==null){
      disciplineId = Number(disciplineId);
    }
    return {
      "id": disciplineId,
      "name": document.getElementById("name").value,
      "restrictions": [
        disciplines.formToAgeRestriction(),
        disciplines.formToGenderRestriction(),
        disciplines.formToOpponentTypeRestriction()
      ]
    };
  },
  disciplineToForm : function disciplineToForm(discipline) {
    sessionStorage.setItem('disciplines.discipline.id', discipline.id);
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
  },
  formToAgeRestriction : function formToAgeRestriction() {
    let ageRestrictionId = sessionStorage.getItem('disciplines.age-restriction.id');
    ageRestrictionId = ageRestrictionId===null?null:Number(ageRestrictionId);

    return {
      "id": ageRestrictionId,
      "discriminator": "A",
      "minDateOfBirth": document.getElementById("min-date-of-birth").value,
      "maxDateOfBirth": document.getElementById("max-date-of-birth").value
    };
  },
  ageRestrictionToForm : function ageRestrictionToForm(ageRestriction) {
    sessionStorage.setItem('disciplines.age-restriction.id', ageRestriction.id);
    document.getElementById("min-date-of-birth").value = ageRestriction.minDateOfBirth;
    document.getElementById("max-date-of-birth").value = ageRestriction.maxDateOfBirth;
  },
  formToGenderRestriction : function formToGenderRestriction() {
    let genderElement = document.getElementById("gender");
    let genderRestrictionId = sessionStorage.getItem('disciplines.gender-restriction.id');
    genderRestrictionId = genderRestrictionId===null?null:Number(genderRestrictionId);

    return {
      "id": genderRestrictionId,
      "discriminator": "G",
      "gender": genderElement.options[genderElement.selectedIndex].value
    };
  },
  genderRestrictionToForm : function genderRestrictionToForm(genderRestriction) {
    sessionStorage.setItem('disciplines.gender-restriction.id', genderRestriction.id);

    let genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, genderRestriction.gender);
  },
  formToOpponentTypeRestriction : function formToOpponentTypeRestriction() {
    let opponentTypeElement = document.getElementById("opponent-type");
    let opponentTypeRestricionId = sessionStorage.getItem('disciplines.opponent-type-restriction.id');
    opponentTypeRestricionId = opponentTypeRestricionId===null?null:Number(opponentTypeRestricionId);

    return {
      "id": opponentTypeRestricionId,
      "discriminator": "O",
      "opponentType": opponentTypeElement.options[opponentTypeElement.selectedIndex].value
    };
  },
  opponentTypeRestrictionToForm : function opponentTypeRestrictionToForm(opponentTypeRestriction) {
    sessionStorage.setItem('disciplines.opponent-type-restriction.id', opponentTypeRestriction.id);

    let opponentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponentTypeElement, opponentTypeRestriction.opponentType);
  },
};

