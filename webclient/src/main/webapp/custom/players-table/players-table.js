// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class PlayersTable extends HTMLElement{
  
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
      attRows = 10;
    }
    
    var attData = this.getAttribute("data");
    if(attData == null || attData.length==0){
      attData = '[]';
    }
    
    var ownerDocument = document.currentScript.ownerDocument;
    var template = ownerDocument.getElementById("players-table-template");
    
    var clone = document.importNode(template.content, true);
    document.body.appendChild(clone);
  }
  
  attributeChangedCallback(name, oldValue, newValue) {
    switch (name) {
      case 'data':
        console.log("Value changed from "+oldValue+" to "+newValue);
      break;
    }
  }
};

customElements.define("players-table", PlayersTable);