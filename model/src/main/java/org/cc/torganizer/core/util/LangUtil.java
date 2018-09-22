package org.cc.torganizer.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Allgemeine Utilities.
 */
public final class LangUtil {

  /**
   * Default.
   */
  private LangUtil() {
    // gem. Bean-Spec.
  }

  /**
   * Gibt das erste Object zurueck, welches nicht <code>null</code> ist.
   *
   * @param objects Array von Objects
   * @return Das erste Object ungleich <code>null</code> aus dem Array
   */
  public static Object getNotNullValue(Object... objects) {
    Object result = null;

    for (Object object : objects) {
      if (object != null) {
        result = object;
        break;
      }
    }

    return result;
  }

  /**
   * Pruefen, ob zwei Listen, unabhaengig von der Reihenfolge, den gleichen Inhalt
   * haben.
   *
   * @param listA Liste A
   * @param listB Liste B
   * @return <code>true</code>, wenn die beiden Listen den gleichen Inhalt haben,
   *     sonst <code>false</code>
   */
  public static boolean sameContent(List<?> listA, List<?> listB) {
    boolean sameContent = true;

    // bei ungleicher Laenge sind die Listen nicht identisch
    if (listA.size() != listB.size()) {
      return false;
    }

    // jedes Element von listA in listB?
    for (Object o : listA) {
      if (!listB.contains(o)) {
        sameContent = false;
        break;
      }
    }

    // jedes Element von listB in listA?
    for (Object o : listB) {
      if (!listA.contains(o)) {
        sameContent = false;
        break;
      }
    }

    return sameContent;
  }

  /**
   * Erzeugen einer Liste von aufsteigenden Interger-Objekten.
   *
   * @param start Startwert (eingeschlossen)
   * @param end   Endwert (nicht mit eingeschlossen)
   * @return Liste mit Integern von <code>start</code> (eingeschlossen) bis
   *     <code>end</code> (ausgeschlossen)
   */
  public static List<Integer> createIntegerList(int start, int end) {
    List<Integer> integerList = new ArrayList<>();

    for (int value = start; value < end; value += 1) {
      integerList.add(value);
    }

    return integerList;
  }

  public static String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return s;
    }

    String firstChar = s.substring(0, 1);
    String remainingChars = s.substring(1);

    return firstChar.toUpperCase(Locale.getDefault()) + remainingChars;
  }
}
