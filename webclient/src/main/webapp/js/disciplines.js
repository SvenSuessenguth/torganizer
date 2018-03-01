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
    
    // cancel gender-restriction
     
    // cancel type-restriction
  }
}

var disciplines = new Disciplines();
