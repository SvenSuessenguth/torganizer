package org.cc.torganizer.core.exceptions;

/**
 * Wird geworfen, wenn gegen eine Restriktion verstossen wird.
 */
public class RestrictionException extends RuntimeException {

  private static final long serialVersionUID = -8976725761960127526L;

  /**
   * Bequemlichkeitskonstruktor.
   *
   * @param message Message
   */
  public RestrictionException(String message) {
    super(message);
  }
}
