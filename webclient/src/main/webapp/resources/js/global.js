const getHeader = {
  method: "GET",
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }
};

//
// Validating the form to show client side errors.
// no business logic is called, just formal errors.
// 
function isFormValid(formId) {
  let form = document.getElementById(formId);
  if (form.checkValidity()) {
    return true;
  } else {
    document.getElementById("validateFormButton").click();
    return false;
  }
}

//
//http://www.imranulhoque.com/javascript/javascript-beginners-select-a-dropdown-option-by-value/
//
function selectItemByValue(eSelect, value) {
  for (let i = 0; i < eSelect.options.length; i++) {
    if (eSelect.options[i].value === value) {
      eSelect.selectedIndex = i;
    }
  }
}

function selectFirstItem(eSelect) {
  if (eSelect.options.length !== 0) {
    eSelect.selectedIndex = 0;
  }
}

//
// default for rejecting a resource-call
//
function resourceReject(json) {
  console.log(json);
}

function processMessages(json) {
  showMessages(json);
  markElements(json);
}

/**
 * @param json             Json-Object including the violations.
 * @param json.violations  violations.
 */
function showMessages(json) {
  let ul = document.getElementById("violations");
  let violations = json.violations;

  violations.forEach(violation => {
    let li = document.createElement("li");
    li.setAttribute("class", "violation");
    li.appendChild(document.createTextNode(violation.message));
    ul.appendChild(li);
  });
}

/**
 * Marking UI-Elements with data causing violations.
 * @param json containing violations
 * @param json.violations violations
 * @param json.violations.propertyPath propertyPath in the model with violation.
 */
function markElements(json) {
  json.violations.forEach(violation => {
    let element = document.getElementById(violation.propertyPath);
    element.classList.add("violation");
  });
}