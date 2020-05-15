package org.cc.torganizer.frontend.utils;

import java.util.ArrayList;
import java.util.Collection;

public class Chunk<T> {
  public Collection<T> get(Collection<T> all, int chunkSize, int chunkIndex) {
    Collection<T> chunk = null;

    int allSize = all.size();
    int startIndex = (chunkIndex) * chunkSize;
    int endIndex = (chunkIndex + 1) * chunkSize;
    endIndex = Math.min(endIndex, allSize);

    if (startIndex > allSize) {
      return new ArrayList<T>();
    }

    return new ArrayList<T>(all).subList(startIndex, endIndex);
  }
}
