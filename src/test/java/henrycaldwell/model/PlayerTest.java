package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link Player} class.
 * <p>
 * This class contains unit tests to verify the correctness of the
 * {@link Player} class, including its constructors, {@code addHand},
 * {@code removeHand}, {@code canSplit}, {@code splitHand},
 * {@code getHands}, {@code getNumHands}, {@code clone},
 * and {@code toString} methods.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Hand hand1 = new Hand();
 * hand1.add(Card.Rank.ACE);
 * hand1.add(Card.Rank.KING);
 * Player player = new Player(Arrays.asList(hand1));
 * assertEquals(1, player.getNumHands());
 * }</pre>
 */
public class PlayerTest {

  private Player player;
  private Hand hand1;
  private Hand hand2;
  private Hand hand3;

  /**
   * Setup method to initialize a new {@link Player} instance and sample
   * {@link Hand}s before each test.
   * <p>
   * Ensures that the player and hands are initialized correctly before each test
   * case.
   * </p>
   */
  @Before
  public void setUp() {
    player = new Player();

    // Initialize hand1 with ACE and KING
    hand1 = new Hand();
    hand1.add(Card.Rank.ACE);
    hand1.add(Card.Rank.KING);

    // Initialize hand2 with two TENs
    hand2 = new Hand();
    hand2.add(Card.Rank.TEN);
    hand2.add(Card.Rank.TEN);

    // Initialize hand3 with TWO and THREE
    hand3 = new Hand();
    hand3.add(Card.Rank.TWO);
    hand3.add(Card.Rank.THREE);
  }

  // ================================
  // Constructor Tests
  // ================================

  /**
   * Tests the {@link Player} default constructor initializes correctly with no
   * hands.
   * <p>
   * Scenario: Creating a {@code Player} instance using the default constructor.
   * </p>
   * <p>
   * Expected Outcome: The player is not null and has an initial number of hands
   * equal to 0.
   * </p>
   */
  @Test
  public void testDefaultConstructor() {
    assertNotNull("Player should not be null after initialization", player);
    assertEquals("Initial number of hands should be 0", 0, player.getNumHands());
  }

  /**
   * Tests the {@link Player} constructor initializes correctly with a predefined
   * list of hands.
   * <p>
   * Scenario: Creating a {@code Player} instance with a predefined list of hands
   * (hand1 and hand2).
   * </p>
   * <p>
   * Expected Outcome: The player is not null, has the correct number of hands,
   * and
   * contains all the provided hands.
   * </p>
   */
  @Test
  public void testConstructorWithHands() {
    List<Hand> initialHands = Arrays.asList(hand1, hand2);
    Player newPlayer = new Player(initialHands);

    assertNotNull("Player should not be null after initialization with hands", newPlayer);
    assertEquals("Initial number of hands should match the provided list", 2, newPlayer.getNumHands());
    assertTrue("Player should contain hand1", newPlayer.getHands().contains(hand1));
    assertTrue("Player should contain hand2", newPlayer.getHands().contains(hand2));
  }

  /**
   * Tests the {@link Player} constructor throws {@link IllegalArgumentException}
   * when initialized with a null hands list.
   * <p>
   * Scenario: Attempting to create a {@code Player} with a {@code null} hands
   * list.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullHands() {
    new Player(null);
  }

  // ================================
  // Tests for addHand Method
  // ================================

  /**
   * Tests the {@link Player#addHand(Hand)} method by adding a valid hand to the
   * player.
   * <p>
   * Scenario: Adding a valid hand (hand1) to the player.
   * </p>
   * <p>
   * Expected Outcome: The number of hands increases by 1, and the player contains
   * the added hand.
   * </p>
   */
  @Test
  public void testAddHandValid() {
    player.addHand(hand1);
    assertEquals("Number of hands should increase by 1 after adding a hand", 1, player.getNumHands());
    assertTrue("Player should contain the added hand", player.getHands().contains(hand1));
  }

