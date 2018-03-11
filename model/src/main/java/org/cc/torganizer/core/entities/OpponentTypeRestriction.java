package org.cc.torganizer.core.entities;

/**
 * Restriktion bezueglich des Typs des spielberechtigten Opponents. Z.B. darf
 * nur Einzel- oder nur Doppel gespielt werden.
 */
public class OpponentTypeRestriction
  extends Restriction {

  private static final Discriminator DISCRIMINATOR = Discriminator.OPPONENT_TYPE_RESTRICTION;

  private OpponentType opponentType = OpponentType.PLAYER;

  /**
   * Default.
   */
  public OpponentTypeRestriction() {
    // gem. Bean-Spec.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean isRestricted(Opponent opponent) {

    Class<? extends Opponent> validOpponentClass = opponentType.getOpponentClass();
    Class<Opponent> opponentClass = (Class<Opponent>) opponent.getClass();

    return !validOpponentClass.equals(opponentClass);
  }

  public OpponentType getOpponentType() {
    return opponentType;
  }

  public void setOpponentType(OpponentType newOpponentType) {
    this.opponentType = newOpponentType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "OpponentTypeRestriction with opponentType: " + opponentType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Discriminator getDiscriminator() {
    return OpponentTypeRestriction.DISCRIMINATOR;
  }
}
