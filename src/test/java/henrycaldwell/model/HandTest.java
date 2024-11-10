package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link Hand} class.
 * <p>
 * This class contains unit tests to verify the correctness of the {@link Hand}
 * class, including its constructor, {@code add}, {@code remove},
 * {@code contains}, {@code evaluateHand}, {@code isSoftHand}, {@code getCards},
 * {@code setCards}, {@code getSize}, {@code clone}, and {@code toString}
 * methods.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Hand hand = new Hand();
 * hand.add(Card.Rank.ACE);
 * assertEquals(1, hand.getSize());
 * }</pre>
 */
public class HandTest {

  private Hand hand;

  /**
   * Setup method to initialize a new {@link Hand} instance before each test.
   * <p>
   * Ensures that the hand is initialized as empty before each test case.
   * </p>
   */
  @Before
  public void setUp() {
    hand = new Hand();
  }

  /**
   * Helper method to count occurrences of a specific rank in the hand.
   *
   * @param rank The rank to count.
   * @return The number of times the rank appears in the hand.
   */
  private int countRankInHand(Card.Rank rank) {
    int count = 0;
    for (Card card : hand.getCards()) {
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
   * Tests the {@link Hand} constructor initializes correctly.
   * <p>
   * Scenario: Creating a {@code Hand} instance using the default constructor.
   * </p>
   * <p>
   * Expected Outcome: The hand is not null and has an initial size of 0.
   * </p>
   */
  @Test
  public void testConstructor() {
    assertNotNull("Hand should not be null after initialization", hand);
    assertEquals("Initial hand size should be 0", 0, hand.getSize());
  }

  // ================================
  // Tests for add Method
  // ================================

  /**
   * Tests the {@link Hand#add(Card.Rank)} method with a valid rank.
   * <p>
   * Scenario: Adding a valid rank (JACK) to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size increases by 1 and contains the added rank.
   * </p>
   */
  @Test
  public void testAddValidRank() {
    hand.add(Card.Rank.JACK);
    assertEquals("Hand size should increase by 1 after adding a card", 1, hand.getSize());
    assertTrue("Hand should contain the added rank", hand.contains(Card.Rank.JACK));
  }

  /**
   * Tests the {@link Hand#add(Card.Rank)} method with multiple valid ranks.
   * <p>
   * Scenario: Adding multiple valid ranks (ACE, KING, FIVE) to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size reflects the number of added cards, and all
   * added ranks are present in the hand.
   * </p>
   */
  @Test
  public void testAddMultipleValidRanks() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.FIVE);
    assertEquals("Hand size should reflect the number of added cards", 3, hand.getSize());
    assertTrue("Hand should contain ACE", hand.contains(Card.Rank.ACE));
    assertTrue("Hand should contain KING", hand.contains(Card.Rank.KING));
    assertTrue("Hand should contain FIVE", hand.contains(Card.Rank.FIVE));
  }

  /**
   * Tests the {@link Hand#add(Card.Rank)} method with duplicate ranks.
   * <p>
   * Scenario: Adding multiple instances of the same rank (TEN) to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size increases accordingly, and the hand contains
   * all instances of the added rank.
   * </p>
   */
  @Test
  public void testAddDuplicateRanks() {
    hand.add(Card.Rank.TEN);
    hand.add(Card.Rank.TEN);
    hand.add(Card.Rank.TEN);
    assertEquals("Hand size should be 3 after adding three TENs", 3, hand.getSize());
    assertTrue("Hand should contain TEN", hand.contains(Card.Rank.TEN));

    int tenCount = countRankInHand(Card.Rank.TEN);
    assertEquals("Hand should contain three TENs", 3, tenCount);
  }

  /**
   * Tests the {@link Hand#add(Card.Rank)} method with a {@code null} rank.
   * <p>
   * Scenario: Attempting to add a {@code null} rank to the hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullRank() {
    hand.add(null);
  }

  // ================================
  // Tests for remove Method
  // ================================

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method with a rank that exists in
   * the hand.
   * <p>
   * Scenario: Removing an existing rank (KING) from the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size decreases by 1, and one instance of the
   * rank is removed while others remain if duplicates exist.
   * </p>
   */
  @Test
  public void testRemoveExistingRank() {
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.QUEEN);
    hand.add(Card.Rank.KING);
    assertTrue("Hand should contain KING before removal", hand.contains(Card.Rank.KING));

