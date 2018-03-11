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

  }

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
  }
  disciplineToForm(discipline) {
  }
  formToAgeRestriction() {
    var json = {
      "id": sessionStorage.getItem('disciplines.current-discipline-id'),
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
    var genderElement = document.getElementById("valid-gender");

    var json = {
      "id": sessionStorage.getItem('disciplines.current-gender-restriction-id'),
      "validGender": genderElement.options[genderElement.selectedIndex].value
    };

    return json;
  }
  genderRestrictionToForm(genderRestriction) {
    sessionStorage.setItem('disciplines.current-gender-restriction-id', genderRestriction.id);

    var genderElement = document.getElementById("valid-gender");
    selectItemByValue(genderElement, genderRestriction.validGender);
  }
  formToOpponentTypeRestriction() {
    var opponentTypeElement = document.getElementById("valid-opponent-type");

    var json = {
      "id": sessionStorage.getItem('disciplines.current-opponent-type-restriction-id'),
      "validGender": opponentTypeElement.options[opponentTypeElement.selectedIndex].value
    };

    return json;
  }
  opponentTypeRestrictionToForm(opponentTypeRestriction) {
    sessionStorage.setItem('disciplines.current-opponent-type-restriction-id', opponentTypeRestriction.id);

    var opponentTypeElement = document.getElementById("valid-opponent-type");
    selectItemByValue(opponentTypeElement, opponentTypeRestriction.validOpponentType);
  }
}

var disciplines = new Disciplines();
