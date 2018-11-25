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
    for (var i = 0; i < this._rows; i++) { 
      
      var rowOpponent = document.createElement("tr");
      var tdFirstNames = document.createElement("td");
      // for correct height of an empty row
      tdFirstNames.innerHTML += '&nbsp;';
      var tdLastNames = document.createElement("td");      
      var tdClubs = document.createElement("td");
      
      rowOpponent.appendChild(tdFirstNames);
      rowOpponent.appendChild(tdLastNames);
      rowOpponent.appendChild(tdClubs);
      
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
    
    // therefore 'opponentSelected' is a global function, the id of the event-sending element is dynmic
    let id = this.getAttribute("id");
    let tbody = this._tbody;
    if(tbody===null){ return; }
    
    opponents.forEach(function(opponent){
      let rowOpponent = tbody.getElementsByTagName("tr")[counter];
      rowOpponent.setAttribute("onclick", "opponentSelected("+opponent.id+", \""+id+"\")");

      // in case of single player put json in new array
      if(!opponent.players){
        var newArray = "{\"players\":["+JSON.stringify(opponent)+"]}";
        opponent = JSON.parse(newArray);
      }

      var players = opponent.players;
      var tdFirstNames = rowOpponent.getElementsByTagName("td")[0];
      tdFirstNames.innerHTML = "";

      players.forEach(function(player){
        tdFirstNames.innerHTML += player.person.firstName+"<br />";
      });
      
      var tdLastNames = rowOpponent.getElementsByTagName("td")[1];
      tdLastNames.innerHTML = "";
      players.forEach(function(player){
        tdLastNames.innerHTML += player.person.lastName+"<br />";
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
    }
  }
  
  template(){
	let template = document.createElement('template');
	template.innerHTML = `	  
      <style>
        table{ border: 1px solid Gray; border-collapse:collapse; }
        tr:nth-child(odd){ background-color: #F4F4F4; }
        tr:nth-child(even){ background-color: #FFFFFF; }
        th{ background-color: #FECEA8; border: 1px solid Gray; padding: 2px 20px; }
        td{ border: 1px solid Gray; padding: 2px 20px; }
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
  var event = new CustomEvent('opponent-selected', {"detail":opponentId});
  document.getElementById(elementId).dispatchEvent(event);
}

window.customElements.define("opponents-table", OpponentsTable);