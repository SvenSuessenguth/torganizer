package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Spielfeld.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Court extends Entity {
  private Match match;
  private int nr;
}
