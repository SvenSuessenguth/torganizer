package org.cc.torganizer.core.exceptions;

/**
 * This exception is thrown, if any opponent of a group restricts the rules.
 */
public class RestrictionException extends RuntimeException {
  public RestrictionException(String message) {
    super(message);
  }
}
