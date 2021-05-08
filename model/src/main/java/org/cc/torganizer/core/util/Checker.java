package org.cc.torganizer.core.util;

import static java.lang.Boolean.TRUE;

import java.util.List;

/**
 * Hilfsmethoden zu Listen und Arrays.
 */
public class Checker {

  /**
   * Default.
   */
  public Checker() {
    // gem. Bean-Spec.
  }

  /**
   * Pr\u00fcft, ob eins und genau ein \u00fcbergebenes Object null ist.
   *
   * @param objects Array von Objekten, die gepr\u00fcft werden sollen.
   * @return a boolean.
   */
  public static int countNullValues(Object... objects) {

    if (objects == null || objects.length == 0) {
      return 0;
    }

    var nullCounter = 0;

    // count null values
    for (Object object : objects) {
      nullCounter += object == null ? 1 : 0;
    }

    return nullCounter;
  }

  /**
   * Pr\u00fcft, ob alle \u00fcbergebenen Objecte null sind.
   *
   * @param objects Array von Objekten, die gepr\u00fcft werden sollen.
   * @return a boolean.
   */
  public static boolean onlyNullValues(Object... objects) {
    int counter = countNullValues(objects);

    return objects == null ? TRUE : counter == objects.length;
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

  /**
   * Pr\u00fcfen, ob die Inhalte der beiden Listen gleich sind. Dabei wird die
   * Reihenfolge der Elemente nicht ber\u00fccksichtigt.
   *
   * @param list0 Liste eins
   * @param list1 Liste zwei
   * @return <code>true</code>, wenn die Inhalte gleich sind, sonst
   *     <code>false</code>
   */
  public boolean equals(List<?> list0, List<?> list1) {
    var equals = true;

    if (!list0.containsAll(list1)) {
      equals = false;
    }
    if (!list1.containsAll(list0)) {
      equals = false;
    }

    return equals;
  }
}