  /**
   * Tests the {@link Player#addHand(Hand)} method by attempting to add a null
   * hand.
   * <p>
   * Scenario: Attempting to add a {@code null} hand to the player.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddHandNull() {
    player.addHand(null);
  }

  /**
   * Tests the {@link Player#addHand(Hand)} method by adding multiple hands.
   * <p>
   * Scenario: Adding multiple hands (hand1, hand2, and hand3) to the player.
   * </p>
   * <p>
   * Expected Outcome: The number of hands reflects all added hands, and the
   * player contains each of them.
   * </p>
   */
  @Test
  public void testAddMultipleHands() {
    player.addHand(hand1);
    player.addHand(hand2);
    player.addHand(hand3);

    assertEquals("Player should have 3 hands after adding three hands", 3, player.getNumHands());
    assertTrue("Player should contain hand1", player.getHands().contains(hand1));
    assertTrue("Player should contain hand2", player.getHands().contains(hand2));
    assertTrue("Player should contain hand3", player.getHands().contains(hand3));
  }

  // ================================
  // Tests for removeHand Method
  // ================================

  /**
   * Tests the {@link Player#removeHand(int)} method by removing a hand at a valid
   * index.
   * <p>
   * Scenario: Removing a hand from the player using a valid index.
   * </p>
   * <p>
   * Expected Outcome: The number of hands decreases by 1, and the specified hand
   * is
   * removed from the player.
   * </p>
   */
  @Test
  public void testRemoveHandValid() {
    player.addHand(hand1);
    player.addHand(hand2);
    assertEquals("Player should have 2 hands before removal", 2, player.getNumHands());

    player.removeHand(0);
    assertEquals("Number of hands should decrease by 1 after removal", 1, player.getNumHands());

    assertFalse("Player should no longer contain the removed hand", player.getHands().contains(hand1));
    assertTrue("Player should still contain the other hand", player.getHands().contains(hand2));
  }

  /**
   * Tests the {@link Player#removeHand(int)} method by attempting to remove a
   * hand at a negative index.
   * <p>
   * Scenario: Attempting to remove a hand using a negative index.
   * </p>
   * <p>
   * Expected Outcome: An {@link IndexOutOfBoundsException} is thrown.
   * </p>
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveHandNegativeIndex() {
    player.addHand(hand1);
    player.removeHand(-1);
  }

  /**
   * Tests the {@link Player#removeHand(int)} method by attempting to remove a
   * hand at an index equal to the number of hands.
   * <p>
   * Scenario: Attempting to remove a hand using an index equal to the number of
   * hands.
   * </p>
   * <p>
   * Expected Outcome: An {@link IndexOutOfBoundsException} is thrown.
   * </p>
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveHandIndexEqualSize() {
    player.addHand(hand1);
    player.removeHand(1);
  }

  /**
   * Tests the {@link Player#removeHand(int)} method by removing all hands from
   * the player.
   * <p>
   * Scenario: Removing all hands one by one from the player.
   * </p>
   * <p>
   * Expected Outcome: The player ends up with no hands.
   * </p>
   */
  @Test
  public void testRemoveAllHands() {
    player.addHand(hand1);
    player.addHand(hand2);
    player.addHand(hand3);
    assertEquals("Player should have 3 hands before removals", 3, player.getNumHands());

    player.removeHand(0);
    player.removeHand(0);
    player.removeHand(0);
    assertEquals("Player should have 0 hands after removing all hands", 0, player.getNumHands());
  }

  // ================================
  // Tests for canSplit Method
  // ================================

  /**
   * Tests the {@link Player#canSplit(int)} method returns {@code true} for a
   * splittable hand.
   * <p>
   * Scenario: Checking if a hand with two identical ranks (hand2 with two TENs)
   * can
   * be split.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code true}.
   * </p>
   */
  @Test
  public void testCanSplitTrue() {
    player.addHand(hand2); // hand2 has two TENs
    assertTrue("canSplit should return true for a hand with two identical ranks", player.canSplit(0));
  }

  /**
   * Tests the {@link Player#canSplit(int)} method returns {@code false} for a
   * non-splittable hand with different ranks.
   * <p>
   * Scenario: Checking if a hand with different ranks (hand1 with ACE and KING)
   * can be split.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code false}.
   * </p>
   */
  @Test
  public void testCanSplitFalseDifferentRanks() {
    player.addHand(hand1); // hand1 has ACE and KING
    assertFalse("canSplit should return false for a hand with different ranks", player.canSplit(0));
  }

