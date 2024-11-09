package henrycaldwell.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import henrycaldwell.model.Card;
import henrycaldwell.model.Hand;

/**
 * Test suite for the {@link PredictionModel} class.
 * <p>
 * This class contains unit tests to verify the correctness of the
 * {@link PredictionModel} class, including its methods for calculating
 * expected values (EV) for various player actions such as standing,
 * hitting, doubling, splitting, and surrendering.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * PredictionModel predictionModel = new PredictionModel();
 * int[] valueCounts = { 4, 4, 4, 4, 4, 4, 4, 4, 4, 16 }; // Standard single deck
 * Hand playerHand = new Hand();
 * playerHand.add(Card.Rank.TEN);
 * playerHand.add(Card.Rank.SEVEN);
 * Hand dealerHand = new Hand();
 * dealerHand.add(Card.Rank.NINE);
 * double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);
 * }</pre>
 */
public class PredictionModelTest {

  private PredictionModel predictionModel;
  private int[] valueCounts;
  private Hand playerHand;
  private Hand dealerHand;

  /**
   * Setup method to initialize {@link PredictionModel}, {@link GameRules}, and
   * common {@link Hand}s before each test.
   */
  @Before
  public void setUp() {
    // Set up GameRules to known common values for testing
    GameRules.NUMBER_OF_DECKS = 1;
    GameRules.BLACKJACK_ODDS = 1.5;
    GameRules.SURRENDER = true;
    GameRules.DEALER_HITS_ON_SOFT_17 = true;
    GameRules.DEALER_PEAKS_FOR_21 = true;
    GameRules.DEALER_ALWAYS_PLAYS_OUT = false;
    GameRules.NATURAL_BLACKJACK_SPLITS = false;
    GameRules.DOUBLE_AFTER_SPLIT = true;
    GameRules.HIT_SPLIT_ACES = false;
    GameRules.DOUBLE_SPLIT_ACES = false;

    predictionModel = new PredictionModel();
    valueCounts = new int[10];

    // Initialize valueCounts to standard single deck counts
    // 4 of each card from Ace (index 0) to 9 (index 8), and 16 cards of value 10
    // (index 9)
    Arrays.fill(valueCounts, 4);
    valueCounts[9] = 16; // Tens, Jacks, Queens, Kings have a combined count of 16 in a single deck

    // Initialize playerHand and dealerHand as empty hands
    playerHand = new Hand();
    dealerHand = new Hand();
  }

  // ================================
  // Constructor Tests
  // ================================

  /**
   * Tests that {@link PredictionModel} initializes correctly.
   */
  @Test
  public void testDefaultConstructor() {
    assertTrue("PredictionModel should be instantiated", predictionModel != null);
  }

