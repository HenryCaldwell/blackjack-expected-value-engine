package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import henrycaldwell.controller.GameRules;

/**
 * Test suite for the Deck class.
 */
public class DeckTest {

  private Deck deck;

  /**
   * Setup method to initialize a new Deck instance before each test.
   */
  @Before
  public void setUp() {
    // For testing purposes, ensure GameRules.NUMBER_OF_DECKS is 1
    deck = new Deck();
  }

  /**
   * Helper method to count occurrences of a specific rank in the deck.
   * 
   * @param rank The rank to count.
   * @return The number of times the rank appears in the deck.
   */
  private int countRankInDeck(Card.Rank rank) {
    int count = 0;

    for (Card card : deck.getCards()) {
      if (card.getRank().equals(rank)) {
        count++;
      }
    }

    return count;
  }

  // ================================
  // Tests for Constructor
  // ================================

  /**
   * Tests the {@link Deck} constructor initializes correctly.
   * <p>
   * Verifies that the deck is not null, has the correct initial size based on
   * {@link GameRules.NUMBER_OF_DECKS}, and that the count is initialized to 0.
   * </p>
   */
  @Test
  public void testConstructor() {
    assertNotNull("Deck should not be null after initialization", deck);
    assertEquals("Deck size should be NUMBER_OF_DECKS * 4 * number of ranks",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length,
        deck.getSize());
    assertEquals("Initial count should be 0", 0.0, deck.getCount(), 0.0001);
  }

  // ================================
  // Tests for InitializeDeck Method
  // ================================

  /**
   * Tests the {@link Deck#initializeDeck()} method correctly resets the deck.
   * <p>
   * Modifies the deck by removing a card, resets it, and verifies that the deck
   * size and count are restored to their initial values.
   * </p>
   */
  @Test
  public void testInitializeDeck() {
    // Modify the deck by removing a card
    deck.remove(Card.Rank.ACE);

    // Ensure the deck size has decreased by 1
    assertEquals("Deck size should decrease by 1 after removing a card",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length - 1,
        deck.getSize());

    // Reset the deck
    deck.initializeDeck();

    // Check if the deck is back to initial size
    assertEquals("Deck size should be reset to initial size",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length,
        deck.getSize());

    // Check if count is reset
    assertEquals("Count should be reset to 0 after reset", 0.0, deck.getCount(), 0.0001);
  }

  // ================================
  // Tests for Add Method
  // ================================

  /**
   * Tests the {@link Deck#add(Card.Rank)} method with a valid rank.
   * <p>
   * Adds a rank to the deck and verifies that the deck size increases by 1 and
   * that the count reflects the addition appropriately.
   * </p>
   */
  @Test
  public void testAddValidRank() {
    Card.Rank newRank = Card.Rank.JACK;
    int initialSize = deck.getSize();

    deck.add(newRank);

    assertEquals("Deck size should increase by 1 after adding a card",
        initialSize + 1, deck.getSize());

    assertTrue("Deck should contain an extra instance of the rank",
        countRankInDeck(newRank) == (GameRules.NUMBER_OF_DECKS * 4 + 1));
  }

  /**
   * Tests the {@link Deck#add(Card.Rank)} method with a null rank.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown when attempting to
   * add a null rank.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullRank() {
    deck.add(null);
  }

  /**
   * Tests adding multiple instances of the same rank to the deck.
   * <p>
   * Adds two identical ranks and verifies that the deck size increases
   * accordingly
   * and that the count reflects the additions.
   * </p>
   */
  @Test
  public void testAddMultipleSameRanks() {
    Card.Rank duplicateRank = Card.Rank.THREE;
    int initialSize = deck.getSize();

    deck.add(duplicateRank);
    deck.add(duplicateRank);

    assertEquals("Deck size should increase by 2 after adding two identical ranks",
        initialSize + 2, deck.getSize());

    // The deck should contain two additional instances of the rank
    int count = countRankInDeck(duplicateRank);
    assertEquals("Deck should contain two additional identical ranks",
        (GameRules.NUMBER_OF_DECKS * 4) + 2, count);
  }

  // ================================
  // Tests for Remove Method
  // ================================

