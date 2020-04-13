package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_UPDATE;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;

@Named
@ViewScoped
public class PlayersState implements Serializable {

  private static final long serialVersionUID = 3683970655136738688L;

  private List<Player> players;

  private List<Club> clubs;

  private Player current;

  private Long currentClubId;

  private PlayerOrder playerOrder = BY_LAST_UPDATE;

  public Player getCurrent() {
    return current;
  }

  public void setCurrent(Player current) {
    this.current = current;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<Club> getClubs() {
    return clubs;
  }

  public void setClubs(List<Club> clubs) {
    this.clubs = clubs;
  }

  public List<Gender> getGenders() {
    return Arrays.asList(Gender.values());
  }

  public Long getCurrentClubId() {
    return currentClubId;
  }

  public void setCurrentClubId(Long currentClubId) {
    this.currentClubId = currentClubId;
  }

  public PlayerOrder getPlayerOrder() {
    return playerOrder;
  }

  public void setPlayerOrder(PlayerOrder playerOrder) {
    this.playerOrder = playerOrder;
  }

  public List<PlayerOrder> getPlayerOrders() {
    return Arrays.asList(PlayerOrder.values());
  }
}
