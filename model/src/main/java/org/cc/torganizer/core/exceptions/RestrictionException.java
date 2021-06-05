package org.cc.torganizer.core.exceptions;

/**
 * Wird geworfen, wenn gegen eine Restriktion verstossen wird.
 */
public class RestrictionException extends RuntimeException {

  /**
   * Bequemlichkeitskonstruktor.
   *
   * @param message Message
   */
  public RestrictionException(String message) {
    super(message);
  }
}
