package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;


/**
 * Gymnasium.
 */
public class Gymnasium extends Entity {

  private String name;

  private final List<Court> courts = new ArrayList<>();

  /**
   * Default.
   */
  public Gymnasium() {
    // gem. Bean-Spec.
  }

  /**
   * Erzeugen einer vorgegebenen Anzahl von Courts. Die bereits bestehenden
   * Courts werden verworfen. Eine negative Anzahl wird ignoriert und alle Courts werden verworfen.
   *
   * @param number Anzahl der Courts, die erzeugt werden sollen.
   */
  public void createCourts(int number) {
    if (number != courts.size()) {
      courts.clear();
      for (var i = 0; i < number; i += 1) {
        var court = new Court();
        court.setNr(i + 1);
        courts.add(court);
      }
    }
  }

  public List<Court> getCourts() {
    return courts;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
