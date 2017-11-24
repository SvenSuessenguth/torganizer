package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Gymnasium.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Gymnasium")
@XmlAccessorType(XmlAccessType.FIELD)
public class Gymnasium extends Entity {

  private String name;

  @XmlTransient
  private List<Court> courts = new ArrayList<>();

  /**
   * Default.
   */
  public Gymnasium() {
    // gem. Bean-Spec.
  }

  /**
   * Erzeugen einer vorgegebenen Anzahl von Courts. Die bereits bestehenden
   * Courts werden verworfen.
   * 
   * @param number Anzahl der Courts, die erzeugt werden sollen.
   */
  public void createCourts(int number) {
    if (number != courts.size()) {
      courts.clear();
      for (int i = 0; i < number; i += 1) {
        Court court = new Court();
        court.setNr(i + 1);
        courts.add(court);
      }
    }
  }

  public int getCourtsCount() {
    return courts.size();
  }

  public List<Court> getCourts() {
    return courts;
  }

  public String getName() {
    return name;
  }

  public void setName(String pName) {
    this.name = pName;
  }
}
