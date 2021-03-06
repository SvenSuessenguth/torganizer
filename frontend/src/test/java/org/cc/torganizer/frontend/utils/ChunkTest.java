package org.cc.torganizer.frontend.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChunkTest {

  private Chunk<String> chunk;

  @BeforeEach
  public void beforeEach() {
    chunk = new Chunk<>();
  }

  @Test
  void test() {
    Collection<String> all = Arrays.asList("1", "2", "3", "4", "5", "6");
    int chunkSize = 2;
    int chunkIndex = 1; // index begins an 0

    Collection<String> actual = chunk.get(all, chunkSize, chunkIndex);
    Collection<String> expected = Arrays.asList("3", "4");

    assertThat(actual).hasSameSizeAs(expected).containsAll(expected);
  }

  @Test
  void testStartIndexToLarge() {
    Collection<String> all = Arrays.asList("1", "2", "3", "4", "5", "6");
    int chunkSize = 2;

    Collection<String> actual = chunk.get(all, chunkSize, 20);

    assertThat(actual).isEmpty();
  }
}