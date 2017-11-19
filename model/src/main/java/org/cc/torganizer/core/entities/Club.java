package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Verein.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Club")
@XmlAccessorType(XmlAccessType.FIELD)
public class Club
  extends AbstractBaseEntity {

  /**
   * zu verwendender Name, wenn ein Opponent keinem club angehoert.
   */
  public static final String NULL_CLUB_NAME = "-";

  private Long id;
  
  private String name;

  /**
   * Default.
   */
  public Club() {
    // gem. Bean-Spec.
  }

  /**
   * Konstruktor.
   * 
   * @param pName Name des Clubs
   */
  public Club(String pName) {
    this.name = pName;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  @Override
  public Long getId() {
    return this.id;
  }
  
	@Override
	public void setId(Long newId) {
		this.id = newId;
	}
}
