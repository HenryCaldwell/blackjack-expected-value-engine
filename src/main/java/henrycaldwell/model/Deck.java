package henrycaldwell.model;

import java.util.ArrayList;
import java.util.List;

import henrycaldwell.controller.GameRules;

/**
 * Represents a deck of playing cards, capable of managing and tracking cards.
 * This class handles adding and removing cards, maintaining card counts, and
 * calculating the count used in card counting strategies.
 */
public class Deck {

  private final List<Card> cards; // The list holding the cards in the deck.
  private int[] valueCounts; // The array storing counts of each card value.
  private int runningCount; // The running count of the deck for card counting strategies.

  /**
   * Constructs a deck and initializes it with a standard set of cards based
   * on the number of decks specified in the game rules.
   */
  public Deck() {
    this.cards = new ArrayList<>();
    this.valueCounts = new int[10];

    initializeDeck();
  }

  /**
   * Initializes the deck by populating it with a full set of cards and updating
   * the value counts for each rank.
   * This method also resets the running count to zero.
   */
  public void initializeDeck() {
    cards.clear();
    valueCounts = new int[10]; // Reset value counts

    for (int currDeck = 0; currDeck < GameRules.NUMBER_OF_DECKS; currDeck++) {
      for (int currSuit = 0; currSuit < 4; currSuit++) {
        for (Card.Rank currRank : Card.Rank.values()) {
          cards.add(new Card(currRank));
          valueCounts[currRank.getValue() - 1]++;
        }
      }
    }

    // Reset running count when deck is reset
    runningCount = 0;
  }

  /**
   * Adds a card of the specified rank to the deck and updates the
   * value counts and running count accordingly.
   *
   * @param rank The rank of the card to be added.
   * @throws IllegalArgumentException if the rank is {@code null}.
   */
  public void add(Card.Rank rank) {
    if (rank == null) {
      throw new IllegalArgumentException("Rank cannot be null.");
    }

    int value = rank.getValue();
    valueCounts[value - 1]++;

    if (value == 1 || value == 10) {
      runningCount--;
    } else if (value >= 2 && value <= 6) {
      runningCount++;
    }

    cards.add(new Card(rank));
  }

  /**
   * Removes a card of the specified rank from the deck and updates
   * the value counts and running count accordingly.
   *
   * @param rank The rank of the card to be removed.
   * @throws IllegalArgumentException if the rank is {@code null}.
   * @throws IllegalStateException    if no card of the specified rank exists in
   *                                  the deck.
   */
  public void remove(Card.Rank rank) {
    if (rank == null) {
      throw new IllegalArgumentException("Rank cannot be null.");
    }

    for (int i = 0; i < cards.size(); i++) {
      Card card = cards.get(i);

      if (card.getRank().equals(rank)) {
        int value = card.getRank().getValue();
        valueCounts[value - 1]--;

        if (value == 1 || value == 10) {
          runningCount++;
        } else if (value >= 2 && value <= 6) {
          runningCount--;
        }

        cards.remove(i);
        return;
      }
    }

    throw new IllegalStateException("No card of rank '" + rank.getName() + "' exists in the deck.");
  }

  /**
   * Checks if a card of the specified rank is present in the deck.
   *
   * @param rank The rank of the card to check for.
   * @return {@code true} if a card of the specified rank is in the deck;
   *         {@code false} otherwise.
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
   * Retrieves a copy of the list of cards in the deck to prevent external
   * modification.
   *
   * @return A copy of the list of cards in the deck.
   */
  public List<Card> getCards() {
    return new ArrayList<>(cards); // Return a copy to prevent external modification.
  }

  /**
   * Retrieves the current number of cards in the deck.
   *
   * @return The number of cards in the deck.
   */
  public int getSize() {
    return cards.size();
  }

  /**
   * Retrieves a deep copy of the value counts array, which represents the count
   * of each card value in the deck.
   *
   * @return A new array containing the counts of each card value.
   */
  public int[] getValueCounts() {
    return valueCounts.clone();
  }

  /**
   * Calculates and retrieves the true count of the deck, adjusted by the number
   * of decks in use, for card counting purposes.
   *
   * @return The true count value.
   */
  public double getCount() {
    if (cards.isEmpty()) {
      return 0.0;
    }

    return runningCount / (cards.size() / 52.0);
  }

  /**
   * Provides a string representation of the deck, listing all cards.
   *
   * @return A string listing all cards in the deck.
   */
  @Override
  public String toString() {
    StringBuilder deckOutput = new StringBuilder();

    for (Card card : cards) {
      deckOutput.append(card).append("\n");
    }

    return deckOutput.toString();
  }
}
