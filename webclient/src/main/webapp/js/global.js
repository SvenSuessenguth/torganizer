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

// http://blog.teamtreehouse.com/introduction-html-imports
function includeFragments(){
  var fragments = document.getElementById('fragments');
  
  // Navigation
  var navsImport = document.getElementById('nav');
  var navs = navsImport.import;
  var nav = navs.getElementById("nav-wrapper")
  var navClone = document.importNode(nav.content, true);
  fragments.appendChild(navClone);
  
  activateNavigation();
  
  // Footer
  var footersImport = document.getElementById('footer');
  var footers = footersImport.import;
  var footer = footers.getElementById("footer-wrapper")
  var footerClone = document.importNode(footer.content, true);
  
  fragments.appendChild(footerClone);
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