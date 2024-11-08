package henrycaldwell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hand of playing cards in a blackjack game.
 * Manages the cards in the hand and provides methods to evaluate the hand's
 * value.
 */
public class Hand {

  private List<Card> cards; // The cards currently in the hand.

  /**
   * Constructs an empty hand.
   */
  public Hand() {
    this.cards = new ArrayList<>();
  }

  /**
   * Adds a card of the specified rank to the hand.
   *
   * @param rank The rank of the card to be added.
   * @throws IllegalArgumentException if the rank is {@code null}.
   */
  public void add(Card.Rank rank) {
    if (rank == null) {
      throw new IllegalArgumentException("Rank cannot be null.");
    }
    cards.add(new Card(rank));
  }

  /**
   * Removes a card of the specified rank from the hand.
   *
   * @param rank The rank of the card to be removed.
   * @throws IllegalArgumentException if the rank is {@code null}.
   * @throws IllegalStateException    if no card of the specified rank exists in
   *                                  the hand.
   */
  public void remove(Card.Rank rank) {
    if (rank == null) {
      throw new IllegalArgumentException("Rank cannot be null.");
    }

    for (int i = 0; i < cards.size(); i++) {
      Card card = cards.get(i);

      if (card.getRank().equals(rank)) {
        cards.remove(i);
        return;
      }
    }

    throw new IllegalStateException("No card of rank '" + rank.getName() + "' exists in the hand.");
  }

  /**
   * Checks if a specific rank is in the hand.
   *
   * @param rank The rank to be checked.
   * @return {@code true} if the rank is in the hand; {@code false} otherwise.
   * @throws IllegalArgumentException if the rank is {@code null}.
   */
  public boolean contains(Card.Rank rank) {
    if (rank == null) {
      throw new IllegalArgumentException("Rank cannot be null.");
    }

    for (Card card : cards) {
      if (card.getRank().equals(rank)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Evaluates and returns the total value of the cards in the hand, considering
   * the special nature of aces.
   *
   * @return The total value of the hand.
   */
  public int evaluateHand() {
    int totalValue = 0;
    int aceCount = 0;

    for (Card card : cards) {
      int cardValue = card.getRank().getValue();
      totalValue += cardValue;

      if (card.getRank().equals(Card.Rank.ACE)) {
        aceCount++;
      }
    }

    // Adjust for aces: count as 11 if it doesn't bust the hand
    while (aceCount > 0 && totalValue + 10 <= 21) {
      totalValue += 10;
      aceCount--;
    }

    return totalValue;
  }

  /**
   * Determines if the hand is a 'soft' hand, which means it includes at least one
   * ace counted as 11 without busting.
   *
   * @return {@code true} if the hand is soft; {@code false} otherwise.
   */
  public boolean isSoftHand() {
    int totalValue = 0;
    int aceCount = 0;

    for (Card card : cards) {
      int cardValue = card.getRank().getValue();
      totalValue += cardValue;

      if (card.getRank().equals(Card.Rank.ACE)) {
        aceCount++;
      }
    }

    // Check if any ace can be counted as 11 without busting
    return aceCount > 0 && (totalValue + 10) <= 21;
  }

  /**
   * Determines if the hand can be split.
   * A hand can be split if it contains exactly two cards of the same rank.
   *
   * @return {@code true} if the hand can be split; {@code false} otherwise.
   */
  public boolean canSplit() {
    if (cards.size() != 2) {
      return false;
    }

    Card.Rank firstCardRank = cards.get(0).getRank();
    Card.Rank secondCardRank = cards.get(1).getRank();
    return firstCardRank == secondCardRank;
  }

  /**
   * Retrieves a copy of the list of cards in the hand.
   *
   * @return A new list containing the cards in the hand.
   */
  public List<Card> getCards() {
    return new ArrayList<>(cards); // Defensive copy to prevent external modification.
  }

  /**
   * Sets the hand's cards to the specified list of ranks.
   *
   * @param ranks The list of ranks to set.
   * @throws IllegalArgumentException if the list is {@code null} or contains
   *                                  {@code null} ranks.
   */
  public void setCards(List<Card.Rank> ranks) {
    if (ranks == null) {
      throw new IllegalArgumentException("Ranks list cannot be null.");
    }

    List<Card> newCards = new ArrayList<>();

    for (Card.Rank rank : ranks) {
      if (rank == null) {
        throw new IllegalArgumentException("Rank in list cannot be null.");
      }
      newCards.add(new Card(rank));
    }

    this.cards = newCards;
  }

  /**
   * Retrieves the number of cards in the hand.
   *
   * @return The size of the hand.
   */
  public int getSize() {
    return cards.size();
  }

  /**
   * Creates a deep copy of the hand.
   *
   * @return A new {@code Hand} object with the same cards.
   */
  @Override
  public Hand clone() {
    Hand clone = new Hand();

    for (Card card : this.cards) {
      clone.add(card.getRank());
    }

    return clone;
  }

  /**
   * Provides a string representation of the hand, listing all cards and the
   * total value of the hand.
   *
   * @return A string summarizing the hand.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    for (int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
      Card currCard = cards.get(cardIndex);
      output.append(currCard);

      if (cardIndex < cards.size() - 1) {
        output.append(", ");
      }
    }

    output.append(" - ").append(evaluateHand()).append(" Total");
    return output.toString();
  }
}
