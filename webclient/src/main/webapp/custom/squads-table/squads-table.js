// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class SquadsTable extends HTMLElement{
  
  static get observedAttributes() {
    return ['data'];
  }
  
  constructor(){
    super();
    this.attachShadow({ mode: 'open' });
    this.rows = 10;
    // to access tbody on attribute change
    this.tbody = null;
    
    var attRows = this.getAttribute("rows");
    if(attRows !== null){
      this.rows = attRows;
    }
    
    var attData = this.getAttribute("data");
    if(attData === null || attData.length===0){
      this.data = '[]';
    }
    
    var attId = this.getAttribute("id");
    if(attId === null || attId.length===0){
      this.Id = '';
    }
  }
  
  set data(newData) { this.setAttribute('data', newData); }
  get data() { return this.getAttribute('data'); }
  
  connectedCallback(){    
    var ownerDocument = document.currentScript.ownerDocument;
    var template = ownerDocument.getElementById("squads-table-template");    
    this.squadsTable = document.importNode(template.content, true);    
    
    // always show empty table with all rows    
    this.tbody = this.squadsTable.getElementById("squads-table-body");    
    for (var i = 0; i < this.rows; i++) { 
      var rowSquad = document.createElement("tr");
      var tdFirstNames = document.createElement("td");
      // for correct height of an empty row
      tdFirstNames.innerHTML += '&nbsp;';
      var tdLastNames = document.createElement("td");      
      var tdClubs = document.createElement("td");
      
      rowSquad.appendChild(tdFirstNames);
      rowSquad.appendChild(tdLastNames);
      rowSquad.appendChild(tdClubs);
      
      this.tbody.appendChild(rowSquad);
    }
    
     this.shadowRoot.appendChild(this.squadsTable);
  }
  
  attributeChangedCallback(name, oldValue, newValue) {
    // calling attributeChangedCallback before connectedCallback results in emtpy 'tbody'
    if(this.tbody===null){
      return;
    }
    
    // only listening for changes of 'data'
    // so newValue is always an array of squads
    var squads = JSON.parse(newValue);    
    var counter = 0;
    var tbody = this.tbody;
    // therefore 'squadSelected' is a global function, the id of the event-sending element is dynmic
    var id = this.getAttribute("id");
    
    squads.forEach(function(squad){      
      var rowSquad = tbody.getElementsByTagName("tr")[counter];
      rowSquad.setAttribute("onclick", "squadSelected("+squad.id+", \""+id+"\")");
      
      var players = squad.players;
      
      var tdFirstNames = rowSquad.getElementsByTagName("td")[0];
      tdFirstNames.innerHTML = "";
      players.forEach(function(player){
        tdFirstNames.innerHTML += player.person.firstName+"<br />";
      });
      
      var tdLastNames = rowSquad.getElementsByTagName("td")[1];
      tdLastNames.innerHTML = "";
      players.forEach(function(player){
        tdLastNames.innerHTML += player.person.lastName+"<br />";
      });
      
      counter += 1;
    });
    
    // clear left over rows
    for(var i=counter;i<this.rows;i+=1){
      var rowSquad = tbody.getElementsByTagName("tr")[i];
      var tdFirstNames = rowSquad.getElementsByTagName("td")[0];
      tdFirstNames.innerHTML = '&nbsp;';
      
      var tdLastNames = rowSquad.getElementsByTagName("td")[1];
      tdLastNames.innerHTML = '&nbsp;';
    }
  }
}

function squadSelected(squadId, elementId){
  // https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Creating_and_triggering_events
  var event = new CustomEvent('squad-selected', {"detail":squadId});  
  document.getElementById(elementId).dispatchEvent(event);
}

var customElements;
customElements.define("squads-table", SquadsTable);