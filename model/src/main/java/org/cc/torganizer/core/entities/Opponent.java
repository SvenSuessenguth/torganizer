package org.cc.torganizer.core.entities;

import java.util.List;


/**
 * Abstract Opponent class.
 */
public abstract class Opponent extends Entity {

	private Status status = Status.ACTIVE;

	/**
	 * Gibt eine Liste aller Players zurueck, die teil des Opponents sind.
	 * 
	 * @return Liste aller Players dieses Opponents.
	 */  
	public abstract List<Player> getPlayers();

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status inStatus) {
		this.status = inStatus;
	}

	public boolean isBye() {
		return this instanceof Bye;
	}
}
