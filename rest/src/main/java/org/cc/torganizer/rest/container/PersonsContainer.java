package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.Person;

/**
 * Container für Restrictions, um korrektes XML/JSON zu generieren.
 *  
 * @author svens
 */
@XmlRootElement(name="persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonsContainer {
  
  private List<Person> persons = new ArrayList<>();
  
  public PersonsContainer() {
    
  }
  
  public PersonsContainer(List<Person> persons) {
    this.persons = persons;
  }

  public void addAll(List<Person> persons) {
    this.persons = persons;
  }

  public List<Person> getPersons() {
    return persons;
  }

}