  // ================================
  // Tests for calculateStandEV Method
  // ================================

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} with null
   * arguments.
   * <p>
   * Scenario: All arguments are {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateStandEV_NullArguments() {
    predictionModel.calculateStandEV(null, null, null);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when value
   * counts is {@code null}.
   * <p>
   * Scenario: Value counts is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateStandEV_NullValueCountsHand() {
    predictionModel.calculateStandEV(null, playerHand, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when player
   * hand is {@code null}.
   * <p>
   * Scenario: Player hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateStandEV_NullPlayerHand() {
    predictionModel.calculateStandEV(valueCounts, null, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when dealer
   * hand is {@code null}.
   * <p>
   * Scenario: Dealer hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateStandEV_NullDealerHand() {
    predictionModel.calculateStandEV(valueCounts, playerHand, null);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} with a
   * player hand of 20 vs. dealer showing 9.
   * <p>
   * Scenario: Player stands on 20 vs. Dealer showing 9.
   * </p>
   */
  @Test
  public void testCalculateStandEV_Player20_Dealer9() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.QUEEN);
    valueCounts[9]--;
    valueCounts[9]--;

    dealerHand.add(Card.Rank.NINE);
    valueCounts[8]--;

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.74, standEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when player
   * busts.
   * <p>
   * Scenario: Player has 22 and stands vs. Dealer showing 5.
   * </p>
   */
  @Test
  public void testCalculateStandEV_PlayerBusts() {
    playerHand.add(Card.Rank.KING);
    playerHand.add(Card.Rank.QUEEN);
    playerHand.add(Card.Rank.TWO);

    dealerHand.add(Card.Rank.FIVE);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(-1.0, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when player
   * has a natural blackjack.
   * <p>
   * Scenario: Player has Blackjack vs. Dealer showing 5.
   * </p>
   */
  @Test
  public void testCalculateStandEV_PlayerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.FIVE);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(GameRules.BLACKJACK_ODDS, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when dealer
   * has a natural blackjack.
   * <p>
   * Scenario: Player has 20 vs. Dealer has Blackjack.
   * </p>
   */
  @Test
  public void testCalculateStandEV_DealerBlackjack() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.QUEEN);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(-1.0, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when both
   * player and dealer have natural blackjack.
   * <p>
   * Scenario: Both Player and Dealer have Blackjack.
   * </p>
   */
  @Test
  public void testCalculateStandEV_PlayerAndDealerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.0, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} when dealer
   * busts.
   * <p>
   * Scenario: Player has 18 vs. Dealer busts.
   * </p>
   */
  @Test
  public void testCalculateStandEV_DealerBusts() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.EIGHT);

    dealerHand.add(Card.Rank.SIX);
    dealerHand.add(Card.Rank.TEN);
    dealerHand.add(Card.Rank.QUEEN);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(1.00, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} with an
   * empty deck.
   * <p>
   * Scenario: Player stands with 18 vs. Dealer showing 7 with an empty deck.
   * </p>
   */
  @Test
  public void testCalculateStandEV_EmptyDeck() {
    Arrays.fill(valueCounts, 0);

    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.EIGHT);

    dealerHand.add(Card.Rank.SEVEN);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.0, standEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateStandEV(int[], Hand, Hand)} with soft
   * hands.
   * <p>
   * Scenario: Player has Ace and 6 (soft 17) vs. Dealer showing 2.
   * </p>
   */
  @Test
  public void testCalculateStandEV_SoftHand() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.SIX);

    dealerHand.add(Card.Rank.TWO);

    double standEV = predictionModel.calculateStandEV(valueCounts, playerHand, dealerHand);

    assertEquals(-0.13, standEV, 0.05);
  }

  // ================================
  // Tests for calculateHitEV Method
  // ================================

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} with null
   * arguments.
   * <p>
   * Scenario: All arguments are {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateHitEV_NullArguments() {
    predictionModel.calculateHitEV(null, null, null);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when value
   * counts is {@code null}.
   * <p>
   * Scenario: Value counts is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateHitEV_NullValueCountsHand() {
    predictionModel.calculateHitEV(null, playerHand, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when player
   * hand is {@code null}.
   * <p>
   * Scenario: Player hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateHitEV_NullPlayerHand() {
    predictionModel.calculateHitEV(valueCounts, null, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when dealer
   * hand is {@code null}.
   * <p>
   * Scenario: Dealer hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateHitEV_NullDealerHand() {
    predictionModel.calculateHitEV(valueCounts, playerHand, null);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} with a player
   * hand of 20 vs. dealer showing 9.
   * <p>
   * Scenario: Player hits on 20 vs. Dealer showing 9.
   * </p>
   */
  @Test
  public void testCalculateHitEV_Player20_Dealer9() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.QUEEN);
    valueCounts[9]--;
    valueCounts[9]--;

    dealerHand.add(Card.Rank.NINE);
    valueCounts[8]--;

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(-0.84, hitEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when player
   * busts.
   * <p>
   * Scenario: Player hits and busts with 22.
   * </p>
   */
  @Test
  public void testCalculateHitEV_PlayerBusts() {
    playerHand.add(Card.Rank.KING);
    playerHand.add(Card.Rank.QUEEN);
    playerHand.add(Card.Rank.TWO);

    dealerHand.add(Card.Rank.FIVE);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(-1.0, hitEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} with a
   * natural blackjack.
   * <p>
   * Scenario: Player has Blackjack and chooses to hit (though typically not
   * allowed).
   * </p>
   */
  @Test
  public void testCalculateHitEV_PlayerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.FIVE);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.33, hitEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when dealer
   * has a natural blackjack.
   * <p>
   * Scenario: Player has 20 vs. Dealer has Blackjack.
   * </p>
   */
  @Test
  public void testCalculateHitEV_DealerBlackjack() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.QUEEN);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(-1.0, hitEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when both
   * player and dealer have natural blackjack.
   * <p>
   * Scenario: Both Player and Dealer have Blackjack.
   * </p>
   */
  @Test
  public void testCalculateHitEV_PlayerAndDealerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(-1.0, hitEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} when dealer
   * busts.
   * <p>
   * Scenario: Player has 17 vs. Dealer busts.
   * </p>
   */
  @Test
  public void testCalculateHitEV_DealerBusts() {
    playerHand.add(Card.Rank.KING);
    playerHand.add(Card.Rank.SEVEN);

    dealerHand.add(Card.Rank.NINE);
    dealerHand.add(Card.Rank.TEN);
    dealerHand.add(Card.Rank.THREE);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(-0.39, hitEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} with an
   * empty deck.
   * <p>
   * Scenario: Player hits with 5 vs. Dealer showing 7 with an empty deck.
   * </p>
   */
  @Test
  public void testCalculateHitEV_EmptyDeck() {
    Arrays.fill(valueCounts, 0);

    playerHand.add(Card.Rank.TWO);
    playerHand.add(Card.Rank.THREE);

    dealerHand.add(Card.Rank.SEVEN);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.0, hitEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateHitEV(int[], Hand, Hand)} with soft
   * hands.
   * <p>
   * Scenario: Player has Ace and 6 (soft 17) vs. Dealer showing 2.
   * </p>
   */
  @Test
  public void testCalculateHitEV_SoftHand() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.SIX);

    dealerHand.add(Card.Rank.TWO);

    double hitEV = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.01, hitEV, 0.05);
  }

  // ================================
  // Tests for calculateDoubleEV Method
  // ================================

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} with null
   * arguments.
   * <p>
   * Scenario: All arguments are {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateDoubleEV_NullArguments() {
    predictionModel.calculateDoubleEV(null, null, null);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when value
   * counts is {@code null}.
   * <p>
   * Scenario: Value counts is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateDoubleEV_NullValueCountsHand() {
    predictionModel.calculateDoubleEV(null, playerHand, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when
   * player hand is {@code null}.
   * <p>
   * Scenario: Player hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateDoubleEV_NullPlayerHand() {
    predictionModel.calculateDoubleEV(valueCounts, null, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when
   * dealer hand is {@code null}.
   * <p>
   * Scenario: Dealer hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateDoubleEV_NullDealerHand() {
    predictionModel.calculateDoubleEV(valueCounts, playerHand, null);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} with a
   * player hand of 11 vs. dealer showing 6.
   * <p>
   * Scenario: Player doubles on 11 vs. Dealer showing 6.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_Player11_Dealer6() {
    playerHand.add(Card.Rank.FIVE);
    playerHand.add(Card.Rank.SIX);
    valueCounts[4]--;
    valueCounts[5]--;

    dealerHand.add(Card.Rank.SIX);
    valueCounts[5]--;

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.76, doubleEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when
   * player busts.
   * <p>
   * Scenario: Player doubles and busts with 22.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_PlayerBusts() {
    playerHand.add(Card.Rank.THREE);
    playerHand.add(Card.Rank.JACK);
    playerHand.add(Card.Rank.NINE);

    dealerHand.add(Card.Rank.TEN);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(-2.0, doubleEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} with a
   * natural blackjack.
   * <p>
   * Scenario: Player has Blackjack and chooses to double (though typically not
   * allowed).
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_PlayerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.TEN);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.18, doubleEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when
   * dealer has a natural blackjack.
   * <p>
   * Scenario: Player has 12 vs. Dealer has Blackjack.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_DealerBlackjack() {
    playerHand.add(Card.Rank.KING);
    playerHand.add(Card.Rank.TWO);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(-2.0, doubleEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when both
   * player and dealer have natural blackjack.
   * <p>
   * Scenario: Both Player and Dealer have Blackjack.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_PlayerAndDealerBlackjack() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.TEN);

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.TEN);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(-2.0, doubleEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} when
   * dealer busts.
   * <p>
   * Scenario: Player has 13 vs. Dealer busts.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_DealerBusts() {
    playerHand.add(Card.Rank.FOUR);
    playerHand.add(Card.Rank.NINE);

    dealerHand.add(Card.Rank.JACK);
    dealerHand.add(Card.Rank.QUEEN);
    dealerHand.add(Card.Rank.KING);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.45, doubleEV, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} with an
   * empty deck.
   * <p>
   * Scenario: Player doubles with 18 vs. Dealer showing 7 with an empty deck.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_EmptyDeck() {
    Arrays.fill(valueCounts, 0);

    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.THREE);

    dealerHand.add(Card.Rank.THREE);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.0, doubleEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateDoubleEV(int[], Hand, Hand)} with soft
   * hands.
   * <p>
   * Scenario: Player has Ace and 7 (soft 18) vs. Dealer showing 3.
   * </p>
   */
  @Test
  public void testCalculateDoubleEV_SoftHand() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.THREE);

    dealerHand.add(Card.Rank.TEN);

    double doubleEV = predictionModel.calculateDoubleEV(valueCounts, playerHand, dealerHand);

    assertEquals(-0.55, doubleEV, 0.05);
  }

  // ================================
  // Tests for calculateSplitEV Method
  // ================================

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} with null
   * arguments.
   * <p>
   * Scenario: All arguments are {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSplitEV_NullArguments() {
    predictionModel.calculateSplitEV(null, null, null);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} when value
   * counts is {@code null}.
   * <p>
   * Scenario: Value counts is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSplitEV_NullValueCountsHand() {
    predictionModel.calculateSplitEV(null, playerHand, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} when player
   * hand is {@code null}.
   * <p>
   * Scenario: Player hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSplitEV_NullPlayerHand() {
    predictionModel.calculateSplitEV(valueCounts, null, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} when dealer
   * hand is {@code null}.
   * <p>
   * Scenario: Dealer hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSplitEV_NullDealerHand() {
    predictionModel.calculateSplitEV(valueCounts, playerHand, null);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} with a
   * valid split scenario.
   * <p>
   * Scenario: Player has a pair of 8s vs. Dealer showing 9.
   * </p>
   */
  @Test
  public void testCalculateSplitEV_PairOf8s_Dealer9() {
    playerHand.add(Card.Rank.EIGHT);
    playerHand.add(Card.Rank.EIGHT);

    dealerHand.add(Card.Rank.NINE);

    double splitEV = predictionModel.calculateSplitEV(valueCounts, playerHand, dealerHand);

    assertEquals(splitEV, -0.41, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} with a pair
   * of Aces vs. Dealer showing 8.
   * <p>
   * Scenario: Player has a pair of Aces vs. Dealer showing 8.
   * </p>
   */
  @Test
  public void testCalculateSplitEV_PairOfAces_Dealer8() {
    playerHand.add(Card.Rank.ACE);
    playerHand.add(Card.Rank.ACE);

    dealerHand.add(Card.Rank.EIGHT);

    double splitEV = predictionModel.calculateSplitEV(valueCounts, playerHand, dealerHand);

    assertEquals(splitEV, 0.39, 0.05);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} when
   * splitting is not allowed.
   * <p>
   * Scenario: Player has non-pair cards vs. Dealer showing 5.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSplitEV_CannotSplit() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.SEVEN);

    dealerHand.add(Card.Rank.FIVE);

    @SuppressWarnings("unused")
    double splitEV = predictionModel.calculateSplitEV(valueCounts, playerHand, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateSplitEV(int[], Hand, Hand)} with an
   * empty deck.
   * <p>
   * Scenario: Player splits with 6 vs. Dealer showing 2 with an empty deck.
   * </p>
   */
  @Test
  public void testCalculateSplitEV_EmptyDeck() {
    Arrays.fill(valueCounts, 0);

    playerHand.add(Card.Rank.THREE);
    playerHand.add(Card.Rank.THREE);

    dealerHand.add(Card.Rank.TWO);

    double splitEV = predictionModel.calculateSplitEV(valueCounts, playerHand, dealerHand);

    assertEquals(0.0, splitEV, 0.0001);
  }

  // ================================
  // Tests for calculateSurrenderEV Method
  // ================================

  /**
   * Tests {@link PredictionModel#calculateSurrenderEV(Hand, Hand)} with
   * null arguments.
   * <p>
   * Scenario: All arguments are {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSurrenderEV_NullArguments() {
    predictionModel.calculateSurrenderEV(null, null);
  }

  /**
   * Tests {@link PredictionModel#calculateSurrenderEV(Hand, Hand)} when
   * player hand is {@code null}.
   * <p>
   * Scenario: Player hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSurrenderEV_NullPlayerHand() {
    predictionModel.calculateSurrenderEV(null, dealerHand);
  }

  /**
   * Tests {@link PredictionModel#calculateSurrenderEV(Hand, Hand)} when
   * dealer hand is {@code null}.
   * <p>
   * Scenario: Dealer hand is {@code null}.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCalculateSurrenderEV_NullDealerHand() {
    predictionModel.calculateSurrenderEV(playerHand, null);
  }

  /**
   * Tests {@link PredictionModel#calculateSurrenderEV(Hand, Hand)} in a scenario
   * where surrender is beneficial.
   * <p>
   * Scenario: Player has 16 vs. Dealer showing 10.
   * </p>
   */
  @Test
  public void testCalculateSurrenderEV_Player16_Dealer10() {
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.SIX);

    dealerHand.add(Card.Rank.TEN);

    double surrenderEV = predictionModel.calculateSurrenderEV(playerHand, dealerHand);

    assertEquals(-0.5, surrenderEV, 0.0001);
  }

  /**
   * Tests {@link PredictionModel#calculateSurrenderEV(Hand, Hand)} in a scenario
   * where both hands are empty.
   * <p>
   * Scenario: Empty player hand vs. Empty dealer hand.
   * </p>
   */
  @Test
  public void testCalculateSurrenderEV_EmptyHands() {
    double surrenderEV = predictionModel.calculateSurrenderEV(playerHand, dealerHand);

    assertEquals(-0.5, surrenderEV, 0.0001);
  }

  // ================================
  // Tests for memoization performance
  // ================================

  /**
   * Tests that the memoization cache improves performance for repeated EV
   * calculations.
   * <p>
   * Scenario: Player has two 6s vs. Dealer showing 2. The test measures the time
   * taken for the first and second calls to
   * {@link PredictionModel#calculateHitEV(int[], Hand, Hand)}
   * to ensure that the second call is faster due to memoization.
   * </p>
   */
  @Test
  public void testMemoizationPerformance() {
    playerHand.add(Card.Rank.SIX);
    playerHand.add(Card.Rank.SIX);

    dealerHand.add(Card.Rank.TWO);

    long startTime = System.currentTimeMillis();
    double firstCall = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);
    long firstDuration = System.currentTimeMillis() - startTime;

    startTime = System.currentTimeMillis();
    double secondCall = predictionModel.calculateHitEV(valueCounts, playerHand, dealerHand);
    long secondDuration = System.currentTimeMillis() - startTime;

    assertTrue(secondDuration <= firstDuration);
    assertEquals(firstCall, secondCall, 0.0001);
  }
}
