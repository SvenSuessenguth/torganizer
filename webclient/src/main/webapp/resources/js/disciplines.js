/* global disciplinesResource, tournamentsResource, tournaments */

class Disciplines {
  constructor() {
  }

  onload() {
    this.cancel();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save() {
    var discipline = this.formToDiscipline();
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
    var tournamentId = tournaments.getCurrentTournamentId();
    var disciplineId = json.id;
    tournamentsResource.addDiscipline(tournamentId, disciplineId, disciplines.addDisciplineResolve, disciplines.addDisciplineReject);
  }
  createReject(json) { }
  addDisciplineResolve(json) {
    disciplines.cancel();
  }
  addDisciplineReject(json) {}

  update(discipline) {
    disciplinesResource.createOrUpdate(discipline, "PUT", this.createResolve, this.createReject);
  }
  updateResolve(json) { }
  updateReject(json) { }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel() {
    // cancel core data
    document.getElementById("discipline-name").value = "";

    // cancel age-restriction
    document.getElementById("min-date-of-birth").valueAsDate = null;
    document.getElementById("max-date-of-birth").valueAsDate = null;

    // cancel gender-restriction
    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, "MALE");

    // cancel type-restriction
    var opponmentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponmentTypeElement, "PLAYER");
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // converting from/to json/form
  //
  //--------------------------------------------------------------------------------------------------------------------
  formToDiscipline() {
    var json = {
      "id": sessionStorage.getItem('disciplines.current-discipline-id'),
      "name": document.getElementById("name").value,
      "restrictions": [
        this.formToAgeRestriction(),
        this.formToGenderRestriction(),
        this.formToOpponentTypeRestriction()
      ]
    };

    return json;
  }
  disciplineToForm(discipline) {
  }
  formToAgeRestriction() {
    var json = {
      "id": sessionStorage.getItem('disciplines.current-age-restriction-id'),
      "discriminator": "A",
      "minDateOfBirth": document.getElementById("min-date-of-birth").value,
      "maxDateOfBirth": document.getElementById("max-date-of-birth").value
    };

    return json;
  }
  ageRestrictionToForm(ageRestriction) {
    sessionStorage.setItem('disciplines.current-age-restriction-id', ageRestriction.id);
    document.getElementById("min-date-of-birth").value = ageRestriction.minDateOfBirth;
    document.getElementById("max-date-of-birth").value = ageRestriction.maxDateOfBirth;
  }
  formToGenderRestriction() {
    var genderElement = document.getElementById("gender");

    var json = {
      "id": sessionStorage.getItem('disciplines.current-gender-restriction-id'),
      "discriminator": "G",
      "gender": genderElement.options[genderElement.selectedIndex].value
    };

    return json;
  }
  genderRestrictionToForm(genderRestriction) {
    sessionStorage.setItem('disciplines.current-gender-restriction-id', genderRestriction.id);

    var genderElement = document.getElementById("gender");
    selectItemByValue(genderElement, genderRestriction.gender);
  }
  formToOpponentTypeRestriction() {
    var opponentTypeElement = document.getElementById("opponent-type");

    var json = {
      "id": sessionStorage.getItem('disciplines.current-opponent-type-restriction-id'),
      "discriminator": "O",
      "opponentType": opponentTypeElement.options[opponentTypeElement.selectedIndex].value
    };

    return json;
  }
  opponentTypeRestrictionToForm(opponentTypeRestriction) {
    sessionStorage.setItem('disciplines.current-opponent-type-restriction-id', opponentTypeRestriction.id);

    var opponentTypeElement = document.getElementById("opponent-type");
    selectItemByValue(opponentTypeElement, opponentTypeRestriction.opponentType);
  }
}

var disciplines = new Disciplines();
