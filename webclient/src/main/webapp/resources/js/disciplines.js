class Disciplines {
  constructor() {
  }
  
  onload(){
    this.cancel();
  }
  
  //--------------------------------------------------------------------------------------------------------------------
  // 
  // save
  //
  //--------------------------------------------------------------------------------------------------------------------
  save(){
    
  }
  
  //--------------------------------------------------------------------------------------------------------------------
  // 
  // cancel
  //
  //--------------------------------------------------------------------------------------------------------------------
  cancel(){
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
  disciplineToJson(){
  }
  disciplineToForm(discipline){
  }
  ageRestrictionToJson(){
  }
  ageRestrictionToForm(ageRestriction){
  }
  genderRestrictionToJson(){
  }
  genderRestrictionToForm(genderRestriction){
  }
  opponentTypeRestrictionToJson(){
  }
  opponentTypeRestrictionToForm(opponentTypeRestriction){
  }
}

var disciplines = new Disciplines();