  /**
   * Tests the {@link Player#canSplit(int)} method returns {@code false} for a
   * hand with more than two cards.
   * <p>
   * Scenario: Checking if a hand with more than two cards (hand with three FIVES)
   * can be split.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code false}.
   * </p>
   */
  @Test
  public void testCanSplitFalseMoreThanTwoCards() {
    Hand multiCardHand = new Hand();
    multiCardHand.add(Card.Rank.FIVE);
    multiCardHand.add(Card.Rank.FIVE);
    multiCardHand.add(Card.Rank.FIVE);
    player.addHand(multiCardHand);
    assertFalse("canSplit should return false for a hand with more than two cards", player.canSplit(0));
  }

  /**
   * Tests the {@link Player#canSplit(int)} method by attempting to split at an
   * invalid index.
   * <p>
   * Scenario: Attempting to check splitting capability on a non-existent hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IndexOutOfBoundsException} is thrown.
   * </p>
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testCanSplitInvalidIndex() {
    player.canSplit(0); // No hands added yet
  }

  // ================================
  // Tests for splitHand Method
  // ================================

  /**
   * Tests the {@link Player#splitHand(int)} method successfully splits a
   * splittable hand.
   * <p>
   * Scenario: Splitting a splittable hand (hand2 with two TENs).
   * </p>
   * <p>
   * Expected Outcome: The number of hands increases by 1, and both split hands
   * contain one TEN each.
   * </p>
   */
  @Test
  public void testSplitHandSuccess() {
    player.addHand(hand2);
    assertEquals("Player should have 1 hand before splitting", 1, player.getNumHands());

    player.splitHand(0);
    assertEquals("Player should have 2 hands after splitting", 2, player.getNumHands());

    List<Hand> currentHands = player.getHands();
    Hand firstSplit = currentHands.get(0);
    Hand secondSplit = currentHands.get(1);

    assertEquals("First split hand should have 1 card", 1, firstSplit.getSize());
    assertEquals("Second split hand should have 1 card", 1, secondSplit.getSize());

    assertEquals("First split hand should contain TEN", Card.Rank.TEN, firstSplit.getCards().get(0).getRank());
    assertEquals("Second split hand should contain TEN", Card.Rank.TEN, secondSplit.getCards().get(0).getRank());
  }

  /**
   * Tests the {@link Player#splitHand(int)} method by attempting to split a
   * non-splittable hand.
   * <p>
   * Scenario: Attempting to split a hand with different ranks (hand1 with ACE and
   * KING).
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSplitHandCannotSplit() {
    player.addHand(hand1); // hand1 has ACE and KING
    player.splitHand(0); // Should throw exception
  }

  /**
   * Tests the {@link Player#splitHand(int)} method by attempting to split at an
   * invalid index.
   * <p>
   * Scenario: Attempting to split a hand at an index where no hand exists.
   * </p>
   * <p>
   * Expected Outcome: An {@link IndexOutOfBoundsException} is thrown.
   * </p>
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testSplitHandInvalidIndex() {
    player.splitHand(0); // No hands added yet
  }

  // ================================
  // Tests for getHands Method
  // ================================

  /**
   * Tests the {@link Player#getHands()} method returns a defensive copy of the
   * hands list.
   * <p>
   * Scenario: Retrieving the list of hands and attempting to modify it
   * externally.
   * </p>
   * <p>
   * Expected Outcome: Modifications to the retrieved list do not affect the
   * original
   * player's hands.
   * </p>
   */
  @Test
  public void testGetHandsDefensiveCopy() {
    player.addHand(hand1);
    player.addHand(hand2);

    List<Hand> retrievedHands = player.getHands();
    assertEquals("Retrieved hands should match the player's hands size", 2, retrievedHands.size());
    assertTrue("Retrieved hands should contain hand1", retrievedHands.contains(hand1));
    assertTrue("Retrieved hands should contain hand2", retrievedHands.contains(hand2));

    retrievedHands.remove(0);
    assertEquals("Modifying retrieved list should not affect the player's hands", 2, player.getNumHands());
    assertTrue("Player should still contain hand1", player.getHands().contains(hand1));
    assertTrue("Player should still contain hand2", player.getHands().contains(hand2));
  }

  /**
   * Tests the {@link Player#getHands()} method returns a new list instance each
   * time.
   * <p>
   * Scenario: Ensuring that each call to {@code getHands()} returns a new list
   * instance.
   * </p>
   * <p>
   * Expected Outcome: Each retrieval is a different instance.
   * </p>
   */
  @Test
  public void testGetHandsReturnsNewList() {
    player.addHand(hand1);
    List<Hand> firstRetrieval = player.getHands();
    List<Hand> secondRetrieval = player.getHands();

    assertNotSame("Each call to getHands should return a new list instance", firstRetrieval, secondRetrieval);
  }

