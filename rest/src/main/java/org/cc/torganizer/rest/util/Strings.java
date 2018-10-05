package org.cc.torganizer.rest.util;

/**
 * Utility-Methods for Strings.
 */
public class Strings {

  /**
   * Utility class constructor.
   */
  private Strings(){
    // do not instantiate...
  }

  /**
   * Checks, if a given String is <code>NULL</code> or <code>empty</code> (after trimming).
   */
  public static boolean isEmpty(String s) {
    return s == null || s.trim().isEmpty();
  }
}
