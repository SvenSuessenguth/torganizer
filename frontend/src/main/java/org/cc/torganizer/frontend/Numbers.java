package org.cc.torganizer.frontend;

public class Numbers {
  public Long getLong(String value) {
    Long l = null;

    try {
      l = Long.valueOf(value.trim());
    } catch (NumberFormatException nfExc) {
      // do nothing
    }

    return l;
  }
}
