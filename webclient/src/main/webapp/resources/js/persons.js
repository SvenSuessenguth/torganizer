var tableSize = Number(2);

function onLoad(){
  showPersonsTable();
}

// 
// load and show persons in a table on loading the page
//
function showPersonsTable(){

  var offset = Number(sessionStorage.getItem('persons-table-offset'));
  
  document.getElementById("personsOffset").innerHTML = offset;
  document.getElementById("personsLength").innerHTML = offset + tableSize;
  
  fetch('http://localhost:8080/rest/resources/persons/all?offset='+offset+'&length='+tableSize).then(function(response) {
	return response.json();
  }).then(function(data) {
    // [{"id":1,"firstName":"Marvin","lastName":"SÃ¼ssenguth","gender":"MALE","dateOfBirthISO":"2004-10-28"}, ]
    showPersonsCount()
    
    // bisherige Daten entfernen, damit keine doppelten Anzeigen erscheinen
    // https://stackoverflow.com/questions/3955229/remove-all-child-elements-of-a-dom-node-in-javascript
    var tableBody = document.querySelector('#personsTableBody');
    while (tableBody.firstChild) {
      tableBody.removeChild(tableBody.firstChild);
    }
    
    data.forEach(function(person){
      var t = document.querySelector("#personRecord").cloneNode(true);
      var template = t.content;
	
      var firstNameElement = template.querySelector("#firstName");
      firstNameElement.innerHTML = person.firstName;
      firstNameElement.setAttribute("id", "firstName"+person.id);
      firstNameElement.onclick = function(e){ showSelectedPersonDetails(person.id); };
		
      var lastNameElement = template.querySelector("#lastName"); 
      lastNameElement.innerHTML = person.lastName;
      lastNameElement.setAttribute("id", "lasttName"+person.id);
		
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
  var personsCount = Number(document.getElementById("personsCount").innerHTML);
  var currOffset = Number(sessionStorage.getItem('persons-table-offset'));
  var newOffset = currOffset + tableSize;
  
  if(newOffset>=personsCount){
    return;
  }
  
  document.getElementById("personsOffset").innerHTML = newOffset;
  sessionStorage.setItem('persons-table-offset', newOffset);
  showPersonsTable();
}

function prev(){
  var currOffset = Number(sessionStorage.getItem('persons-table-offset'));
  var newOffset = currOffset - tableSize;
  
  if(newOffset<0){
    newOffset = 0;
  }
  
  document.getElementById("personsOffset").innerHTML = newOffset;
  document.getElementById("personsLength").innerHTML = newOffset + tableSize;
  sessionStorage.setItem('persons-table-offset', newOffset);
  showPersonsTable();
}

//
// show selected persons details
// 
function showSelectedPersonDetails(id){
  fetch('http://localhost:8080/rest/resources/persons/'+id).then(function(response) {
    return response.json();
  }).then(function(data) {
    
    // "id":1, "firstName":"Sven", "lastName":"Süssenguth", "gender":"MALE", "dateOfBirth":"1968-01-12"
    sessionStorage.setItem('persons-current-person-id', data.id);
	
    document.getElementById("pdFirstName").setAttribute('value', data.firstName);
    document.getElementById("pdLastName").setAttribute('value', data.lastName);
    document.getElementById("pdDateOfBirth").setAttribute('value', data.dateOfBirth);
    
    var genderElement = document.getElementById("pdGender");
    selectItemByValue(genderElement, data.gender);
	
  }).catch(function(err) {
  });
}

//
// Create new person, send to the server and persist in database.
// After that the table with all persons is updated.
// 
function createPerson(){
	
	if(!isFormValid("personsForm")){
		  return;
	  }
	
  fetch('http://localhost:8080/rest/resources/persons',{
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: personDataToJSon()
  }).then(function(response) {
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
  if(!isFormValid("personsForm")){ return; }
  
  var id= Number(sessionStorage.getItem('persons-current-person-id'));
  fetch('http://localhost:8080/rest/resources/persons/'+id,{
    method: "PUT",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: personDataToJSon()    
  }).then(function(response) {
    return response.json;
  }).then(function(data) {
    console.log(data);
  }).catch(function(err) {
    console.log(err);
  });

  // neuladen des gesamten Dokumentes nach einem 'post'
  window.location.reload(true);
}

//
// Delete person from database
// After that the table with all persons is updated.
//
function deletePerson(){
  
  if(!isFormValid("personsForm")){ return; }

  var id= Number(sessionStorage.getItem('persons-current-person-id'));

  fetch('http://localhost:8080/rest/resources/persons/'+id,{
    method: "DELETE"
  }).then(function(response) {
    return response.json;
  }).then(function(data) { 
  }).catch(function(err) {
  });

  // neuladen des gesamten Dokumentes nach einem 'delete'
  window.location.reload(true);
}

function personDataToJSon(){
  var id= Number(sessionStorage.getItem('persons-current-person-id'));
  var firstName = document.getElementById("pdFirstName").value;
  var lastName = document.getElementById("pdLastName").value;
  var dateOfBirth = document.getElementById("pdDateOfBirth").value;
  
  var genderElement = document.getElementById("pdGender");
  var gender = genderElement.options[genderElement.selectedIndex].value;
  
  var json = JSON.stringify({
    "id": Number(id),
    "firstName": firstName,
    "lastName": lastName,
    "dateOfBirth": dateOfBirth,
    "gender": gender      
  });
  
  return json;  
}