package henrycaldwell.model;

import henrycaldwell.controller.GameRules;

/**
 * Represents the dealer in a blackjack game.
 */
public class Dealer {

  private Hand hand; // The hand held by the dealer.

  /**
   * Constructs a dealer with an empty hand.
   */
  public Dealer() {
    this.hand = new Hand();
  }

  /**
   * Constructs a dealer with a predefined hand.
   *
   * @param hand The pre-existing hand to be assigned to the dealer.
   * @throws IllegalArgumentException if the hand is {@code null}.
   */
  public Dealer(Hand hand) {
    if (hand == null) {
      throw new IllegalArgumentException("Hand cannot be null.");
    }
    this.hand = hand.clone(); // Defensive copy.
  }

  /**
   * Determines whether the dealer should hit based on the current hand and game
   * rules.
   *
   * @return {@code true} if the dealer should hit; {@code false} otherwise.
   */
  public boolean shouldHit() {
    int dealerScore = hand.evaluateHand();
    boolean isSoftHand = hand.isSoftHand();

    return dealerScore < 17 || (dealerScore == 17 && isSoftHand && GameRules.DEALER_HITS_ON_SOFT_17);
  }

  /**
   * Sets the dealer's hand to the specified hand.
   * If a hand already exists, it will be replaced with the new hand.
   *
   * @param hand The hand to assign to the dealer.
   * @throws IllegalArgumentException if the hand is {@code null}.
   */
  public void addHand(Hand hand) {
    if (hand == null) {
      throw new IllegalArgumentException("Hand cannot be null.");
    }
    this.hand = hand.clone(); // Defensive copy.
  }

  /**
   * Retrieves the dealer's current hand.
   *
   * @return The dealer's hand.
   */
  public Hand getHand() {
    return this.hand;
  }

  /**
   * Creates a deep copy of the dealer.
   *
   * @return A new {@code Dealer} object with a cloned hand.
   */
  @Override
  public Dealer clone() {
    Hand clonedHand = hand.clone();
    return new Dealer(clonedHand);
  }

  /**
   * Provides a string representation of the dealer's hand, listing all
   * cards and the total value of the hand.
   *
   * @return A string detailing the dealer's hand.
   */
  @Override
  public String toString() {
    return hand.toString();
  }
}
