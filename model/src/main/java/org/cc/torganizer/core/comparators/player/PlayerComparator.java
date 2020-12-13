package org.cc.torganizer.core.comparators.player;

import java.io.Serializable;
import java.util.Comparator;
import org.cc.torganizer.core.entities.Player;

public interface PlayerComparator extends Comparator<Player>, Serializable {

  PlayerOrderCriteria getPlayerOrderCriteria();
}
