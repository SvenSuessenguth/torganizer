package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.Opponent;

@XmlRootElement(name="opponents")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpponentsContainer {
  
  @XmlElement
  private List<Opponent> opponents = new ArrayList<>();
  
  public OpponentsContainer() {
  }
  
  public OpponentsContainer(List<Opponent> opponents) {
    this.opponents = opponents;
  }
}
