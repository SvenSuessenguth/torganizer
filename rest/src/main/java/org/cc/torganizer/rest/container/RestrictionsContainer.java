package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;

/**
 * Container für Restrictions, um korrektes XML/JSON zu generieren.
 *  
 * @author svens
 * @see https://stackoverflow.com/questions/4888228/configuring-collection-of-polymorphic-objects-to-work-in-jaxb2
 */
@XmlRootElement(name="restrictions")
@XmlAccessorType(XmlAccessType.NONE)
public class RestrictionsContainer {

  @XmlElements(value = { @XmlElement(name = "GenderRestriction", type = GenderRestriction.class),
      @XmlElement(name = "AgeRestriction", type = AgeRestriction.class),
      @XmlElement(name = "OpponentTypeRestriction", type = OpponentTypeRestriction.class) })
  private List<Restriction> restrictions = new ArrayList<>();

  public RestrictionsContainer(List<Restriction> restrictions) {
	  this.restrictions = restrictions;
  }
  
  public void add(Restriction restriction) {
    restrictions.add(restriction);
  }
  public void addAll(Collection<Restriction> restrictions) {
    this.restrictions.addAll(restrictions);
  }
  
  public List<Restriction> getRestrictions(){
    return restrictions;
  }
}