  /**
   * Tests the {@link Deck#remove(Card.Rank)} method with a rank that exists in
   * the deck.
   * <p>
   * Removes a rank from the deck and verifies that the deck size decreases by 1,
   * the count is updated correctly, and the deck still contains remaining
   * instances
   * of the rank if multiple exist.
   * </p>
   */
  @Test
  public void testRemoveExistingRank() {
    Card.Rank rankToRemove = Card.Rank.KING;
    int initialSize = deck.getSize();
    double initialCount = deck.getCount();

    // Ensure the deck contains the rank
    assertTrue("Deck should contain the rank to be removed", deck.contains(rankToRemove));

    // Remove the rank
    deck.remove(rankToRemove);

    // Check if the deck size has decreased by 1
    assertEquals("Deck size should decrease by 1 after removing a rank",
        initialSize - 1, deck.getSize());

    // Check if the count has been updated correctly
    // Since KING has a value of 10, count should have been incremented by 1
    double expectedCount = (initialCount + 1) / ((initialSize - 1) / 52.0);
    assertEquals("Count should be incremented by 1 after removing a KING", expectedCount, deck.getCount(), 0.0001);

    // Ensure the deck no longer contains the removed rank if all instances are
    // removed
    // Since multiple instances might exist, ensure at least one is removed
    assertTrue("Deck should still contain the rank if multiple instances exist",
        countRankInDeck(rankToRemove) == (GameRules.NUMBER_OF_DECKS * 4 - 1));
  }

  /**
   * Tests the {@link Deck#remove(Card.Rank)} method with a rank that does not
   * exist in the deck.
   * <p>
   * Attempts to remove a rank that has no remaining instances in the deck and
   * expects
   * an {@link IllegalStateException} to be thrown.
   * </p>
   */
  @Test(expected = IllegalStateException.class)
  public void testRemoveNonExistingRank() {
    // Create a scenario where all instances of a rank are removed
    Card.Rank rankToRemove = Card.Rank.ACE;
    int numberOfAces = GameRules.NUMBER_OF_DECKS * 4;

    for (int i = 0; i < numberOfAces; i++) {
      deck.remove(rankToRemove);
    }

    // Attempting to remove one more Ace should throw an exception
    deck.remove(rankToRemove);
  }

  /**
   * Tests the {@link Deck#remove(Card.Rank)} method with a null rank.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown when attempting to
   * remove a null rank.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullRank() {
    deck.remove(null);
  }

  /**
   * Tests removing all instances of a specific rank from the deck.
   * <p>
   * Removes all instances of a given rank and verifies that the deck no longer
   * contains that rank and that the count is updated appropriately.
   * </p>
   */
  @Test
  public void testRemoveAllInstancesOfRank() {
    Card.Rank rankToRemove = Card.Rank.EIGHT;
    int numberOfEights = GameRules.NUMBER_OF_DECKS * 4;

    // Remove all instances
    for (int i = 0; i < numberOfEights; i++) {
      deck.remove(rankToRemove);
    }

    // Ensure no more instances exist
    assertFalse("Deck should not contain any instances of EIGHT after removal", deck.contains(rankToRemove));
    assertEquals("Count should reflect the removal of all EIGHTs (value=8 does not affect count)", 0.0, deck.getCount(),
        0.0001);
  }

  // ================================
  // Tests for Contains Method
  // ================================

  /**
   * Tests the {@link Deck#contains(Card.Rank)} method for a rank that exists in
   * the deck.
   * <p>
   * Verifies that the method returns {@code true} when the deck contains the
   * specified rank.
   * </p>
   */
  @Test
  public void testContainsExistingRank() {
    Card.Rank existingRank = Card.Rank.QUEEN;
    assertTrue("Deck should contain the existing rank", deck.contains(existingRank));
  }

  /**
   * Tests the {@link Deck#contains(Card.Rank)} method for a rank that does not
   * exist in the deck.
   * <p>
   * Removes all instances of a rank and verifies that the method returns
   * {@code false}.
   * </p>
   */
  @Test
  public void testContainsNonExistingRank() {
    // Remove all NINES from the deck
    Card.Rank rankToRemove = Card.Rank.NINE;
    int numberOfNines = GameRules.NUMBER_OF_DECKS * 4;

    for (int i = 0; i < numberOfNines; i++) {
      deck.remove(rankToRemove);
    }

    // Now, the deck should not contain any NINE
    assertFalse("Deck should not contain NINE after removing all instances", deck.contains(rankToRemove));
  }

