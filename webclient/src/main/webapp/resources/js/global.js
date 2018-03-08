//
// Validating the form to show client side errors.
// no business logic is called, just formal errors.
// 
function isFormValid(formId){
  var form = document.getElementById(formId);
  if(form.checkValidity()){
    return true;
  }else{
    document.getElementById("validateFormButton").click();
    return false;
  }
}

//
//http://www.imranulhoque.com/javascript/javascript-beginners-select-a-dropdown-option-by-value/
//
function selectItemByValue(elmnt, value){
  for(var i=0; i < elmnt.options.length; i++) {
    if(elmnt.options[i].value === value) {
      elmnt.selectedIndex = i;
    }
  }
}

//
// Es muss ein aktives Turnier ausgewählt sein, bevor man weitere Eingaben
// (andere Seiten) anzeigen kann.
//
function activateNavigation() {
  var activeNavigation = new Tournaments().isActiveTournament();
  if(!activeNavigation) {
    // text soll von allen optional-Links angezeigt werden, aber kein a href vorhanden sein
    // https://www.w3schools.com/jsref/met_document_getelementsbyclassname.asp
    var navOptional = document.getElementsByClassName("nav-optional");
    var i;
    for (i = 0; i < navOptional.length; i++) {
      navOptional[i].removeAttribute("href");
    }
  }
}

//https://stackoverflow.com/questions/979975/how-to-get-the-value-from-the-get-parameters
function getUrlVars() {
  var vars = {};
  window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}