  /**
   * Tests the {@link Player#getHands()} method ensures the hands list is
   * immutable externally.
   * <p>
   * Scenario: Attempting to modify the retrieved hands list externally.
   * </p>
   * <p>
   * Expected Outcome: The original player's hands remain unchanged despite
   * external
   * modifications.
   * </p>
   */
  @Test
  public void testGetHandsDefensiveCopyImmutability() {
    player.addHand(hand1);
    List<Hand> retrievedHands = player.getHands();

    retrievedHands.add(hand2);
    retrievedHands.remove(hand1);

    assertEquals("Player's hands should remain unchanged after external modification attempts", 1,
        player.getNumHands());
    assertTrue("Player should still contain hand1", player.getHands().contains(hand1));
    assertFalse("Player should not contain hand2", player.getHands().contains(hand2));
  }

  // ================================
  // Tests for getNumHands Method
  // ================================

  /**
   * Tests the {@link Player#getNumHands()} method reflects the correct number of
   * hands.
   * <p>
   * Scenario: Adding and removing hands from the player and verifying the count.
   * </p>
   * <p>
   * Expected Outcome: The method returns the accurate count of hands in the
   * player.
   * </p>
   */
  @Test
  public void testGetNumHands() {
    assertEquals("Initial number of hands should be 0", 0, player.getNumHands());

    player.addHand(hand1);
    assertEquals("Number of hands should be 1 after adding a hand", 1, player.getNumHands());

    player.addHand(hand2);
    assertEquals("Number of hands should be 2 after adding another hand", 2, player.getNumHands());

    player.removeHand(0);
    assertEquals("Number of hands should be 1 after removing a hand", 1, player.getNumHands());
  }

  // ================================
  // Tests for clone Method
  // ================================

  /**
   * Tests the {@link Player#clone()} method creates an accurate deep copy of the
   * player.
   * <p>
   * Scenario: Cloning a player containing two hands (hand1 and hand2).
   * </p>
   * <p>
   * Expected Outcome: The cloned player is a separate instance with identical
   * hands. Modifications to the original player do not affect the cloned player.
   * </p>
   */
  @Test
  public void testClone() {
    player.addHand(hand1);
    player.addHand(hand2);

    Player clonedPlayer = player.clone();
    assertNotNull("Cloned player should not be null", clonedPlayer);
    assertEquals("Cloned player should have the same number of hands as the original", player.getNumHands(),
        clonedPlayer.getNumHands());

    List<Hand> originalHands = player.getHands();
    List<Hand> clonedHands = clonedPlayer.getHands();

    for (int i = 0; i < originalHands.size(); i++) {
      Hand originalHand = originalHands.get(i);
      Hand clonedHand = clonedHands.get(i);
      assertNotSame("Cloned hand should be a different instance", originalHand, clonedHand);
      assertEquals("Cloned hand should have the same number of cards as the original", originalHand.getSize(),
          clonedHand.getSize());

      for (int j = 0; j < originalHand.getSize(); j++) {
        Card originalCard = originalHand.getCards().get(j);
        Card clonedCard = clonedHand.getCards().get(j);
        assertEquals("Cloned card should have the same rank as the original", originalCard.getRank(),
            clonedCard.getRank());
      }
    }

    player.addHand(hand3);
    assertEquals("Original player should have 3 hands after adding a new hand", 3, player.getNumHands());
    assertEquals("Cloned player should still have 2 hands", 2, clonedPlayer.getNumHands());

    assertFalse("Cloned player should not contain hand3", clonedPlayer.getHands().contains(hand3));
  }

  /**
   * Tests the {@link Player#clone()} method on an empty player.
   * <p>
   * Scenario: Cloning a player with no hands.
   * </p>
   * <p>
   * Expected Outcome: The cloned player is a separate instance with no hands.
   * </p>
   */
  @Test
  public void testCloneEmptyPlayer() {
    Player clonedPlayer = player.clone();
    assertNotNull("Cloned player should not be null", clonedPlayer);
    assertEquals("Cloned player should have 0 hands", 0, clonedPlayer.getNumHands());
  }

