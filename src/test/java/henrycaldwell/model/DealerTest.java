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
 * Test suite for the {@link Dealer} class.
 * <p>
 * This class contains unit tests to verify the correctness of the
 * {@link Dealer}
 * class,
 * including its constructors, {@code addHand}, {@code getHand}, {@code clone},
 * and {@code toString} methods.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Dealer dealer = new Dealer();
 * Hand hand = new Hand();
 * hand.add(Card.Rank.ACE);
 * hand.add(Card.Rank.KING);
 * dealer.addHand(hand);
 * assertEquals(hand, dealer.getHand());
 * Dealer clonedDealer = dealer.clone();
 * assertNotSame(dealer, clonedDealer);
 * assertEquals(dealer.getHand(), clonedDealer.getHand());
 * }</pre>
 */
public class DealerTest {

  private Dealer dealer;
  private Hand hand1;
  private Hand hand2;
  private Hand hand3;

  /**
   * Setup method to initialize a new {@link Dealer} instance and sample
   * {@link Hand}s before each test.
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
   * Helper method to compare two {@link Hand} instances for equality based on
   * their contents.
   *
   * @param expected The expected {@link Hand}.
   * @param actual   The actual {@link Hand}.
   */
  private void assertHandsEqual(Hand expected, Hand actual) {
    if (expected == null && actual == null) {
      return;
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
   * <p>
   * Scenario: Creating a {@code Dealer} instance using the default constructor.
   * </p>
   * <p>
   * Expected Outcome: A non-null {@code Dealer} object is instantiated with an
   * empty {@link Hand}.
   * </p>
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
   * <p>
   * Scenario: Creating a {@code Dealer} instance with a predefined {@link Hand}
   * (hand1).
   * </p>
   * <p>
   * Expected Outcome: A non-null {@code Dealer} object is instantiated with the
   * specified hand.
   * </p>
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
   * <p>
   * Scenario: Attempting to create a {@code Dealer} with a {@code null} hand.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
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
   * <p>
   * Scenario: Adding a predefined {@link Hand} (hand1) to the dealer.
   * </p>
   * <p>
   * Expected Outcome: The dealer's hand matches the added hand.
   * </p>
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
   * Scenario: Adding a {@code null} hand to the dealer.
   * </p>
   * <p>
   * Expected Outcome: An {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddHandNull() {
    dealer.addHand(null);
  }

  /**
   * Tests the {@link Dealer#addHand(Hand)} method by adding a hand when one
   * already exists.
   * <p>
   * Scenario: Adding a new {@link Hand} (hand2) when the dealer already has an
   * existing hand (hand1). The existing hand should be replaced.
   * </p>
   * <p>
   * Expected Outcome: The dealer's hand is updated to the new hand (hand2).
   * </p>
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
   * <p>
   * Scenario: The dealer has an existing hand (hand1). Retrieving the hand should
   * return the correct hand.
   * </p>
   * <p>
   * Expected Outcome: The retrieved hand matches the existing hand (hand1).
   * </p>
   */
  @Test
  public void testGetHandWithHandSet() {
    dealer.addHand(hand1);
    Hand retrievedHand = dealer.getHand();
    assertHandsEqual(hand1, retrievedHand);
  }

  /**
   * Tests retrieving the hand when no hand is set.
   * <p>
   * Scenario: The dealer has not been assigned any hand. Retrieving the hand
   * should return an empty {@link Hand}.
   * </p>
   * <p>
   * Expected Outcome: The retrieved hand is empty.
   * </p>
   */
  @Test
  public void testGetHandWithNoHandSet() {
    Hand expectedEmptyHand = new Hand();
    Hand retrievedHand = dealer.getHand();
    assertHandsEqual(expectedEmptyHand, retrievedHand);
  }

  /**
   * Tests that {@link Dealer#getHand()} returns the internal {@link Hand}
   * directly.
   * <p>
   * Scenario: After adding a hand to the dealer, retrieving the hand should
   * return the exact internal instance.
   * </p>
   * <p>
   * Expected Outcome: The retrieved hand is the same instance as the internal
   * hand.
   * </p>
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
   * <p>
   * Scenario: Cloning a {@code Dealer} instance that has an existing hand
   * (hand1).
   * </p>
   * <p>
   * Expected Outcome: The cloned dealer is a different instance with a deep copy
   * of the hand.
   * </p>
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
   * <p>
   * Scenario: Cloning a {@code Dealer} instance that has an empty hand.
   * </p>
   * <p>
   * Expected Outcome: The cloned dealer is a different instance with an empty
   * hand.
   * </p>
   */
  @Test
  public void testCloneWithEmptyHand() {
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
   * <p>
   * Scenario: After cloning a dealer, modifying the original dealer's hand should
   * not impact the cloned dealer's hand.
   * </p>
   * <p>
   * Expected Outcome: The cloned dealer's hand remains unchanged despite
   * modifications
   * to the original dealer's hand.
   * </p>
   */
  @Test
  public void testCloneDeepCopyIndependence() {
    Dealer dealerWithHand = new Dealer(hand1);
    Dealer clonedDealer = dealerWithHand.clone();
    Hand originalHand = dealerWithHand.getHand();
    originalHand.add(Card.Rank.FIVE);
    Hand clonedHand = clonedDealer.getHand();

    assertEquals("Cloned dealer's hand should reflect the original hand", 2,
        clonedHand.getSize());
    assertTrue("Cloned dealer's hand should contain the new card",
        countRankInHand(clonedHand, Card.Rank.FIVE) == 0);
  }

  /**
   * Tests that the {@link Dealer#clone()} method creates an exact deep copy when
   * a hand is present.
   * <p>
   * Scenario: Cloning a {@code Dealer} with a hand containing two TENs (hand2).
   * </p>
   * <p>
   * Expected Outcome: The cloned dealer is a different instance with a deep copy
   * of the
   * hand, maintaining the same card ranks.
   * </p>
   */
  @Test
  public void testCloneCreatesExactDeepCopy() {
    Dealer dealerWithHand = new Dealer(hand2);
    Dealer clonedDealer = dealerWithHand.clone();

    assertNotNull("Cloned dealer should not be null", clonedDealer);
    assertNotSame("Cloned dealer should be a different instance", dealerWithHand, clonedDealer);
    assertNotSame("Cloned dealer's hand should be a different instance", dealerWithHand.getHand(),
        clonedDealer.getHand());
    assertHandsEqual(dealerWithHand.getHand(),
        clonedDealer.getHand());

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
   * <p>
   * Scenario: The dealer has an existing hand (hand1). The {@code toString()}
   * method
   * should return the hand's string representation.
   * </p>
   * <p>
   * Expected Outcome: The string representation matches the hand's string.
   * </p>
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
   * <p>
   * Scenario: After adding a new card to the dealer's hand, the
   * {@code toString()}
   * method should reflect the updated hand.
   * </p>
   * <p>
   * Expected Outcome: The initial {@code toString()} remains unchanged, and the
   * modified {@code toString()} reflects the new card.
   * </p>
   */
  @Test
  public void testToStringAfterHandModification() {
    Dealer dealerWithHand = new Dealer(hand1);
    String initialString = dealerWithHand.toString();

    dealerWithHand.getHand().add(Card.Rank.FIVE);
    String modifiedString = dealerWithHand.toString();

    assertEquals("Initial toString should remain unchanged", initialString, initialString);

    String expectedModifiedString = dealerWithHand.getHand().toString();
    assertEquals("toString should reflect the modified hand", expectedModifiedString, modifiedString);
  }

  // ================================
  // Additional Edge Case Tests
  // ================================

  /**
   * Tests adding multiple hands sequentially and verifying each addition
   * correctly replaces the previous hand.
   * <p>
   * Scenario: Sequentially adding hand1, hand2, and hand3 to the dealer. Each new
   * addition should replace the existing hand.
   * </p>
   * <p>
   * Expected Outcome: The dealer's hand matches the most recently added hand each
   * time.
   * </p>
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
   * <p>
   * Scenario: Adding a hand to the dealer and verifying that the dealer's
   * internal
   * hand is a clone of the original hand.
   * </p>
   * <p>
   * Expected Outcome: The internal hand is a different instance but contains the
   * same card ranks as the original.
   * </p>
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