  /**
   * Tests the {@link Deck#contains(Card.Rank)} method with a null rank.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown when attempting to
   * check containment of a null rank.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testContainsNullRank() {
    deck.contains(null);
  }

  // ================================
  // Tests for getCards Method
  // ================================

  /**
   * Tests the {@link Deck#getCards()} method.
   * <p>
   * Verifies that the method returns a non-null list of cards with the correct
   * size.
   * </p>
   */
  @Test
  public void testGetCards() {
    List<Card> cards = deck.getCards();
    assertNotNull("getCards should not return null", cards);
    assertEquals("getCards should return a list of correct size",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length,
        cards.size());
  }

  // ================================
  // Tests for getSize Method
  // ================================

  /**
   * Tests the {@link Deck#getSize()} method.
   * <p>
   * Verifies that the method returns the correct number of cards after various
   * operations such as adding and removing ranks.
   * </p>
   */
  @Test
  public void testGetSize() {
    assertEquals("Initial deck size should match expected size",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length,
        deck.getSize());

    // Add a rank and check size
    deck.add(Card.Rank.TWO);
    assertEquals("Deck size should increase by 1 after adding a rank",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length + 1,
        deck.getSize());

    // Remove a rank and check size
    deck.remove(Card.Rank.TWO);
    assertEquals("Deck size should decrease by 1 after removing a rank",
        GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length,
        deck.getSize());
  }

  // ================================
  // Tests for getCount Method
  // ================================

  /**
   * Tests the {@link Deck#getCount()} method.
   * <p>
   * Verifies that the count accurately reflects the current count based on
   * the cards removed or added.
   * </p>
   */
  @Test
  public void testGetCount() {
    int initialSize = deck.getSize();

    // Initial count should be 0
    assertEquals("Initial count should be 0", 0.0, deck.getCount(), 0.0001);

    // Remove a high card (ACE, value=1) which increments count
    deck.remove(Card.Rank.ACE);
    assertEquals("Count should be incremented by 1 after removing an ACE", (1 / ((initialSize - 1) / 52.0)),
        deck.getCount(), 0.0001);

    // Remove a low card (FIVE, value=5) which decrements count
    deck.remove(Card.Rank.FIVE);
    assertEquals("Count should be back to 0 after removing a FIVE", 0.0,
        deck.getCount(), 0.0001);

    // Remove a neutral card (SEVEN, value=7) which does not affect count
    deck.remove(Card.Rank.SEVEN);
    assertEquals("Count should remain 0 after removing a SEVEN", 0.0,
        deck.getCount(), 0.0001);

    // Remove another high card (KING, value=10)
    deck.remove(Card.Rank.KING);
    assertEquals("Count should be incremented by 1 after removing a KING", (1 / ((initialSize - 4)
        / 52.0)),
        deck.getCount(), 0.0001);

    // Remove another low card (TWO, value=2)
    deck.remove(Card.Rank.TWO);
    assertEquals("Count should be back to 0 after removing a TWO",
        0.0, deck.getCount(), 0.0001);
  }

  /**
   * Tests the {@link Deck#getCount()} method when the deck becomes empty to avoid
   * division by zero.
   * <p>
   * Verifies that the count is handled gracefully and returns 0.0 when the deck
   * is empty.
   * </p>
   */
  @Test
  public void testGetCountWithZeroCards() {
    // Remove all cards from the deck
    int totalCards = GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length;
    for (int i = 0; i < totalCards; i++) {
      Card.Rank rank = deck.getCards().get(0).getRank();
      deck.remove(rank);
    }

    // Now, getCount should handle division by zero gracefully
    // According to the current implementation, it returns 0.0
    double count = deck.getCount();
    assertEquals("Count should be 0.0 when deck is empty", 0.0, count, 0.0001);
  }

  // ================================
  // Tests for getValueCounts Method
  // ================================

