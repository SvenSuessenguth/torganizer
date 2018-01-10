// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class TournamentPlayers extends HTMLElement{
  
  static get observedAttributes() {
    return ['data'];
  }
  
  constructor(){
    super();
  }
  
  set data(newData) { this.setAttribute('data', newData); }
  get data() { return this.getAttribute('data'); }
  
  connectedCallback(){
    var attRows = this.getAttribute("rows");
    if(attRows == null){
      rows = 10;
    }
    
    var attData = this.getAttribute("data");
    if(attData == null || attData.length==0){
      data = '[]';
    }
    
    var ownerDocument = document.currentScript.ownerDocument;
    var template = ownerDocument.getElementById("tournament-players-template");
    
    var clone = document.importNode(template.content, true);
    document.body.appendChild(clone);
  }
  
  attributeChangedCallback(name, oldValue, newValue) {
    // only listening for changes of 'data'
    // so newValue is always an array of players
    var jsonArray = JSON.parse(newValue);
    jsonArray.forEach(function(player){
      // TODO: insert data
    });    
  }
};

customElements.define("tournament-players", TournamentPlayers);