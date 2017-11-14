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
  // Navigation
  var navsImport = document.getElementById('nav');
  var navs = navsImport.import;
  var nav = navs.getElementById("nav-wrapper")
  var navClone = document.importNode(nav.content, true);
  
  fragments = document.getElementById('fragments');  
  fragments.appendChild(navClone);
}