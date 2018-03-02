class Disciplines {
  constructor() {
  }
  
  onload(){
    includeFragments();
  }
  
  save(){
    
  }
 
  cancel(){
    // cancel core data
    document.getElementById("discipline-name").value = "";
    
    // cancel age-restriction
    document.getElementById("min-date-of-birth").valueAsDate = null;
    document.getElementById("max-date-of-birth").valueAsDate = null;
    
    // cancel gender-restriction
     
    // cancel type-restriction
  }
}

var disciplines = new Disciplines();