  /**
   * Tests the {@link Deck#getValueCounts()} method.
   * <p>
   * Verifies that the initial value counts are correct based on the number of
   * decks and ranks.
   * </p>
   */
  @Test
  public void testInitialValueCounts() {
    int[] valueCounts = deck.getValueCounts();
    assertNotNull("getValueCounts should not return null", valueCounts);
    assertEquals("valueCounts should have 10 elements", 10, valueCounts.length);

    // Check if each value count matches the expected initial count
    for (int i = 0; i < valueCounts.length - 1; i++) {
      int expectedCount;

      if (i != 9) {
        expectedCount = GameRules.NUMBER_OF_DECKS * 4;
      } else {
        expectedCount = GameRules.NUMBER_OF_DECKS * 16;
      }

      assertEquals("Initial count for value " + (i + 1) + " should be " + expectedCount,
          expectedCount, valueCounts[i]);
    }
  }

  /**
   * Tests that the value counts update correctly after adding a card.
   * <p>
   * Adds a card of a specific rank and verifies that the corresponding value
   * count increases by 1.
   * </p>
   */
  @Test
  public void testValueCountsAfterAdd() {
    Card.Rank rankToAdd = Card.Rank.THREE;
    int initialCount = deck.getValueCounts()[rankToAdd.getValue() - 1];

    deck.add(rankToAdd);

    int[] valueCounts = deck.getValueCounts();
    assertEquals("Count for value " + rankToAdd.getValue() + " should increase by 1 after add",
        initialCount + 1, valueCounts[rankToAdd.getValue() - 1]);
  }

  /**
   * Tests that the value counts update correctly after removing a card.
   * <p>
   * Removes a card of a specific rank and verifies that the corresponding value
   * count decreases by 1.
   * </p>
   */
  @Test
  public void testValueCountsAfterRemove() {
    Card.Rank rankToRemove = Card.Rank.SEVEN;
    int initialCount = deck.getValueCounts()[rankToRemove.getValue() - 1];

    deck.remove(rankToRemove);

    int[] valueCounts = deck.getValueCounts();
    assertEquals("Count for value " + rankToRemove.getValue() + " should decrease by 1 after remove",
        initialCount - 1, valueCounts[rankToRemove.getValue() - 1]);
  }

  /**
   * Tests that modifying the returned value counts array does not affect the
   * internal array.
   * <p>
   * Ensures that {@link Deck#getValueCounts()} returns a deep copy of the
   * internal
   * value counts array, preventing external modifications.
   * </p>
   */
  @Test
  public void testValueCountsDeepCopy() {
    int[] valueCounts = deck.getValueCounts();
    valueCounts[0] = -1; // Modify the returned array

    int[] updatedValueCounts = deck.getValueCounts();
    assertNotEquals("Modifying returned array should not change internal valueCounts",
        -1, updatedValueCounts[0]);
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests the {@link Deck#toString()} method.
   * <p>
   * Verifies that the method returns the correct string representation of the
   * deck, listing all cards separated by newlines.
   * </p>
   */
  @Test
  public void testToString() {
    StringBuilder expectedOutput = new StringBuilder();

    for (Card card : deck.getCards()) {
      expectedOutput.append(card).append("\n");
    }

    assertEquals("toString should return all cards separated by newlines",
        expectedOutput.toString(), deck.toString());
  }

  // ================================
  // Tests for Additional Scenarios
  // ================================

  /**
   * Tests removing all instances of a specific rank and verifies deck integrity.
   * <p>
   * Removes all instances of a given rank and ensures the deck no longer contains
   * that rank, and that the count reflects the removals appropriately.
   * </p>
   */
  @Test
  public void testEmptyDeck() {
    // Remove all cards from the deck
    int totalCards = GameRules.NUMBER_OF_DECKS * 4 * Card.Rank.values().length;

    for (int i = 0; i < totalCards; i++) {
      // Remove the first card repeatedly
      Card.Rank rank = deck.getCards().get(0).getRank();
      deck.remove(rank);
    }

    assertEquals("Deck size should be 0 after removing all cards", 0, deck.getSize());
    assertEquals("Count should be 0 after removing all cards", 0.0, deck.getCount(), 0.0001);

    // Attempting to remove a card from an empty deck should throw
    // IllegalStateException
    try {
      deck.remove(Card.Rank.ACE);
      fail("Removing a card from an empty deck should throw IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

}