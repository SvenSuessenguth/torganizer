package org.cc.torganizer.core.util;

/**
 * Mathematische Hilfsmethoden.
 */
public final class MathUtil {

  /**
   * Default.
   */
  private MathUtil() {
    // gem. Bean-Spec.
  }

  /**
   * Finden der n\u00e4chsten Zweier-Potenz, die ueber dem gegebenen Wert liegt.
   * 
   * @param value Wert, zu dem die naechste Zweierpotenz gesucht wird.
   * @return N\u00e4chste Zweier-Potenz.
   */
  public static int nextSquare(int value) {
    int number = 1;
    while (number * 2 < value) {
      number *= 2;
    }
    number *= 2;

    return number;
  }
}
