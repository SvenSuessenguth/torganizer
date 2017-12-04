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

	/**
	 * <p>
	 * Getter for the field <code>status</code>.
	 * </p>
	 * 
	 * @return a {@link org.cc.torganizer.core.entities.Status} object.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * <p>
	 * Setter for the field <code>status</code>.
	 * </p>
	 * 
	 * @param inStatus a {@link org.cc.torganizer.core.entities.Status} object.
	 */
	public void setStatus(Status inStatus) {
		this.status = inStatus;
	}

	/**
	 * <p>
	 * isBye.
	 * </p>
	 * 
	 * @return a boolean.
	 */
	public boolean isBye() {
		return this instanceof Bye;
	}
}