    hand.remove(Card.Rank.KING);
    assertEquals("Hand size should decrease by 1 after removing a KING", 2, hand.getSize());

    int kingCount = countRankInHand(Card.Rank.KING);
    assertEquals("Hand should contain one KING after removal", 1, kingCount);
  }

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method removes all instances of a
   * duplicated rank.
   * <p>
   * Scenario: Removing all instances of a rank (NINE) from the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size becomes 0, and the rank no longer exists in
   * the hand.
   * </p>
   */
  @Test
  public void testRemoveAllInstancesOfRank() {
    hand.add(Card.Rank.NINE);
    hand.add(Card.Rank.NINE);
    hand.add(Card.Rank.NINE);
    assertEquals("Hand should have three NINEs", 3, hand.getSize());

    hand.remove(Card.Rank.NINE);
    hand.remove(Card.Rank.NINE);
    hand.remove(Card.Rank.NINE);
    assertEquals("Hand size should be 0 after removing all NINEs", 0, hand.getSize());

    assertFalse("Hand should not contain NINE after removal", hand.contains(Card.Rank.NINE));
  }

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method with a rank that does not
   * exist in the hand.
   * <p>
   * Scenario: Attempting to remove a rank (KING) that has no instances in the
   * hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalStateException} is thrown.
   * </p>
   */
  @Test(expected = IllegalStateException.class)
  public void testRemoveNonExistingRank() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.FIVE);
    hand.remove(Card.Rank.KING);
  }

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method with a {@code null} rank.
   * <p>
   * Scenario: Attempting to remove a {@code null} rank from the hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullRank() {
    hand.remove(null);
  }

  // ================================
  // Tests for contains Method
  // ================================

  /**
   * Tests the {@link Hand#contains(Card.Rank)} method for a rank that exists in
   * the hand.
   * <p>
   * Scenario: Checking if the hand contains an existing rank (FIVE).
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code true}.
   * </p>
   */
  @Test
  public void testContainsExistingRank() {
    hand.add(Card.Rank.FIVE);
    assertTrue("contains should return true for a rank present in the hand", hand.contains(Card.Rank.FIVE));
  }

  /**
   * Tests the {@link Hand#contains(Card.Rank)} method for a rank that does not
   * exist in the hand.
   * <p>
   * Scenario: Checking if the hand contains a non-existing rank (TWO).
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code false}.
   * </p>
   */
  @Test
  public void testContainsNonExistingRank() {
    hand.add(Card.Rank.SEVEN);
    assertFalse("contains should return false for a rank not present in the hand", hand.contains(Card.Rank.TWO));
  }

  /**
   * Tests the {@link Hand#contains(Card.Rank)} method with a {@code null} rank.
   * <p>
   * Scenario: Attempting to check containment of a {@code null} rank.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testContainsNullRank() {
    hand.contains(null);
  }

  // ================================
  // Tests for evaluateHand Method
  // ================================

  /**
   * Tests the {@link Hand#evaluateHand()} method with no cards.
   * <p>
   * Scenario: Evaluating an empty hand.
   * </p>
   * <p>
   * Expected Outcome: The method returns 0.
   * </p>
   */
  @Test
  public void testEvaluateHandEmpty() {
    assertEquals("evaluateHand should return 0 for an empty hand", 0, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with multiple cards, including
   * an ace counted as 11.
   * <p>
   * Scenario: Adding ACE and SEVEN to the hand.
   * </p>
   * <p>
   * Expected Outcome: The ace is counted as 11, resulting in a total of 18.
   * </p>
   */
  @Test
  public void testEvaluateHandWithAceAsEleven() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.SEVEN);
    assertEquals("Hand should evaluate Ace as 11 without busting", 18, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with an ace that must be counted
   * as 1 to prevent busting.
   * <p>
   * Scenario: Adding ACE, TEN, and TWO to the hand.
   * </p>
   * <p>
   * Expected Outcome: The ace is counted as 1, resulting in a total of 13.
   * </p>
   */
  @Test
  public void testEvaluateHandWithAceAsOne() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.TEN);
    hand.add(Card.Rank.TWO);
    assertEquals("Hand should evaluate Ace as 1 to prevent busting", 13, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with multiple aces.
   * <p>
   * Scenario: Adding two ACEs and a NINE to the hand.
   * </p>
   * <p>
   * Expected Outcome: One ace is counted as 11 and the other as 1, resulting in a
   * total of 21.
   * </p>
   */
  @Test
  public void testEvaluateHandMultipleAces() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.NINE);
    assertEquals("Hand should evaluate multiple Aces correctly", 21, hand.evaluateHand());

    hand.add(Card.Rank.THREE);
    assertEquals("Hand should adjust Aces to prevent busting", 14, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with high cards.
   * <p>
   * Scenario: Adding KING, QUEEN, and JACK to the hand.
   * </p>
   * <p>
   * Expected Outcome: The total is 30.
   * </p>
   */
  @Test
  public void testEvaluateHandHighCards() {
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.QUEEN);
    hand.add(Card.Rank.JACK);
    assertEquals("Hand should correctly evaluate high cards", 30, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with a combination that includes
   * multiple aces and high cards.
   * <p>
   * Scenario: Adding two ACEs, SEVEN, and THREE to the hand.
   * </p>
   * <p>
   * Expected Outcome: Both aces are counted as 1 to prevent busting, resulting in
   * a total of 12.
   * </p>
   */
  @Test
  public void testEvaluateHandComplexCombination() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.SEVEN);
    hand.add(Card.Rank.THREE);

    assertEquals("Hand should evaluate to 12 with multiple Aces avoiding busting", 12, hand.evaluateHand());
    assertFalse("isSoftHand should return false as Aces are counted as 1", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with maximum possible hand
   * without busting.
   * <p>
   * Scenario: Adding ACE, EIGHT, and TWO to the hand.
   * </p>
   * <p>
   * Expected Outcome: The ace is counted as 11, resulting in a total of 21.
   * </p>
   */
  @Test
  public void testEvaluateHandMaxWithoutBusting() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.EIGHT);
    hand.add(Card.Rank.TWO);

    assertEquals("Hand should evaluate to 21", 21, hand.evaluateHand());
    assertTrue("isSoftHand should return true as Ace is counted as 11", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with busting condition.
   * <p>
   * Scenario: Adding KING, QUEEN, and JACK to the hand.
   * </p>
   * <p>
   * Expected Outcome: The total is 30, indicating a bust.
   * </p>
   */
  @Test
  public void testEvaluateHandBust() {
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.QUEEN);
    hand.add(Card.Rank.JACK);

    assertEquals("Hand should evaluate to 30", 30, hand.evaluateHand());
    assertFalse("isSoftHand should return false when busting", hand.isSoftHand());
  }

  // ================================
  // Tests for isSoftHand Method
  // ================================

  /**
   * Tests the {@link Hand#isSoftHand()} method for a soft hand.
   * <p>
   * Scenario: Adding ACE and SEVEN to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand is considered soft since the ace is counted as 11.
   * </p>
   */
  @Test
  public void testIsSoftHandTrue() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.SEVEN);
    assertTrue("isSoftHand should return true for a soft hand", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#isSoftHand()} method for a hard hand.
   * <p>
   * Scenario: Adding ACE, TEN, and FIVE to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand is considered hard since the ace must be counted
   * as 1 to prevent busting.
   * </p>
   */
  @Test
  public void testIsSoftHandFalse() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.TEN);
    hand.add(Card.Rank.FIVE);
    assertFalse("isSoftHand should return false when Ace must be counted as 1 to prevent busting", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#isSoftHand()} method with no aces.
   * <p>
   * Scenario: Adding TWO and THREE to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand is not soft as there are no aces.
   * </p>
   */
  @Test
  public void testIsSoftHandNoAces() {
    hand.add(Card.Rank.TWO);
    hand.add(Card.Rank.THREE);
    assertFalse("isSoftHand should return false when there are no aces", hand.isSoftHand());
  }

  // ================================
  // Tests for getCards Method
  // ================================

  /**
   * Tests the {@link Hand#getCards()} method returns a defensive copy.
   * <p>
   * Scenario: Retrieving the list of cards and attempting to modify it
   * externally.
   * </p>
   * <p>
   * Expected Outcome: Modifications to the retrieved list do not affect the
   * original hand.
   * </p>
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testGetCardsDefensiveCopy() {
    hand.add(Card.Rank.FOUR);
    hand.add(Card.Rank.FIVE);

    List<Card> retrievedCards = hand.getCards();
    assertEquals("Retrieved cards should match the hand's size", 2, retrievedCards.size());
    assertTrue("Retrieved cards should contain FOUR", retrievedCards.contains(new Card(Card.Rank.FOUR)));
    assertTrue("Retrieved cards should contain FIVE", retrievedCards.contains(new Card(Card.Rank.FIVE)));

    retrievedCards.remove(Card.Rank.FOUR);
    assertEquals("Modifying retrieved list should not affect the original hand", 2, hand.getSize());
    assertTrue("Original hand should still contain FOUR", hand.contains(Card.Rank.FOUR));
    assertTrue("Original hand should still contain FIVE", hand.contains(Card.Rank.FIVE));
  }

  // ================================
  // Tests for setCards Method
  // ================================

  /**
   * Tests the {@link Hand#setCards(List)} method with a valid list of ranks.
   * <p>
   * Scenario: Setting a new list of ranks (NINE, TEN, ACE) to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size matches the set list size, and all specified
   * ranks are present in the hand.
   * </p>
   */
  @Test
  public void testSetCardsValidList() {
    List<Card.Rank> newRanks = Arrays.asList(Card.Rank.NINE, Card.Rank.TEN, Card.Rank.ACE);
    hand.setCards(newRanks);

    assertEquals("Hand size should match the set list size", 3, hand.getSize());
    assertTrue("Hand should contain NINE", hand.contains(Card.Rank.NINE));
    assertTrue("Hand should contain TEN", hand.contains(Card.Rank.TEN));
    assertTrue("Hand should contain ACE", hand.contains(Card.Rank.ACE));
  }

  /**
   * Tests the {@link Hand#setCards(List)} method with an empty list.
   * <p>
   * Scenario: Setting an empty list of ranks to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand size becomes 0.
   * </p>
   */
  @Test
  public void testSetCardsEmptyList() {
    List<Card.Rank> emptyRanks = new ArrayList<>();
    hand.setCards(emptyRanks);

    assertEquals("Hand size should be 0 after setting an empty list", 0, hand.getSize());
  }

  /**
   * Tests the {@link Hand#setCards(List)} method with a {@code null} list.
   * <p>
   * Scenario: Attempting to set a {@code null} list of ranks to the hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetCardsNullList() {
    hand.setCards(null);
  }

  /**
   * Tests the {@link Hand#setCards(List)} method with a list containing a
   * {@code null} rank.
   * <p>
   * Scenario: Setting a list of ranks that includes a {@code null} element.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetCardsListWithNullRank() {
    List<Card.Rank> ranksWithNull = Arrays.asList(Card.Rank.FOUR, null, Card.Rank.SEVEN);
    hand.setCards(ranksWithNull);
  }

  // ================================
  // Tests for getSize Method
  // ================================

  /**
   * Tests the {@link Hand#getSize()} method reflects the correct number of cards.
   * <p>
   * Scenario: Adding and removing cards from the hand and verifying the size
   * accordingly.
   * </p>
   * <p>
   * Expected Outcome: The method returns the accurate count of cards in the hand.
   * </p>
   */
  @Test
  public void testGetSize() {
    assertEquals("Initial hand size should be 0", 0, hand.getSize());

    hand.add(Card.Rank.THREE);
    assertEquals("Hand size should be 1 after adding a card", 1, hand.getSize());

    hand.add(Card.Rank.FIVE);
    hand.add(Card.Rank.SEVEN);
    assertEquals("Hand size should be 3 after adding three cards", 3, hand.getSize());

    hand.remove(Card.Rank.FIVE);
    assertEquals("Hand size should be 2 after removing a card", 2, hand.getSize());
  }

  // ================================
  // Tests for clone Method
  // ================================

  /**
   * Tests the {@link Hand#clone()} method creates an accurate deep copy of the
   * hand.
   * <p>
   * Scenario: Cloning a hand containing KING, QUEEN, and ACE.
   * </p>
   * <p>
   * Expected Outcome: The cloned hand is a separate instance with identical card
   * ranks, and modifications to the original hand do not affect the cloned hand.
   * </p>
   */
  @Test
  public void testClone() {
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.QUEEN);
    hand.add(Card.Rank.ACE);

    Hand clonedHand = hand.clone();
    assertNotNull("Cloned hand should not be null", clonedHand);
    assertEquals("Cloned hand should have the same size as the original", hand.getSize(), clonedHand.getSize());

    List<Card> originalCards = hand.getCards();
    List<Card> clonedCards = clonedHand.getCards();

    for (int i = 0; i < originalCards.size(); i++) {
      assertEquals("Cloned hand should have the same card ranks as the original",
          originalCards.get(i).getRank(), clonedCards.get(i).getRank());
    }

    hand.remove(Card.Rank.KING);
    assertEquals("Original hand size should decrease after removal", 2, hand.getSize());

    assertTrue("Cloned hand should still contain KING after original hand removal",
        clonedHand.contains(Card.Rank.KING));
  }

  /**
   * Tests the {@link Hand#clone()} method on an empty hand.
   * <p>
   * Scenario: Cloning an empty hand.
   * </p>
   * <p>
   * Expected Outcome: The cloned hand is a separate instance with a size of 0.
   * </p>
   */
  @Test
  public void testCloneEmptyHand() {
    Hand clonedHand = hand.clone();
    assertNotNull("Cloned hand should not be null", clonedHand);
    assertEquals("Cloned hand should have size 0", 0, clonedHand.getSize());
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests the {@link Hand#toString()} method returns the correct string
   * representation.
   * <p>
   * Scenario: Adding THREE, FIVE, and ACE to the hand.
   * </p>
   * <p>
   * Expected Outcome: The string representation lists all cards separated by
   * commas and includes the total value of the hand.
   * </p>
   */
  @Test
  public void testToString() {
    hand.add(Card.Rank.THREE);
    hand.add(Card.Rank.FIVE);
    hand.add(Card.Rank.ACE);
    String expected = "Three, Five, Ace - 19 Total";
    assertEquals("toString should return the correct string representation", expected, hand.toString());

    hand.add(Card.Rank.TEN);
    expected = "Three, Five, Ace, Ten - 19 Total";
    assertEquals("toString should adjust ACE value correctly", expected, hand.toString());
  }

  /**
   * Tests the {@link Hand#toString()} method on an empty hand.
   * <p>
   * Scenario: Evaluating the string representation of an empty hand.
   * </p>
   * <p>
   * Expected Outcome: The method returns a string indicating no cards and a total
   * of 0.
   * </p>
   */
  @Test
  public void testToStringEmptyHand() {
    String expected = " - 0 Total";
    assertEquals("toString should handle empty hand correctly", expected, hand.toString());
  }

  // ================================
  // Additional Edge Case Tests
  // ================================

  /**
   * Tests the {@link Hand#evaluateHand()} method with a large number of low
   * cards.
   * <p>
   * Scenario: Adding ten TWOs to the hand.
   * </p>
   * <p>
   * Expected Outcome: The hand evaluates to 20, and it is not a soft hand.
   * </p>
   */
  @Test
  public void testEvaluateHandWithManyLowCards() {
    for (int i = 0; i < 10; i++) {
      hand.add(Card.Rank.TWO);
    }
    assertEquals("Hand should evaluate to 20 with ten TWOs", 20, hand.evaluateHand());
    assertFalse("isSoftHand should return false as there are no Aces", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with multiple Aces and exact 21.
   * <p>
   * Scenario: Adding two ACEs and a NINE to the hand.
   * </p>
   * <p>
   * Expected Outcome: One ace is counted as 11 and the other as 1, resulting in
   * a total of 21, and the hand is soft.
   * </p>
   */
  @Test
  public void testEvaluateHandMultipleAcesExact21() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.NINE);

    assertEquals("Hand should evaluate to 21 with one Ace counted as 11", 21, hand.evaluateHand());
    assertTrue("isSoftHand should return true as one Ace is counted as 11", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with a combination of multiple
   * Aces and exact 21.
   * <p>
   * Scenario: Adding two ACEs and a TEN to the hand.
   * </p>
   * <p>
   * Expected Outcome: Both aces are counted as 1 to prevent busting, resulting in
   * a total of 12, and the hand is not soft.
   * </p>
   */
  @Test
  public void testEvaluateHandMultipleAcesExact21DifferentCombination() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.TEN);

    assertEquals("Hand should evaluate to 12 with two Aces counted as 1 to prevent busting", 12, hand.evaluateHand());
    assertFalse("isSoftHand should return false as both Aces are counted as 1", hand.isSoftHand());
  }

}