  /**
   * Tests that modifying a cloned player's hands does not affect the original
   * player.
   * <p>
   * Scenario: Cloning a player and modifying the cloned player's hands.
   * </p>
   * <p>
   * Expected Outcome: The original player's hands remain unchanged after
   * modifications to the cloned player.
   * </p>
   */
  @Test
  public void testCloneIndependence() {
    player.addHand(hand1);
    Player clonedPlayer = player.clone();

    clonedPlayer.removeHand(0);
    clonedPlayer.addHand(hand2);

    assertEquals("Original player should still have 1 hand after modifying the clone", 1, player.getNumHands());
    assertTrue("Original player should still contain hand1", player.getHands().contains(hand1));

    assertEquals("Cloned player should have 1 hand after removal and addition", 1, clonedPlayer.getNumHands());
    assertTrue("Cloned player should contain hand2 after modification", clonedPlayer.getHands().contains(hand2));
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests the {@link Player#toString()} method returns the correct string
   * representation with multiple hands.
   * <p>
   * Scenario: Player has two hands (hand1 and hand2).
   * </p>
   * <p>
   * Expected Outcome: The string representation lists all hands with their
   * respective string representations.
   * </p>
   */
  @Test
  public void testToStringMultipleHands() {
    player.addHand(hand1);
    player.addHand(hand2);

    String expected = "Hand 1: " + hand1.toString() + "\n" + "Hand 2: " + hand2.toString();
    assertEquals("toString should return the correct string representation with multiple hands", expected,
        player.toString());
  }

  /**
   * Tests the {@link Player#toString()} method returns the correct string
   * representation for a single hand.
   * <p>
   * Scenario: Player has one hand (hand1).
   * </p>
   * <p>
   * Expected Outcome: The string representation lists the single hand with its
   * string representation.
   * </p>
   */
  @Test
  public void testToStringSingleHand() {
    player.addHand(hand1);

    String expected = "Hand 1: " + hand1.toString();
    assertEquals("toString should return the correct string representation for a single hand", expected,
        player.toString());
  }

  /**
   * Tests the {@link Player#toString()} method returns an empty string for a
   * player with no hands.
   * <p>
   * Scenario: Player has no hands.
   * </p>
   * <p>
   * Expected Outcome: The method returns an empty string.
   * </p>
   */
  @Test
  public void testToStringNoHands() {
    String expected = "";
    assertEquals("toString should return an empty string for a player with no hands", expected, player.toString());
  }

  // ================================
  // Additional Edge Case Tests
  // ================================

  /**
   * Tests splitting a hand and ensures the hands list is updated correctly.
   * <p>
   * Scenario: Splitting a splittable hand (hand2 with two TENs) and verifying
   * hands list.
   * </p>
   * <p>
   * Expected Outcome: The player has two hands, each containing one TEN, and the
   * order of hands is maintained correctly.
   * </p>
   */
  @Test
  public void testSplitHandUpdatesHandsList() {
    player.addHand(hand2);

    player.splitHand(0);
    assertEquals("Player should have 2 hands after splitting", 2, player.getNumHands());

    List<Hand> currentHands = player.getHands();
    Hand firstSplit = currentHands.get(0);
    Hand secondSplit = currentHands.get(1);

    assertEquals("First split hand should have 1 card", 1, firstSplit.getSize());
    assertEquals("Second split hand should have 1 card", 1, secondSplit.getSize());

    assertEquals("First split hand should contain TEN", Card.Rank.TEN, firstSplit.getCards().get(0).getRank());
    assertEquals("Second split hand should contain TEN", Card.Rank.TEN, secondSplit.getCards().get(0).getRank());
  }

  /**
   * Tests splitting a hand results in hands being in the correct order.
   * <p>
   * Scenario: Splitting a splittable hand (hand2 with two TENs) and verifying the
   * order of split hands.
   * </p>
   * <p>
   * Expected Outcome: The first split hand corresponds to the first card, and the
   * second split hand corresponds to the second card.
   * </p>
   */
  @Test
  public void testSplitHandOrder() {
    player.addHand(hand2); // hand2 has two TENs

    player.splitHand(0);
    List<Hand> currentHands = player.getHands();

    Hand firstSplit = currentHands.get(0);
    Hand secondSplit = currentHands.get(1);

    assertEquals("First split hand should be at index 0", hand2.getCards().get(0).getRank(),
        firstSplit.getCards().get(0).getRank());
    assertEquals("Second split hand should be at index 1", hand2.getCards().get(1).getRank(),
        secondSplit.getCards().get(0).getRank());
  }

}
