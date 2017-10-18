// Laden der Personendaten bei der Anzeige der Seite
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
    firstNameElement.onclick = function(e){ showPersonDetails(person.id); }
		
    var lastNameElement = template.querySelector("#lastName"); 
    lastNameElement.innerHTML = person.lastName;
    lastNameElement.setAttribute("id", "lasttName"+person.id)
		
    tableBody.appendChild(template);
  });
  }).catch(function(err) {
  });
}

// Anzeigen der Personendaten der angeklickten Person aus der Tabelle
function showPersonDetails(id){
  fetch('http://localhost:8080/rest/resources/persons/'+id).then(function(response) {
    return response.json();
  }).then(function(data) {
    // "id":1, "firstName":"Sven", "lastName":"SÃ¼ssenguth", "gender":"MALE", "dateOfBirthISO":"1968-01-12"
    document.getElementById("pdFirstName").setAttribute('value', data.firstName)
    document.getElementById("pdLastName").setAttribute('value', data.lastName)
    document.getElementById("pdGender").setAttribute('value', data.gender)
    document.getElementById("pdDateOfBirth").setAttribute('value', data.dateOfBirthISO)
  }).catch(function(err) {
  });
}

// Anlegen einer neuen Person, mit den eingegebenen Daten
function newPerson(){
  var firstName = document.getElementById("pdFirstName").value
  var lastName = document.getElementById("pdLastName").value
  var gender = document.getElementById("pdGender").value
  var dateOfBirth = document.getElementById("pdDateOfBirth").value
	
  // http://localhost:8080/rest/resources/persons/add?firstName=Sven&lastName=Süssenguth&gender=MALE&dobISO=1968-01-12
  fetch('http://localhost:8080/rest/resources/persons/add?firstName='+firstName+'&lastName='+lastName+'&gender='+gender+'&dobISO='+dateOfBirth).then(function(response) {
    return response;
  }).then(function(data) {
  }).catch(function(err) {
  });

  // neuladen des gesamten Dokumentes nach einem 'post'
  window.location.reload(true);
}
