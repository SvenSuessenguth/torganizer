/* global squadsResource, playersResource, tournamentsResource, tournaments */

class Menue {
  constructor() {
  }
  
  onLoad(){
    this.init();
  }

  init(){
    this.update();
  }

  update(){
    // https://stackoverflow.com/questions/13955667/disabled-href-tag
    // no selected tournament -> no further actions
    if(tournaments.getId()===null){
      this.disableLink("menueDisciplines");
      this.disableLink("menuePlayers");
      this.disableLink("menueSquads");
      this.disableLink("menueClubs");
      this.disableLink("menueMatches");
    }else{
      this.enableLink("menueDisciplines");
      this.enableLink("menuePlayers");
      this.enableLink("menueSquads");
      this.enableLink("menueClubs");
      this.enableLink("menueMatches");
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    if(currentDisciplineId===null){
      this.disableLink("menueRounds");
    }else {
      this.enableLink("menueRounds");
    }
  }

  disableLink(linkId){
    document.getElementById(linkId).setAttribute("onclick", "return false;");
    document.getElementById(linkId).setAttribute("class", "disabledLink");
  }
  enableLink(linkId){
    document.getElementById(linkId).removeAttribute("onclick");
    document.getElementById(linkId).removeAttribute("class");
  }
}

var menue = new Menue();