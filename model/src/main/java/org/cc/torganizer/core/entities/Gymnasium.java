package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


/**
 * Gymnasium.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Gymnasium extends Entity {
  private String name;
  private final List<Court> courts = new ArrayList<>();

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
}
