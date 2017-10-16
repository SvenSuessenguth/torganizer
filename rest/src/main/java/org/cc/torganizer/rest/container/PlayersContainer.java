package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.Player;

@XmlRootElement(name="players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayersContainer {
  
  @XmlElement
  private List<Player> players = new ArrayList<>();
  
  public PlayersContainer() {
  }
  
  public PlayersContainer(List<Player> players) {
    this.players = players;
  }
}
