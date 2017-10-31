// 
// load and show persons in a table on loading the page
//
function onload(){

  var offset = document.getElementById("pdOffset").value
  var length = document.getElementById("pdLength").value
  
  fetch('http://localhost:8080/rest/resources/persons?offset='+offset+'&length='+length).then(function(response) {
	return response.json();
  }).then(function(data) {
    // "persons":
    // {"id":1,"firstName":"Marvin","lastName":"SÃ¼ssenguth","gender":"MALE","dateOfBirthISO":"2004-10-28"},
    showPersonsCount()
    
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

function showPersonsCount(){
  fetch('http://localhost:8080/rest/resources/persons/count').then(function(response) {
    return response.json();
    }).then(function(data) {
      document.getElementById("personsCount").innerHTML=data;
    }).catch(function(err) {
  });
}

function next(){
  var currOffset = Number(document.getElementById("pdOffset").value)
  var length = Number(document.getElementById("pdLength").value)
  var newOffset = currOffset + length
  
  document.getElementById("pdOffset").setAttribute("value", newOffset)
  onload();
}

function prev(){
  var currOffset = Number(document.getElementById("pdOffset").value)
  var length = Number(document.getElementById("pdLength").value)
  var newOffset = currOffset - length
  
  if(newOffset<0){
    newOffset = 0;
  }
  
  document.getElementById("pdOffset").setAttribute("value", newOffset)
  onload();
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
  if(!isFormValid()){ return; }
  
  // http://localhost:8080/rest/resources/persons/add?firstName=Sven&lastName=Süssenguth&gender=MALE&dobISO=1968-01-12
  fetch('http://localhost:8080/rest/resources/persons/update',{
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: personDataToJSon()    
  }).then(function(response) {
    return response.json;
  }).then(function(data) {
    console.log(data)
  }).catch(function(err) {
    console.log(err)
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

function personDataToJSon(){
  console.log("personDataToJSon");
  
  var id= document.getElementById("selectedPersonId").value
  var firstName = document.getElementById("pdFirstName").value
  var lastName = document.getElementById("pdLastName").value
  var dateOfBirth = document.getElementById("pdDateOfBirth").value
  
  var genderElement = document.getElementById("pdGender");
  var gender = genderElement.options[genderElement.selectedIndex].value
  
  var json = JSON.stringify({
    "id": Number(id),
    "firstName": firstName,
    "lastName": lastName,
    "dateOfBirthISO": dateOfBirth,
    "gender": gender      
  })
  
  console.log('personDataToJSon:'+json)
  
  return json;
  
}