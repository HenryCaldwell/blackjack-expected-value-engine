package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test suite for the {@link Card} class.
 * <p>
 * This class contains unit tests to verify the correctness of the {@link Card}
 * class,
 * including its constructor, {@code equals}, {@code hashCode}, and
 * {@code toString} methods.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Card card = new Card(Card.Rank.ACE);
 * assertEquals(Card.Rank.ACE, card.getRank());
 * }</pre>
 */
public class CardTest {

  // ================================
  // Tests for Constructor
  // ================================

  /**
   * Tests the {@link Card} constructor with a valid {@link Card.Rank}.
   * <p>
   * Scenario: Creating a {@code Card} with a valid rank {@link Card.Rank#ACE}.
   * </p>
   * <p>
   * Expected Outcome: A non-null {@code Card} object is instantiated with the
   * specified rank.
   * </p>
   */
  @Test
  public void testConstructorValid() {
    Card card = new Card(Card.Rank.ACE);
    assertNotNull("Card should not be null", card);
    assertEquals("Rank should be ACE", Card.Rank.ACE, card.getRank());
  }

  /**
   * Tests the {@link Card} constructor with a {@code null} {@link Card.Rank}.
   * <p>
   * Scenario: Attempting to create a {@code Card} with a {@code null} rank.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullRank() {
    new Card(null);
  }

  // ================================
  // Tests for getRank Method
  // ================================

  /**
   * Tests the {@link Card#getRank()} method.
   * <p>
   * Scenario: Retrieving the rank of a {@code Card} instantiated with
   * {@link Card.Rank#KING}.
   * </p>
   * <p>
   * Expected Outcome: The correct rank {@link Card.Rank#KING} is returned.
   * </p>
   */
  @Test
  public void testGetRank() {
    Card card = new Card(Card.Rank.KING);
    assertEquals("Rank should be KING", Card.Rank.KING, card.getRank());
  }

  // ================================
  // Tests for equals Method
  // ================================

  /**
   * Tests the {@link Card#equals(Object)} method for reflexivity.
   * <p>
   * Scenario: A {@code Card} instance should be equal to itself.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code true}.
   * </p>
   */
  @Test
  public void testEqualsReflexive() {
    Card card = new Card(Card.Rank.QUEEN);
    assertTrue("Card should be equal to itself", card.equals(card));
  }

  /**
   * Tests the {@link Card#equals(Object)} method for symmetry.
   * <p>
   * Scenario: Two {@code Card} instances with the same rank should be equal to
   * each other.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code true} in both directions.
   * </p>
   */
  @Test
  public void testEqualsSymmetric() {
    Card card1 = new Card(Card.Rank.JACK);
    Card card2 = new Card(Card.Rank.JACK);
    assertTrue("Both cards should be equal", card1.equals(card2));
    assertTrue("Both cards should be equal", card2.equals(card1));
  }

  /**
   * Tests the {@link Card#equals(Object)} method for transitivity.
   * <p>
   * Scenario: If {@code card1} equals {@code card2} and {@code card2} equals
   * {@code card3}, then {@code card1} should equal {@code card3}.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code true} for all comparisons.
   * </p>
   */
  @Test
  public void testEqualsTransitive() {
    Card card1 = new Card(Card.Rank.TEN);
    Card card2 = new Card(Card.Rank.TEN);
    Card card3 = new Card(Card.Rank.TEN);
    assertTrue("card1 should equal card2", card1.equals(card2));
    assertTrue("card2 should equal card3", card2.equals(card3));
    assertTrue("card1 should equal card3", card1.equals(card3));
  }

  /**
   * Tests the {@link Card#equals(Object)} method with different ranks.
   * <p>
   * Scenario: Two {@code Card} instances with different ranks should not be
   * equal.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code false}.
   * </p>
   */
  @Test
  public void testEqualsDifferentRanks() {
    Card card1 = new Card(Card.Rank.EIGHT);
    Card card2 = new Card(Card.Rank.SEVEN);
    assertFalse("Cards with different ranks should not be equal", card1.equals(card2));
  }

  /**
   * Tests the {@link Card#equals(Object)} method when comparing with
   * {@code null}.
   * <p>
   * Scenario: A {@code Card} instance should not be equal to {@code null}.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code false}.
   * </p>
   */
  @Test
  public void testEqualsNull() {
    Card card = new Card(Card.Rank.SIX);
    assertFalse("Card should not be equal to null", card.equals(null));
  }

  /**
   * Tests the {@link Card#equals(Object)} method with an object of a different
   * type.
   * <p>
   * Scenario: A {@code Card} instance should not be equal to an object of a
   * different class.
   * </p>
   * <p>
   * Expected Outcome: {@code equals} returns {@code false}.
   * </p>
   */
  @Test
  public void testEqualsDifferentObjectType() {
    Card card = new Card(Card.Rank.FIVE);
    String notACard = "Not a Card";
    assertFalse("Card should not be equal to an object of different type", card.equals(notACard));
  }

  // ================================
  // Tests for hashCode Method
  // ================================

  /**
   * Tests the {@link Card#hashCode()} method for consistency with
   * {@link Card#equals(Object)}.
   * <p>
   * Scenario: Two equal {@code Card} instances should have the same hash code.
   * </p>
   * <p>
   * Expected Outcome: {@code hashCode} values are identical.
   * </p>
   */
  @Test
  public void testHashCodeConsistentWithEquals() {
    Card card1 = new Card(Card.Rank.FOUR);
    Card card2 = new Card(Card.Rank.FOUR);
    assertEquals("Equal cards should have the same hash code", card1.hashCode(), card2.hashCode());
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests the {@link Card#toString()} method.
   * <p>
   * Scenario: The {@code toString()} method should return the name of the card's
   * rank.
   * </p>
   * <p>
   * Expected Outcome: The correct string representation of the card's rank is
   * returned.
   * </p>
   */
  @Test
  public void testToString() {
    Card card = new Card(Card.Rank.TWO);
    assertEquals("toString should return the rank's name", "Two", card.toString());
  }

}
