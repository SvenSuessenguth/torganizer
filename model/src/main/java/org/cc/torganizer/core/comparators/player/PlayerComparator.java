package org.cc.torganizer.core.comparators.player;

import org.cc.torganizer.core.entities.Player;

import java.io.Serializable;
import java.util.Comparator;

public interface PlayerComparator extends Comparator<Player>, Serializable {

  PlayerOrder getPlayerOrder();
}
