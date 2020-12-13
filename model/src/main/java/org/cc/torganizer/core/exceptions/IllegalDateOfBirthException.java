package org.cc.torganizer.core.exceptions;

/**
 * This exception is thrown, if the DateOfBirth is illegal. The DateOfBirth ist illegal, if the
 * Person is less than one year old.
 */
public class IllegalDateOfBirthException extends RuntimeException {

  public IllegalDateOfBirthException(String message) {
    super(message);
  }
}
