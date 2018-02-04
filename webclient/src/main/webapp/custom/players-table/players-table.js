// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class PlayersTable extends HTMLElement{
  
  static get observedAttributes() {
    return ['data'];
  }
  
  constructor(){
    super();
    this.rows = 10;
    // to access tbody on attribute change
    this.tbody = null;
  }
  
  set data(newData) { this.setAttribute('data', newData); }
  get data() { return this.getAttribute('data'); }
  
  connectedCallback(){
    
    var attRows = this.getAttribute("rows");
    if(attRows !== null){
      this.rows = attRows;
    }
    
    var attData = this.getAttribute("data");
    if(attData === null || attData.length===0){
      data = '[]';
    }
    
    var ownerDocument = document.currentScript.ownerDocument;
    var template = ownerDocument.getElementById("players-table-template");
    
    var clone = document.importNode(template.content, true);    
    
    // always show empty table with all rows
    this.tbody = clone.getElementById("players-table-body");    
    for (var i = 0; i < this.rows; i++) { 
      var rowPlayer = document.createElement("tr");
      var tdFirstName = document.createElement("td");
      // for correct height of a row
      tdFirstName.innerHTML += '&nbsp;';
      var tdLastName = document.createElement("td");      
      var tdClub = document.createElement("td");
      
      rowPlayer.appendChild(tdFirstName);
      rowPlayer.appendChild(tdLastName);
      rowPlayer.appendChild(tdClub);
      
      this.tbody.appendChild(rowPlayer);
    }
    
    document.body.appendChild(clone);
    
    // show first data
    let attOnshow = this.getAttribute("onshow");
    eval(attOnshow);
  }
  
  attributeChangedCallback(name, oldValue, newValue) {
    // calling attributeChangedCallback before connectedCallback results in emtpy 'tbody'
    if(this.tbody===null){
      return;
    }
    
    // only listening for changes of 'data'
    // so newValue is always an array of players
    var jsonArray = JSON.parse(newValue);    
    var counter = 0;
    var tbody = this.tbody;
    jsonArray.forEach(function(player){      
      var rowPlayer = tbody.getElementsByTagName("tr")[counter];
      var tdFirstName = rowPlayer.getElementsByTagName("td")[0];
      tdFirstName.innerHTML = player.person.firstName;
      
      var tdLastName = rowPlayer.getElementsByTagName("td")[1];
      tdLastName.innerHTML = player.person.lastName;
      
      // missing club
      
      counter += 1;
    });
    
    // clear left over rows
    for(var i=counter;i<this.rows;i+=1){
      var rowPlayer = tbody.getElementsByTagName("tr")[i];
      var tdFirstName = rowPlayer.getElementsByTagName("td")[0];
      tdFirstName.innerHTML += '&nbsp;';
      
      var tdLastName = rowPlayer.getElementsByTagName("td")[1];
      tdLastName.innerHTML += '&nbsp;';
    }
  }
};

var customElements;
customElements.define("players-table", PlayersTable);