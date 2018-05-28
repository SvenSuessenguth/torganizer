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
    if(tournaments.getCurrentTournamentId()===null){
      document.getElementById("menueDisciplines").removeAttribute("href");
      document.getElementById("menueRounds").removeAttribute("href");
      document.getElementById("menuePlayers").removeAttribute("href");
      document.getElementById("menueSquads").removeAttribute("href");
      document.getElementById("menueClubs").removeAttribute("href");
      document.getElementById("menueMatches").removeAttribute("href");
    }

    let currentDisciplineId = sessionStorage.getItem('disciplines.current-discipline.id');
    if(currentDisciplineId===null){
      document.getElementById("menueRounds").removeAttribute("href");
    }
  }
}

var menue = new Menue();