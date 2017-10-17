package org.cc.torganizer.core.util;

import java.util.List;

/**
 * Hilfsmethoden zu Listen und Arrays.
 *
 * @author svens
 * @version $Id: $
 */
public class Checker {

  /**
   * Default.
   */
  public Checker() {
    // gem. Bean-Spec.
  }

  /**
   * Pr\u00fcfen, ob die Inhalte der beiden Listen gleich sind. Dabei wird die
   * Reihenfolge der Elemente nicht ber\u00fccksichtigt.
   *
   * @param list0 Liste eins
   * @param list1 Liste zwei
   * @return <code>true</code>, wenn die Inhalte gleich sind, sonst
   *         <code>false</code>
   */
  public boolean equals(List<?> list0, List<?> list1) {
    boolean equals = true;

    if (!list0.containsAll(list1)) {
      equals = false;
    }
    if (!list1.containsAll(list0)) {
      equals = false;
    }

    return equals;
  }

  /**
   * Pr\u00fcft, ob eins und genau ein \u00fcbergebenes Object null ist.
   *
   * @param objects Array von Objekten, die gepr\u00fcft werden sollen.
   * @return a boolean.
   */
  public boolean onlyOneIsNull(Object... objects) {

    if (objects == null || objects.length == 0) {
      return false;
    }

    int nullCounter = 0;

    // Zaehlen der Objekte, die NULL sind
    for (Object object : objects) {
      nullCounter += object == null ? 1 : 0;
    }

    return nullCounter == 1;
  }

  /**
   * Pr\u00fcft, ob eins und genau ein \u00fcbergebenes Object null ist.
   *
   * @param objects Array von Objekten, die gepr\u00fcft werden sollen.
   * @return a boolean.
   */
  public boolean onlyOneIsNotNull(Object... objects) {

    // es muss mindestens ein Objekt uebergeben werden 
    if (objects == null || objects.length == 0) {
      return false;
    }

    int notNullCounter = 0;

    // Zaehlen der Objekte, die NULL sind
    for (Object object : objects) {
      notNullCounter += object != null ? 1 : 0;
    }

    return notNullCounter == 1;
  }

  /**
   * Pr\u00fcft, ob alle \u00fcbergebenen Objecte null sind.
   *
   * @param objects Array von Objekten, die gepr\u00fcft werden sollen.
   * @return a boolean.
   */
  public boolean allIsNull(Object... objects) {
    // es muss mindestens ein Objekt uebergeben werden 
    if (objects == null || objects.length == 0) {
      return false;
    }

    int nullCounter = 0;

    // Zaehlen der Objekte, die NULL sind
    for (Object object : objects) {
      nullCounter += object == null ? 1 : 0;
    }

    return nullCounter == objects.length;
  }

  /**
   * Pruefen, ob keins der uebergebenen Objekte NULL ist.
   *
   * @param objects Array von Objekten
   * @return <code>true</code>, wenn kein Objekt null ist, sonst <code>false</code>
   */
  public static boolean isNoNullValue(Object... objects) {
    // es muss mindestens ein Objekt uebergeben werden 
    if (objects == null || objects.length == 0) {
      return true;
    }

    for (Object object : objects) {
      if (object == null) {
        return false;
      }
    }
    return true;
  }
}