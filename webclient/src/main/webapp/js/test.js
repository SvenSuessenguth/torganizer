class Test {
  constructor() {
  }
  
  onload(){
    this.updateCurrentSubscribers();
  }
  
  updateCurrentSubscribers(){
    this.countCurrentSubscribers().then(function(data){
      document.getElementById("subscribersCount").innerHTML=data;
    });
  }
  
  countCurrentSubscribers() {
    var tournamentId = Number(localStorage.getItem('tournaments-current-tournament-id'));    
    var count = fetch('http://localhost:8080/rest/resources/tournaments/1/subscribers/count').then(function(response) {
      return response.json();
    });
    
    return count;
  }
}

var test = new Test();