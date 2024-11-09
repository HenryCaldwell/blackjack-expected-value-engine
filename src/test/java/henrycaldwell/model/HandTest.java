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
 * class,
 * including its constructor, {@code add}, {@code remove}, {@code contains},
 * {@code evaluateHand}, {@code isSoftHand}, {@code getCards}, {@code setCards},
 * {@code getSize}, {@code clone}, and {@code toString} methods.
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

  private Hand hand; // The Hand instance used in tests

  /**
   * Setup method to initialize a new {@link Hand} instance before each test.
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
   */
  @Test
  public void testAddValidRank() {
    hand.add(Card.Rank.JACK);
    assertEquals("Hand size should increase by 1 after adding a card", 1, hand.getSize());
    assertTrue("Hand should contain the added rank", hand.contains(Card.Rank.JACK));
  }

  /**
   * Tests the {@link Hand#add(Card.Rank)} method with multiple valid ranks.
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
   * Expects an {@link IllegalArgumentException} to be thrown.
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
   */
  @Test
  public void testRemoveExistingRank() {
    hand.add(Card.Rank.KING);
    hand.add(Card.Rank.QUEEN);
    hand.add(Card.Rank.KING);
    assertTrue("Hand should contain KING before removal", hand.contains(Card.Rank.KING));

    hand.remove(Card.Rank.KING);
    assertEquals("Hand size should decrease by 1 after removing a KING", 2, hand.getSize());

    // Ensure one KING is still present
    int kingCount = countRankInHand(Card.Rank.KING);
    assertEquals("Hand should contain one KING after removal", 1, kingCount);
  }

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method removes all instances of a
   * duplicated rank.
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
   * Expects an {@link IllegalStateException} to be thrown.
   * </p>
   */
  @Test(expected = IllegalStateException.class)
  public void testRemoveNonExistingRank() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.FIVE);
    hand.remove(Card.Rank.KING); // KING is not in hand, should throw exception
  }

  /**
   * Tests the {@link Hand#remove(Card.Rank)} method with a {@code null} rank.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown.
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
   */
  @Test
  public void testContainsExistingRank() {
    hand.add(Card.Rank.FIVE);
    assertTrue("contains should return true for a rank present in the hand", hand.contains(Card.Rank.FIVE));
  }

  /**
   * Tests the {@link Hand#contains(Card.Rank)} method for a rank that does not
   * exist in the hand.
   */
  @Test
  public void testContainsNonExistingRank() {
    hand.add(Card.Rank.SEVEN);
    assertFalse("contains should return false for a rank not present in the hand", hand.contains(Card.Rank.TWO));
  }

  /**
   * Tests the {@link Hand#contains(Card.Rank)} method with a {@code null} rank.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown.
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
   */
  @Test
  public void testEvaluateHandEmpty() {
    assertEquals("evaluateHand should return 0 for an empty hand", 0, hand.evaluateHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with multiple cards, including
   * an ace counted as 11.
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
   */
  @Test
  public void testEvaluateHandComplexCombination() {
    hand.add(Card.Rank.ACE); // 1 or 11
    hand.add(Card.Rank.ACE); // 1 or 11
    hand.add(Card.Rank.SEVEN); // 7
    hand.add(Card.Rank.THREE); // 3
    // Total: 1+1+7+3 = 12 or 11+1+7+3 = 22 or 1+11+7+3 = 22
    // Should count both Aces as 1 to avoid busting: Total = 12
    assertEquals("Hand should evaluate to 12 with multiple Aces avoiding busting", 12, hand.evaluateHand());
    assertFalse("isSoftHand should return false as Aces are counted as 1", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with maximum possible hand
   * without busting.
   */
  @Test
  public void testEvaluateHandMaxWithoutBusting() {
    hand.add(Card.Rank.ACE); // 11
    hand.add(Card.Rank.EIGHT); // 8
    hand.add(Card.Rank.TWO); // 2
    // Total: 11 + 8 + 2 = 21
    assertEquals("Hand should evaluate to 21", 21, hand.evaluateHand());
    assertTrue("isSoftHand should return true as Ace is counted as 11", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with busting condition.
   */
  @Test
  public void testEvaluateHandBust() {
    hand.add(Card.Rank.KING); // 10
    hand.add(Card.Rank.QUEEN); // 10
    hand.add(Card.Rank.JACK); // 10
    // Total: 30
    assertEquals("Hand should evaluate to 30", 30, hand.evaluateHand());
    assertFalse("isSoftHand should return false when busting", hand.isSoftHand());
  }

  // ================================
  // Tests for isSoftHand Method
  // ================================

  /**
   * Tests the {@link Hand#isSoftHand()} method for a soft hand.
   */
  @Test
  public void testIsSoftHandTrue() {
    hand.add(Card.Rank.ACE);
    hand.add(Card.Rank.SEVEN);
    assertTrue("isSoftHand should return true for a soft hand", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#isSoftHand()} method for a hard hand.
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

    // Modify the retrieved list and ensure the original hand is unaffected
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
   * Expects an {@link IllegalArgumentException} to be thrown.
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
   * Expects an {@link IllegalArgumentException} to be thrown.
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

    // Modify the original hand and ensure the clone does not change
    hand.remove(Card.Rank.KING);
    assertEquals("Original hand size should decrease after removal", 2, hand.getSize());

    // Ensure the cloned hand still contains KING
    assertTrue("Cloned hand should still contain KING after original hand removal",
        clonedHand.contains(Card.Rank.KING));
  }

  /**
   * Tests the {@link Hand#clone()} method on an empty hand.
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
   */
  @Test
  public void testToString() {
    hand.add(Card.Rank.THREE);
    hand.add(Card.Rank.FIVE);
    hand.add(Card.Rank.ACE);
    String expected = "Three, Five, Ace - 19 Total"; // ACE counted as 11
    assertEquals("toString should return the correct string representation", expected, hand.toString());

    // Adding a card that causes ACE to be counted as 1
    hand.add(Card.Rank.TEN);
    expected = "Three, Five, Ace, Ten - 19 Total"; // ACE counted as 1 to prevent busting
    assertEquals("toString should adjust ACE value correctly", expected, hand.toString());
  }

  /**
   * Tests the {@link Hand#toString()} method on an empty hand.
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
   */
  @Test
  public void testEvaluateHandWithManyLowCards() {
    for (int i = 0; i < 10; i++) {
      hand.add(Card.Rank.TWO); // 2 * 10 = 20
    }
    assertEquals("Hand should evaluate to 20 with ten TWOs", 20, hand.evaluateHand());
    assertFalse("isSoftHand should return false as there are no Aces", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with multiple Aces and exact 21.
   */
  @Test
  public void testEvaluateHandMultipleAcesExact21() {
    hand.add(Card.Rank.ACE); // 11
    hand.add(Card.Rank.ACE); // 11 or 1
    hand.add(Card.Rank.NINE); // 9
    // Total: 11 + 1 + 9 = 21
    assertEquals("Hand should evaluate to 21 with one Ace counted as 11", 21, hand.evaluateHand());
    assertTrue("isSoftHand should return true as one Ace is counted as 11", hand.isSoftHand());
  }

  /**
   * Tests the {@link Hand#evaluateHand()} method with a combination of multiple
   * Aces and exact 21.
   */
  @Test
  public void testEvaluateHandMultipleAcesExact21DifferentCombination() {
    hand.add(Card.Rank.ACE); // 11
    hand.add(Card.Rank.ACE); // 1
    hand.add(Card.Rank.TEN); // 10
    // Total: 11 + 1 + 10 = 22 -> Aces adjust to 1 each
    assertEquals("Hand should evaluate to 12 with two Aces counted as 1 to prevent busting", 12, hand.evaluateHand());
    assertFalse("isSoftHand should return false as both Aces are counted as 1", hand.isSoftHand());
  }

}
