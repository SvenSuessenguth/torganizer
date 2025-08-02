package org.cc.torganizer.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Restriktion bezueglich des Typs des spielberechtigten Opponents. Z.B. darf
 * nur Einzel- oder nur Doppel gespielt werden.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OpponentTypeRestriction extends Restriction {

  private static final Discriminator DISCRIMINATOR = Discriminator.OPPONENT_TYPE_RESTRICTION;

  private OpponentType opponentType = OpponentType.PLAYER;

  @Override
  public boolean isRestricted(Opponent opponent) {
    if (opponent == null) {
      return true;
    }

    var ot = opponent.getOpponentType();
    return opponentType != ot;
  }

  @Override
  public String toString() {
    return "OpponentTypeRestriction with opponentType: " + opponentType;
  }

  @Override
  public Discriminator getDiscriminator() {
    return OpponentTypeRestriction.DISCRIMINATOR;
  }
}
