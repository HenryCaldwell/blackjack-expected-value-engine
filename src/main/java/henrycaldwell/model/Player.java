package henrycaldwell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a blackjack game, managing their hands and actions.
 */
public class Player {

  private List<Hand> hands; // The list of hands held by the player.

  /**
   * Constructs a player with no hands.
   */
  public Player() {
    this.hands = new ArrayList<>();
  }

  /**
   * Constructs a player with a predefined list of hands.
   *
   * @param hands The list of pre-existing hands to be assigned to the player.
   * @throws IllegalArgumentException if the hands list is {@code null}.
   */
  public Player(List<Hand> hands) {
    if (hands == null) {
      throw new IllegalArgumentException("Hands list cannot be null.");
    }

    this.hands = new ArrayList<>(hands);
  }

  /**
   * Adds a hand to the player's list of hands.
   *
   * @param hand The hand to add.
   * @throws IllegalArgumentException if the hand is {@code null}.
   */
  public void addHand(Hand hand) {
    if (hand == null) {
      throw new IllegalArgumentException("Hand cannot be null.");
    }

    hands.add(hand);
  }

  /**
   * Removes a hand from the player's list at the specified index.
   *
   * @param index The index of the hand to be removed.
   * @throws IndexOutOfBoundsException if the index is out of range (less than 0
   *                                   or greater than or equal to the number of
   *                                   hands).
   */
  public void removeHand(int index) {
    if (index < 0 || index >= hands.size()) {
      throw new IndexOutOfBoundsException("Invalid hand index: " + index + ".");
    }

    hands.remove(index);
  }

  /**
   * Attempts to split a hand at a specified index. Splitting is only allowed if
   * the hand contains exactly two cards of the same rank.
   *
   * @param index The index of the hand to split.
   * @throws IndexOutOfBoundsException if the index is out of range (less than 0
   *                                   or greater than or equal to the number of
   *                                   hands).
   * @throws IllegalArgumentException  if the hand at the specified index cannot
   *                                   be split.
   */
  public void splitHand(int index) {
    if (index < 0 || index >= hands.size()) {
      throw new IndexOutOfBoundsException("Invalid hand index: " + index + ".");
    }

    Hand originalHand = hands.get(index);

    if (!canSplit(index)) {
      throw new IllegalArgumentException("Hand at index " + index + " cannot be split.");
    }

    List<Card> originalCards = originalHand.getCards();

    Card firstCard = originalCards.get(0);
    Card secondCard = originalCards.get(1);

    Hand firstSplit = new Hand();
    firstSplit.add(firstCard.getRank());

    Hand secondSplit = new Hand();
    secondSplit.add(secondCard.getRank());

    hands.set(index, firstSplit);
    hands.add(index + 1, secondSplit);
  }

  /**
   * Checks if a hand at a given index can be split.
   *
   * @param index The index of the hand to check.
   * @return {@code true} if the hand can be split; {@code false} otherwise.
   * @throws IndexOutOfBoundsException if the index is out of range (less than 0
   *                                   or greater than or equal to the number of
   *                                   hands).
   */
  public boolean canSplit(int index) {
    if (index < 0 || index >= hands.size()) {
      throw new IndexOutOfBoundsException("Invalid hand index: " + index + ".");
    }

    Hand hand = hands.get(index);

    if (hand.getSize() != 2) {
      return false;
    }

    List<Card> cards = hand.getCards();
    return cards.get(0).getRank().equals(cards.get(1).getRank());
  }

  /**
   * Retrieves a list of all hands held by the player.
   *
   * @return A new list containing the player's hands.
   */
  public List<Hand> getHands() {
    return new ArrayList<>(hands);
  }

  /**
   * Retrieves the number of hands the player currently holds.
   *
   * @return The number of hands.
   */
  public int getNumHands() {
    return hands.size();
  }

  /**
   * Creates a deep copy of the player.
   *
   * @return A new {@code Player} object with cloned hands.
   */
  @Override
  public Player clone() {
    List<Hand> clonedHands = new ArrayList<>();

    for (Hand hand : this.hands) {
      clonedHands.add(hand.clone());
    }

    return new Player(clonedHands);
  }

  /**
   * Provides a string representation of all hands held by the player, listing
   * each hand on a new line.
   *
   * @return A string detailing each hand held by the player.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    for (int handIndex = 0; handIndex < hands.size(); handIndex++) {
      Hand currentHand = hands.get(handIndex);
      output.append("Hand ").append(handIndex + 1).append(": ").append(currentHand);

      if (handIndex < hands.size() - 1) {
        output.append("\n");
      }
    }

    return output.toString();
  }
}
