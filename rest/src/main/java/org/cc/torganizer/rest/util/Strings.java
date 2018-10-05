package org.cc.torganizer.rest.util;

/**
 * Utility-Methods for Strings.
 */
public class Strings {

  /**
   * Checks, if a given String is <code>NULL</code> or <code>empty</code> (after trimming).
   */
  public static final boolean isEmpty(String s) {
    return s == null || s.trim().isEmpty();
  }
}
