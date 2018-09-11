/* global squadsResource, playersResource, tournamentsResource, tournaments */

menue = {

  onload : function onload(){
    menue.update();
  },

  update : function update(){
    // https://stackoverflow.com/questions/13955667/disabled-href-tag
    // no selected tournament -> no further actions
    if(tournaments.getId()===null){
      menue.disableLink("menueDisciplines");
      menue.disableLink("menuePlayers");
      menue.disableLink("menueSquads");
      menue.disableLink("menueClubs");
      menue.disableLink("menueMatches");
    }else{
      menue.enableLink("menueDisciplines");
      menue.enableLink("menuePlayers");
      menue.enableLink("menueSquads");
      menue.enableLink("menueClubs");
      menue.enableLink("menueMatches");
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.discipline.id');
    if(currentDisciplineId===null){
      this.disableLink("menueRounds");
    }else {
      this.enableLink("menueRounds");
    }
  },

  disableLink : function disableLink(linkId){
    document.getElementById(linkId).setAttribute("onclick", "return false;");
    document.getElementById(linkId).setAttribute("class", "disabledLink");
  },
  enableLink : function enableLink(linkId){
    document.getElementById(linkId).removeAttribute("onclick");
    document.getElementById(linkId).removeAttribute("class");
  },
}