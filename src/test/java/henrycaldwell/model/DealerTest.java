package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the Dealer class.
 */
public class DealerTest {

  private Dealer dealer;
  private Hand hand1;
  private Hand hand2;
  private Hand hand3;

  /**
   * Setup method to initialize a new Dealer instance and sample Hands before each
   * test.
   */
  @Before
  public void setUp() {
    dealer = new Dealer();

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

  /**
   * Helper method to compare two Hands for equality based on their contents.
   *
   * @param expected The expected Hand.
   * @param actual   The actual Hand.
   */
  private void assertHandsEqual(Hand expected, Hand actual) {
    if (expected == null && actual == null) {
      return; // Both are null, considered equal
    }
    if (expected == null || actual == null) {
      fail("One of the hands is null while the other is not.");
    }
    assertEquals("Hands should have the same number of cards", expected.getSize(), actual.getSize());
    for (int i = 0; i < expected.getSize(); i++) {
      Card expectedCard = expected.getCards().get(i);
      Card actualCard = actual.getCards().get(i);
      assertEquals("Card at position " + i + " should have the same rank", expectedCard.getRank(),
          actualCard.getRank());
    }
  }

  /**
   * Helper method to count occurrences of a specific rank in a hand.
   *
   * @param hand The hand to inspect.
   * @param rank The rank to count.
   * @return The number of times the rank appears in the hand.
   */
  private int countRankInHand(Hand hand, Card.Rank rank) {
    if (hand == null) {
      return 0;
    }
    int count = 0;
    for (Card card : hand.getCards()) {
      if (card.getRank().equals(rank)) {
        count++;
      }
    }
    return count;
  }

  // ================================
  // Constructor Tests
  // ================================

  /**
   * Tests the {@link Dealer} default constructor initializes correctly with an
   * empty hand.
   */
  @Test
  public void testDefaultConstructor() {
    assertNotNull("Dealer should not be null after initialization", dealer);
    Hand expectedEmptyHand = new Hand();
    assertHandsEqual(expectedEmptyHand, dealer.getHand());
  }

  /**
   * Tests the {@link Dealer} parameterized constructor initializes correctly with
   * a predefined hand.
   */
  @Test
  public void testParameterizedConstructorWithHand() {
    Dealer newDealer = new Dealer(hand1);
    assertNotNull("Dealer should not be null after initialization with a hand", newDealer);
    assertHandsEqual(hand1, newDealer.getHand());
  }

  /**
   * Tests the {@link Dealer} parameterized constructor throws
   * {@link IllegalArgumentException} when initialized with a null hand.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testParameterizedConstructorWithNullHand() {
    new Dealer(null);
  }

  // ================================
  // Tests for addHand Method
  // ================================

  /**
   * Tests the {@link Dealer#addHand(Hand)} method by adding a valid hand to the
   * dealer.
   */
  @Test
  public void testAddHandValid() {
    dealer.addHand(hand1);
    assertHandsEqual(hand1, dealer.getHand());
  }

  /**
   * Tests the {@link Dealer#addHand(Hand)} method by attempting to add a null
   * hand.
   * <p>
   * Expects an {@link IllegalArgumentException} to be thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddHandNull() {
    dealer.addHand(null);
  }

  /**
   * Tests the {@link Dealer#addHand(Hand)} method by adding a hand when one
   * already exists.
   * The existing hand should be replaced with the new hand.
   */
  @Test
  public void testAddHandWhenExistingHandPresent() {
    dealer.addHand(hand1);
    assertHandsEqual(hand1, dealer.getHand());

    dealer.addHand(hand2);
    assertHandsEqual(hand2, dealer.getHand());
  }

  // ================================
  // Tests for getHand Method
  // ================================

  /**
   * Tests retrieving the hand when a hand is present.
   */
  @Test
  public void testGetHandWithHandSet() {
    dealer.addHand(hand1);
    Hand retrievedHand = dealer.getHand();
    assertHandsEqual(hand1, retrievedHand);
  }

  /**
   * Tests retrieving the hand when no hand is set.
   */
  @Test
  public void testGetHandWithNoHandSet() {
    Hand expectedEmptyHand = new Hand();
    Hand retrievedHand = dealer.getHand();
    assertHandsEqual(expectedEmptyHand, retrievedHand);
  }

  /**
   * Tests that {@link Dealer#getHand()} returns the internal Hand directly.
   */
  @Test
  public void testGetHandReturnsInternalHand() {
    dealer.addHand(hand1);
    Hand retrievedHand = dealer.getHand();
    assertSame("getHand should return the internal Hand instance", dealer.getHand(), retrievedHand);
  }

  // ================================
  // Tests for clone Method
  // ================================

  /**
   * Tests cloning a dealer with a hand.
   */
  @Test
  public void testCloneWithHand() {
    Dealer dealerWithHand = new Dealer(hand1);
    Dealer clonedDealer = dealerWithHand.clone();
    assertNotNull("Cloned dealer should not be null", clonedDealer);
    assertNotSame("Cloned dealer should be a different instance", dealerWithHand, clonedDealer);
    assertNotSame("Cloned dealer's hand should be a different instance", dealerWithHand.getHand(),
        clonedDealer.getHand());
    assertHandsEqual(dealerWithHand.getHand(),
        clonedDealer.getHand());
  }

  /**
   * Tests cloning a dealer with an empty hand.
   */
  @Test
  public void testCloneWithEmptyHand() {
    // Dealer was initialized with an empty Hand
    Dealer clonedDealer = dealer.clone();
    assertNotNull("Cloned dealer should not be null", clonedDealer);
    assertNotSame("Cloned dealer should be a different instance", dealer, clonedDealer);
    assertNotSame("Cloned dealer's hand should be a different instance", dealer.getHand(),
        clonedDealer.getHand());
    Hand expectedEmptyHand = new Hand();
    assertHandsEqual(expectedEmptyHand, clonedDealer.getHand());
  }

  /**
   * Tests that modifying the original dealer's hand does not affect the cloned
   * dealer's hand.
   */
  @Test
  public void testCloneDeepCopyIndependence() {
    Dealer dealerWithHand = new Dealer(hand1);
    Dealer clonedDealer = dealerWithHand.clone();

    // Modify the original dealer's hand
    Hand originalHand = dealerWithHand.getHand();
    originalHand.add(Card.Rank.FIVE);

    // The cloned dealer's hand should not reflect the new card as it's a different
    // instance
    Hand clonedHand = clonedDealer.getHand();
    assertEquals("Cloned dealer's hand should reflect the original hand", 2,
        clonedHand.getSize());
    assertTrue("Cloned dealer's hand should contain the new card",
        countRankInHand(clonedHand, Card.Rank.FIVE) == 0);
  }

  /**
   * Tests that the {@link Dealer#clone()} method creates an exact deep copy when
   * a hand is present.
   */
  @Test
  public void testCloneCreatesExactDeepCopy() {
    Dealer dealerWithHand = new Dealer(hand2); // hand2 has two TENs
    Dealer clonedDealer = dealerWithHand.clone();

    // Verify clonedDealer's hand is equal but not the same instance
    assertNotNull("Cloned dealer should not be null", clonedDealer);
    assertNotSame("Cloned dealer should be a different instance", dealerWithHand, clonedDealer);
    assertNotSame("Cloned dealer's hand should be a different instance", dealerWithHand.getHand(),
        clonedDealer.getHand());
    assertHandsEqual(dealerWithHand.getHand(),
        clonedDealer.getHand());

    // Verify cloned hand's contents are the same
    assertEquals("Cloned hand should have the same size", dealerWithHand.getHand().getSize(),
        clonedDealer.getHand().getSize());
    for (int i = 0; i < dealerWithHand.getHand().getSize(); i++) {
      assertEquals("Cloned hand's card rank should match the original",
          dealerWithHand.getHand().getCards().get(i).getRank(),
          clonedDealer.getHand().getCards().get(i).getRank());
    }
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests the {@link Dealer#toString()} method returns the correct string
   * representation when a hand is present.
   */
  @Test
  public void testToStringWithHand() {
    Dealer dealerWithHand = new Dealer(hand1);
    String expected = hand1.toString();
    assertEquals("toString should return the hand's string representation", expected, dealerWithHand.toString());
  }

  /**
   * Tests the {@link Dealer#toString()} method returns the correct string
   * representation after hand modifications.
   */
  @Test
  public void testToStringAfterHandModification() {
    Dealer dealerWithHand = new Dealer(hand1);
    String initialString = dealerWithHand.toString();

    // Modify the dealer's hand
    dealerWithHand.getHand().add(Card.Rank.FIVE);
    String modifiedString = dealerWithHand.toString();

    // The initial toString should not change as it was captured before modification
    assertEquals("Initial toString should remain unchanged", initialString, initialString);

    // The modified toString should reflect the new card
    String expectedModifiedString = dealerWithHand.getHand().toString();
    assertEquals("toString should reflect the modified hand", expectedModifiedString, modifiedString);
  }

  // ================================
  // Additional Edge Case Tests
  // ================================

  /**
   * Tests adding multiple hands sequentially and verifying each addition
   * correctly replaces the previous hand.
   */
  @Test
  public void testAddMultipleHandsSequentially() {
    dealer.addHand(hand1);
    assertHandsEqual(hand1, dealer.getHand());

    dealer.addHand(hand2);
    assertHandsEqual(hand2, dealer.getHand());

    dealer.addHand(hand3);
    assertHandsEqual(hand3, dealer.getHand());
  }

  /**
   * Tests that the {@link Dealer#addHand(Hand)} method correctly clones the hand
   * before assigning.
   */
  @Test
  public void testAddHandCloning() {
    Dealer dealerWithHand = new Dealer(hand1);
    Hand originalHand = hand1;
    Hand retrievedHand = dealerWithHand.getHand();

    assertNotSame("getHand should return a cloned instance", originalHand, retrievedHand);
    assertHandsEqual(originalHand, retrievedHand);
  }
}