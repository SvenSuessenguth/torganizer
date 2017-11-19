package org.cc.torganizer.core.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Abstract Opponent class.
 * </p>
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Opponent")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Opponent extends AbstractBaseEntity {

	private Long id;
	
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

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(Long newId) {
		this.id = newId;
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
