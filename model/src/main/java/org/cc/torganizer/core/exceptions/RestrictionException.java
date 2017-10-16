package org.cc.torganizer.core.exceptions;

/**
 * Wird geworfen, wenn gegen eine Restriktion verstossen wird.
 *
 * @author svens
 * @version $Id: $
 */
public class RestrictionException
  extends RuntimeException {
  /** serialVersionUID. */
  private static final long serialVersionUID = 747642716444713231L;

  /**
   * Bequemlichkeitskonstruktor.
   *
   * @param message Message
   */
  public RestrictionException(String message) {
    super(message);
  }
}
