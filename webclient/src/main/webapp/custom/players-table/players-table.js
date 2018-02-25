// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class PlayersTable extends HTMLElement{
  
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
    var template = ownerDocument.getElementById("players-table-template");    
    this.playerTable = document.importNode(template.content, true);    
    
    // always show empty table with all rows    
    this.tbody = this.playerTable.getElementById("players-table-body");    
    for (var i = 0; i < this.rows; i++) { 
      var rowPlayer = document.createElement("tr");
      var tdFirstName = document.createElement("td");
      // for correct height of an empty row
      tdFirstName.innerHTML += '&nbsp;';
      var tdLastName = document.createElement("td");      
      var tdClub = document.createElement("td");
      
      rowPlayer.appendChild(tdFirstName);
      rowPlayer.appendChild(tdLastName);
      rowPlayer.appendChild(tdClub);
      
      this.tbody.appendChild(rowPlayer);
    }
    
     this.shadowRoot.appendChild(this.playerTable);
  }
  
  attributeChangedCallback(name, oldValue, newValue) {
    // calling attributeChangedCallback before connectedCallback results in emtpy 'tbody'
    if(this.tbody===null){
      return;
    }
    
    // only listening for changes of 'data'
    // so newValue is always an array of players (or null on loading the page)
    if(newValue!== '' && newValue!==null && newValue!=='null')
    {
      var jsonArray = JSON.parse(newValue);    
      var counter = 0;
      var tbody = this.tbody;
      // therefore 'playerSelected' is a global function, the id of the event-sending element is dynmic
      var id = this.getAttribute("id");
    
      jsonArray.forEach(function(player){      
        var rowPlayer = tbody.getElementsByTagName("tr")[counter];
        rowPlayer.setAttribute("onclick", "playerSelected("+player.id+", \""+id+"\")");
      
        var tdFirstName = rowPlayer.getElementsByTagName("td")[0];
        tdFirstName.innerHTML = player.person.firstName;
      
        var tdLastName = rowPlayer.getElementsByTagName("td")[1];
        tdLastName.innerHTML = player.person.lastName;
      
        counter += 1;
      });
    }
  
    // clear left over rows
    for(var i=counter;i<this.rows;i+=1){
      var rowPlayer = tbody.getElementsByTagName("tr")[i];
      var tdFirstName = rowPlayer.getElementsByTagName("td")[0];
      tdFirstName.innerHTML = '&nbsp;';
      
      var tdLastName = rowPlayer.getElementsByTagName("td")[1];
      tdLastName.innerHTML = '&nbsp;';
    }
  }
}

function playerSelected(playerId, elementId){
  // https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Creating_and_triggering_events
  var event = new CustomEvent('player-selected', {"detail":playerId});  
  document.getElementById(elementId).dispatchEvent(event);
}

var customElements;
customElements.define("players-table", PlayersTable);