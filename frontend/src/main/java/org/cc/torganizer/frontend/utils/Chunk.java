package org.cc.torganizer.frontend.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Functions for chunks.
 */
public class Chunk<T> {

  /**
   * Getting a chunk.
   */
  public Collection<T> get(Collection<T> all, int chunkSize, int chunkIndex) {

    var allSize = all.size();
    var startIndex = (chunkIndex) * chunkSize;
    var endIndex = (chunkIndex + 1) * chunkSize;
    endIndex = Math.min(endIndex, allSize);

    if (startIndex > allSize) {
      return new ArrayList<>();
    }

    return new ArrayList<>(all).subList(startIndex, endIndex);
  }
}
