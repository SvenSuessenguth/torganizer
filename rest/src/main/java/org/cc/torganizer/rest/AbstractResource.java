package org.cc.torganizer.rest;

public abstract class AbstractResource<T> {

  /**
   * Menge der ausgegebenen Datensätze, wenn nichts anderes angefordert wurde.
   */
  protected static final int DEFAULT_LENGTH = 10;
  /**
   * Startposition der Menge aller ausgegebenen Datensätze, wenn nichts anderes angegeben.
   */
  protected static final int DEFAULT_OFFSET = 0;
  
  protected AbstractResource() {
    // Utility classes, which are collections of static members, are not meant to be instantiated. 
    // Even abstract utility classes, which can be extended, should not have public constructors.
  }

}
