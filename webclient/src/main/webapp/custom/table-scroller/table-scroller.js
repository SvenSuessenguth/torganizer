// https://developers.google.com/web/fundamentals/web-components/customelements
// http://www.adam-bien.com/roller/abien/entry/creating_a_customelement_webcomponent_from

class TableScroller extends HTMLElement{
  
  constructor(){
    super();
  }
  
  connectedCallback(){
    var ownerDocument = document.currentScript.ownerDocument;
    var template = ownerDocument.getElementById("table-scroller-template");
    
    var clone = document.importNode(template.content, true);
    document.body.appendChild(clone);
  }
};

customElements.define("table-scroller", TableScroller);
