// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class OpponentsTable extends HTMLElement{
  
  constructor(){
    super();
    this.template = this.template();
    this._rows = 10;
    this._tbody = null;
    this._visibilit = true
  }
  
  set id(newId) { this.setAttribute('id', newId); }
  get id() { return this._id; }
  set rows(newRows) { this.setAttribute('rows', newRows); }
  get rows() { return this._rows; }
  set data(newData) { this.setAttribute('data', newData); }
  get data() { return this._data; }

  connectedCallback(){
	this.attachShadow({ mode: 'open' });
	let opponentsTable = this.template.content.cloneNode(true);
	this._tbody = opponentsTable.getElementById("opponents-table-body");
	
    // always show empty table with all rows
    for (let i = 0; i < this._rows; i++) {
      
      let rowOpponent = document.createElement("tr");
      let tdFirstNames = document.createElement("td");
      // for correct height of an empty row
      tdFirstNames.innerHTML += '&nbsp;';
      let tdLastNames = document.createElement("td");
      let tdClubs = document.createElement("td");
      
      rowOpponent.appendChild(tdFirstNames);
      rowOpponent.appendChild(tdLastNames);
      rowOpponent.appendChild(tdClubs);
      rowOpponent.setAttribute("ondragover" , "event.preventDefault()");
      rowOpponent.setAttribute("ondrop" , "opponentDropped(-1, -1)");

      this._tbody.appendChild(rowOpponent);
    }
    
    this.shadowRoot.appendChild(opponentsTable);
  }
  
  static get observedAttributes() { return ['data', 'rows']; }
  
  attributeChangedCallback(name, oldValue, newValue) {

    if(name==="rows"){
      this._rows = newValue;
      return;
    }

    // only listening for changes of 'data'
    // so newValue is always an array of opponents
    let opponents = JSON.parse(newValue);
    let counter = 0;
    
    // therefore 'opponentSelected' is a global function, the id of the event-sending element is dynamic
    let id = this.getAttribute("id");
    let tbody = this._tbody;
    if(tbody===null){ return; }

    opponents.forEach(function(opponent){
      let rowOpponent = tbody.getElementsByTagName("tr")[counter];
      rowOpponent.setAttribute("onclick", "opponentSelected("+opponent.id+", "+id+")");
      // rowOpponent.setAttribute("draggable", "true");
      // rowOpponent.setAttribute("ondragstart" , "opponentDragged(event, "+opponent.id+", "+id+")");
      // rowOpponent.setAttribute("ondrop" , "opponentDropped(event, "+opponent.id+", "+id+")");

      // in case of single player put json in new array
      if(!opponent.players){
        let newArray = "{\"players\":["+JSON.stringify(opponent)+"]}";
        opponent = JSON.parse(newArray);
      }

      let players = opponent.players;
      let tdFirstNames = rowOpponent.getElementsByTagName("td")[0];
      tdFirstNames.innerHTML = "";

      players.forEach(function(player){
        tdFirstNames.innerHTML += player.person.firstName+"<br />";
      });
      
      let tdLastNames = rowOpponent.getElementsByTagName("td")[1];
      tdLastNames.innerHTML = "";
      players.forEach(function(player){
        tdLastNames.innerHTML += player.person.lastName+"<br />";
      });

      let tdClubs = rowOpponent.getElementsByTagName("td")[2];
      tdClubs.innerHTML = "";
      players.forEach(function(player){
        let clubName = player.club.name;
        if(clubName!=null) {
          tdClubs.innerHTML += player.club.name + "<br />";
        }else{
          tdClubs.innerHTML += "-<br />";
        }
      });
      
      counter += 1;
    });
    
    // clear left over rows
    for(var i=opponents.length;i<this.rows;i+=1){
      var rowOpponent = tbody.getElementsByTagName("tr")[i];
      var tdFirstNames = rowOpponent.getElementsByTagName("td")[0];
      tdFirstNames.innerHTML = '&nbsp;';
      
      var tdLastNames = rowOpponent.getElementsByTagName("td")[1];
      tdLastNames.innerHTML = '&nbsp;';

      var tdClubs = rowOpponent.getElementsByTagName("td")[2];
      tdClubs.innerHTML = '&nbsp;';
    }
  }
  
  template(){
	let template = document.createElement('template');
	template.innerHTML = `	  
      <style>
        table{
          border: var(--table_border);
          border-collapse: var(--table_border-collapse);
        }
        tr:nth-child(odd){
          background-color: var(--tr_nth-child_odd_background-color);
        }
        tr:nth-child(even){
          background-color: var(--tr_nth-child_even_background-color);
        }
        th{ 
          background-color: var(--th_background-color); 
          border: var(--th_border); 
          padding: var(--th_padding); 
        }
        td{
          border: var(--td_border);
          padding: var(--td_padding);
        }
      </style>
  
      <table id="opponents-table" style="visibility: visible">
        <thead>
          <tr>
            <th>Vornamen</th><th>Nachnamen</th><th>Vereine</th>
          </tr>      
        </thead>
        <tbody id="opponents-table-body">
        </tbody>
      </table>
    `;
	
	return template;
  }
}

function opponentSelected(opponentId, elementId){
  // https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Creating_and_triggering_events
  let event = new CustomEvent('opponent-selected', {"detail":opponentId});
  document.getElementById(elementId).dispatchEvent(event);
}

function opponentDragged(event, opponentId, elementId) {
    let dragJson = {
    sourceOpponentId: opponentId,
    sourceElementId: elementId
  };

  event.dataTransfer.setData("text/plain", JSON.stringify(dragJson));
}
function opponentDropped(event, opponentId, elementId) {
  let dragJson = JSON.parse(event.dataTransfer.getData("text/plain"));
  let dropJson = {
    sourceOpponentId: dragJson.sourceOpponentId,
    sourceElementId: dragJson.sourceElementId,
    dropOpponentId: opponentId,
    dropElementId: elementId
  }

  let dragAndDroppedEvent = new CustomEvent('opponent-dragged-and-dropped', {"detail":JSON.stringify(dropJson)});
  let element = document.getElementById(elementId);
  element.dispatchEvent(dragAndDroppedEvent);
}

window.customElements.define("opponents-table", OpponentsTable);