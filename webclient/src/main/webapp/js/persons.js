// 
// load and show persons in a table on loading the page
//
function onload(){	
  fetch('http://localhost:8080/rest/resources/persons').then(function(response) {
	return response.json();
  }).then(function(data) {
    // "persons":
    // {"id":1,"firstName":"Marvin","lastName":"SÃ¼ssenguth","gender":"MALE","dateOfBirthISO":"2004-10-28"},
    document.getElementById("personsCount").innerHTML=data.persons.length;

    var personRecordTemplate = document.querySelector("#personRecord");
    var tableBody = document.querySelector('#personsTableBody');
	
    data.persons.forEach(function(person){
    var t = document.querySelector("#personRecord").cloneNode(true)		
    var template = t.content
	
    var firstNameElement = template.querySelector("#firstName");
    firstNameElement.innerHTML = person.firstName;
    firstNameElement.setAttribute("id", "firstName"+person.id)
    firstNameElement.onclick = function(e){ showSelectedPersonDetails(person.id); }
		
    var lastNameElement = template.querySelector("#lastName"); 
    lastNameElement.innerHTML = person.lastName;
    lastNameElement.setAttribute("id", "lasttName"+person.id)
		
    tableBody.appendChild(template);
  });
  }).catch(function(err) {
  });
}

//
// show selected persons details
// 
function showSelectedPersonDetails(id){
  fetch('http://localhost:8080/rest/resources/persons/'+id).then(function(response) {
    return response.json();
  }).then(function(data) {
    // "id":1, "firstName":"Sven", "lastName":"SÃ¼ssenguth", "gender":"MALE", "dateOfBirthISO":"1968-01-12"
	document.getElementById("selectedPersonId").setAttribute('value', data.id)
	
    document.getElementById("pdFirstName").setAttribute('value', data.firstName)
    document.getElementById("pdLastName").setAttribute('value', data.lastName)
    document.getElementById("pdDateOfBirth").setAttribute('value', data.dateOfBirthISO)
    
    var genderElement = document.getElementById("pdGender");
	selectItemByValue(genderElement, data.gender);
  }).catch(function(err) {
  });
}

//
// Create new person, send to the server and persist in database.
// After that the table with all persons is updated.
// 
function newPerson(){
	
	if(!isFormValid()){
		  return;
	  }
	
  var firstName = document.getElementById("pdFirstName").value
  var lastName = document.getElementById("pdLastName").value
  var dateOfBirth = document.getElementById("pdDateOfBirth").value
  
  var genderElement = document.getElementById("pdGender");
  var gender = genderElement.options[genderElement.selectedIndex].value
	
  // http://localhost:8080/rest/resources/persons/add?firstName=Sven&lastName=Süssenguth&gender=MALE&dobISO=1968-01-12
  fetch('http://localhost:8080/rest/resources/persons/add?firstName='+firstName+'&lastName='+lastName+'&gender='+gender+'&dobISO='+dateOfBirth).then(function(response) {
    return response;
  }).then(function(data) {
  }).catch(function(err) {
  });

  // neuladen des gesamten Dokumentes nach einem 'post'
  window.location.reload(true);
}

//
// Update persons date and persist in database
// After that the table with all persons is updated.
// 
function updatePerson(){
  if(!isFormValid()){
	  return;
  }
	
  var id= document.getElementById("selectedPersonId").value
  var firstName = document.getElementById("pdFirstName").value
  var lastName = document.getElementById("pdLastName").value
  var gender = document.getElementById("pdGender").value
  var dateOfBirth = document.getElementById("pdDateOfBirth").value
	
  // http://localhost:8080/rest/resources/persons/add?firstName=Sven&lastName=Süssenguth&gender=MALE&dobISO=1968-01-12
  fetch('http://localhost:8080/rest/resources/persons/update?id='+id+'&firstName='+firstName+'&lastName='+lastName+'&gender='+gender+'&dobISO='+dateOfBirth).then(function(response) {
    return response;
  }).then(function(data) {
  }).catch(function(err) {
  });

  // neuladen des gesamten Dokumentes nach einem 'post'
  window.location.reload(true);
}


//
// Validating the form to show client side errors.
// 
function isFormValid(){
	var form = document.getElementById("personsForm");
	if(form.checkValidity()){
		return true;
	}else{
	  document.getElementById("validateFormButton").click();
	  return false;
	}
}

//
// http://www.imranulhoque.com/javascript/javascript-beginners-select-a-dropdown-option-by-value/
//
function selectItemByValue(elmnt, value){
  for(var i=0; i < elmnt.options.length; i++) {
    if(elmnt.options[i].value == value) {
        elmnt.selectedIndex = i;
    }
  }
}