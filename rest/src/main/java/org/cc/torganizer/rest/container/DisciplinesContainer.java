package org.cc.torganizer.rest.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.entities.Discipline;

@XmlRootElement(name = "disciplines")
@XmlAccessorType(XmlAccessType.FIELD)
public class DisciplinesContainer {

  @XmlElement
  private List<Discipline> disciplines = new ArrayList<>();

  public DisciplinesContainer() {
  }

  public DisciplinesContainer(List<Discipline> disciplines) {
    this.disciplines = disciplines;
  }
}
