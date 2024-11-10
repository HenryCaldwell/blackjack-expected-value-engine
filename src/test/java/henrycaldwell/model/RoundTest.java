package henrycaldwell.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the {@link Round} class.
 * <p>
 * This class contains unit tests to verify the correctness of the {@link Round}
 * class, including its constructors, {@code getPlayers}, {@code getDealer},
 * {@code getNumPlayers}, {@code allHandsResolved}, and {@code toString}
 * methods.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Round round = new Round(2);
 * List<Player> players = round.getPlayers();
 * Dealer dealer = round.getDealer();
 * assertEquals(2, round.getNumPlayers());
 * assertNotNull(dealer);
 * }</pre>
 */
public class RoundTest {

  private Round roundWithNoPlayers;
  private Round roundWithMultiplePlayers;

  // ================================
  // Setup Method
  // ================================

  /**
   * Setup method to initialize {@link Round} instances and {@link Player}s before
   * each test.
   * <p>
   * Ensures that the rounds and players are initialized correctly before each
   * test
   * case.
   * </p>
   */
  @Before
  public void setUp() {
    roundWithNoPlayers = new Round(0);

    roundWithMultiplePlayers = new Round(3);
  }

  // ================================
  // Constructor Tests
  // ================================

  /**
   * Tests that initializing a {@link Round} with zero players results in an empty
   * player list.
   * <p>
   * Scenario: Creating a {@code Round} instance with zero players.
   * </p>
   * <p>
   * Expected Outcome: The round is not null, has zero players, and the dealer's
   * hand is initialized correctly.
   * </p>
   */
  @Test
  public void testConstructorWithZeroPlayers() {
    assertNotNull("Round should not be null", roundWithNoPlayers);
    assertEquals("Number of players should be zero", 0, roundWithNoPlayers.getNumPlayers());
    List<Player> players = roundWithNoPlayers.getPlayers();
    assertNotNull("Players list should not be null", players);
    assertTrue("Players list should be empty", players.isEmpty());

    Dealer dealer = roundWithNoPlayers.getDealer();
    assertNotNull("Dealer should not be null", dealer);
    Hand dealerHand = dealer.getHand();
    assertNotNull("Dealer's hand should not be null", dealerHand);
    assertEquals("Dealer's hand should be empty", 0, dealerHand.getSize());
  }

  /**
   * Tests that initializing a {@link Round} with multiple players results in the
   * correct number of players.
   * <p>
   * Scenario: Creating a {@code Round} instance with three players.
   * </p>
   * <p>
   * Expected Outcome: The round is not null, has three players, each player has
   * at least one empty hand, and the dealer's hand is initialized correctly.
   * </p>
   */
  @Test
  public void testConstructorWithMultiplePlayers() {
    assertNotNull("Round should not be null", roundWithMultiplePlayers);
    assertEquals("Number of players should be three", 3, roundWithMultiplePlayers.getNumPlayers());
    List<Player> players = roundWithMultiplePlayers.getPlayers();
    assertNotNull("Players list should not be null", players);
    assertEquals("Players list should contain three players", 3, players.size());

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      assertNotNull("Player should not be null", player);
      List<Hand> hands = player.getHands();
      assertNotNull("Player's hands should not be null", hands);
      assertFalse("Player should have at least one hand", hands.isEmpty());
      assertEquals("Player should have one hand", 1, hands.size());
      Hand hand = hands.get(0);
      assertNotNull("Player's hand should not be null", hand);
      assertEquals("Player's hand should be empty", 0, hand.getSize());
    }

    Dealer dealer = roundWithMultiplePlayers.getDealer();
    assertNotNull("Dealer should not be null", dealer);
    Hand dealerHand = dealer.getHand();
    assertNotNull("Dealer's hand should not be null", dealerHand);
    assertEquals("Dealer's hand should be empty", 0, dealerHand.getSize());
  }

  // ================================
  // Tests for getPlayers Method
  // ================================

  /**
   * Tests that {@link Round#getPlayers()} returns the correct list of players.
   * <p>
   * Scenario: Retrieving the list of players from a round with multiple players.
   * </p>
   * <p>
   * Expected Outcome: The returned list is not null and contains the correct
   * number of players.
   * </p>
   */
  @Test
  public void testGetPlayers() {
    List<Player> players = roundWithMultiplePlayers.getPlayers();
    assertNotNull("getPlayers() should not return null", players);
    assertEquals("There should be three players", 3, players.size());
  }

  // ================================
  // Tests for getDealer Method
  // ================================

  /**
   * Tests that {@link Round#getDealer()} returns the correct {@link Dealer}
   * instance.
   * <p>
   * Scenario: Retrieving the dealer from a round with multiple players.
   * </p>
   * <p>
   * Expected Outcome: The dealer is not null and has an empty hand initially.
   * </p>
   */
  @Test
  public void testGetDealer() {
    Dealer dealer = roundWithMultiplePlayers.getDealer();
    assertNotNull("getDealer() should not return null", dealer);
    // Verify that the dealer has an empty Hand initially
    Hand dealerHand = dealer.getHand();
    assertNotNull("Dealer's hand should not be null", dealerHand);
    assertEquals("Dealer's hand should be empty initially", 0, dealerHand.getSize());
  }

  // ================================
  // Tests for getNumPlayers Method
  // ================================

  /**
   * Tests that {@link Round#getNumPlayers()} returns the correct number of
   * players.
   * <p>
   * Scenario: Retrieving the number of players from a round with multiple
   * players.
   * </p>
   * <p>
   * Expected Outcome: The method returns the accurate count of players in the
   * round.
   * </p>
   */
  @Test
  public void testGetNumPlayers() {
    assertEquals("Number of players should be three", 3, roundWithMultiplePlayers.getNumPlayers());
  }

  // ================================
  // Tests for allHandsResolved Method
  // ================================

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code true} when all
   * player hands are resolved.
   * <p>
   * Scenario: All players have hands that are either Blackjack or Bust.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code true}.
   * </p>
   */
  @Test
  public void testAllHandsResolvedAllResolved() {
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);

    players.get(1).getHands().get(0).add(Card.Rank.TEN);
    players.get(1).getHands().get(0).add(Card.Rank.KING);
    players.get(1).getHands().get(0).add(Card.Rank.THREE);

    players.get(2).getHands().get(0).add(Card.Rank.JACK);
    players.get(2).getHands().get(0).add(Card.Rank.ACE);

    assertTrue("All hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code false} when at
   * least one player hand is not resolved.
   * <p>
   * Scenario: At least one player has an unresolved hand.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code false}.
   * </p>
   */
  @Test
  public void testAllHandsResolvedSomeUnresolved() {
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    players.get(0).getHands().get(0).add(Card.Rank.NINE);
    players.get(0).getHands().get(0).add(Card.Rank.NINE);

    players.get(1).getHands().get(0).add(Card.Rank.TEN);
    players.get(1).getHands().get(0).add(Card.Rank.KING);
    players.get(1).getHands().get(0).add(Card.Rank.THREE);

    players.get(2).getHands().get(0).add(Card.Rank.ACE);
    players.get(2).getHands().get(0).add(Card.Rank.KING);

    assertFalse("Not all hands should be resolved",
        roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code true} when players
   * have multiple hands and all are resolved.
   * <p>
   * Scenario: Players have multiple hands, and all are either Blackjack or Bust.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code true}.
   * </p>
   */
  @Test
  public void testAllHandsResolvedMultipleHandsPerPlayerAllResolved() {
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    players.get(0).addHand(new Hand());
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);
    players.get(0).getHands().get(1).add(Card.Rank.TEN);
    players.get(0).getHands().get(1).add(Card.Rank.TEN);
    players.get(0).getHands().get(1).add(Card.Rank.TWO);

    players.get(1).getHands().get(0).add(Card.Rank.QUEEN);
    players.get(1).getHands().get(0).add(Card.Rank.ACE);

    players.get(2).getHands().get(0).add(Card.Rank.TEN);
    players.get(2).getHands().get(0).add(Card.Rank.KING);
    players.get(2).getHands().get(0).add(Card.Rank.THREE);

    assertTrue("All hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code false} when at
   * least one player has at least one unresolved hand.
   * <p>
   * Scenario: Players have multiple hands, and at least one hand is unresolved.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code false}.
   * </p>
   */
  @Test
  public void testAllHandsResolvedMultipleHandsPerPlayerSomeUnresolved() {
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    players.get(0).addHand(new Hand());
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);
    players.get(0).getHands().get(1).add(Card.Rank.TWO);
    players.get(0).getHands().get(1).add(Card.Rank.THREE);

    players.get(1).getHands().get(0).add(Card.Rank.JACK);
    players.get(1).getHands().get(0).add(Card.Rank.ACE);

    players.get(2).getHands().get(0).add(Card.Rank.TEN);
    players.get(2).getHands().get(0).add(Card.Rank.KING);
    players.get(2).getHands().get(0).add(Card.Rank.THREE);

    assertFalse("Not all hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code true} when players
   * have no hands.
   * <p>
   * Scenario: All players have no hands.
   * </p>
   * <p>
   * Expected Outcome: The method returns {@code true} as there are no hands to
   * resolve.
   * </p>
   */
  @Test
  public void testAllHandsResolvedPlayersWithNoHands() {
    Round round = new Round(2);
    List<Player> players = round.getPlayers();

    Player player1 = players.get(0);
    player1.getHands().clear();

    Player player2 = players.get(1);
    player2.getHands().clear();

    assertTrue("All hands are resolved when players have no hands", round.allHandsResolved());
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests that {@link Round#toString()} provides an accurate representation of
   * the round's state.
   * <p>
   * Scenario: Round has two players with their respective hands and a dealer with
   * a hand.
   * </p>
   * <p>
   * Expected Outcome: The string representation lists all players and the dealer
   * with their hands correctly.
   * </p>
   */
  @Test
  public void testToString() {
    Round round = new Round(2);
    List<Player> players = round.getPlayers();
    Dealer dealer = round.getDealer();

    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);

    players.get(1).getHands().get(0).add(Card.Rank.NINE);
    players.get(1).getHands().get(0).add(Card.Rank.NINE);

    dealer.getHand().add(Card.Rank.JACK);
    dealer.getHand().add(Card.Rank.QUEEN);

    String expected = "Player 1:\n" + players.get(0).toString() + "\n" +
        "Player 2:\n" + players.get(1).toString() + "\n" +
        "Dealer:\nHand: " + dealer.toString();

    String actual = round.toString();

    assertEquals("toString() should accurately represent the round's state",
        expected, actual);
  }

  /**
   * Tests that {@link Round#toString()} accurately reflects an empty round (no
   * players).
   * <p>
   * Scenario: Round has zero players.
   * </p>
   * <p>
   * Expected Outcome: The string representation lists only the dealer with an
   * empty hand.
   * </p>
   */
  @Test
  public void testToStringEmptyRound() {
    Round emptyRound = new Round(0);
    String expected = "Dealer:\nHand: " + emptyRound.getDealer().toString();
    String actual = emptyRound.toString();
    assertEquals("toString() should accurately represent an empty round", expected, actual);
  }

  /**
   * Tests that {@link Round#toString()} accurately reflects players with multiple
   * hands.
   * <p>
   * Scenario: Players have multiple hands, and the dealer has a hand.
   * </p>
   * <p>
   * Expected Outcome: The string representation lists all hands for each player
   * and the dealer correctly.
   * </p>
   */
  @Test
  public void testToStringMultipleHandsPerPlayer() {
    Round round = new Round(2);
    List<Player> players = round.getPlayers();
    Dealer dealer = round.getDealer();

    Player player1 = players.get(0);
    player1.addHand(new Hand());

    Hand player1Hand1 = player1.getHands().get(0);
    Hand player1Hand2 = player1.getHands().get(1);

    player1Hand1.add(Card.Rank.ACE);
    player1Hand1.add(Card.Rank.KING);
    player1Hand2.add(Card.Rank.TEN);
    player1Hand2.add(Card.Rank.FIVE);

    Player player2 = players.get(1);
    Hand player2Hand = player2.getHands().get(0);
    player2Hand.add(Card.Rank.NINE);
    player2Hand.add(Card.Rank.NINE);

    dealer.getHand().add(Card.Rank.JACK);
    dealer.getHand().add(Card.Rank.QUEEN);

    String expected = "Player 1:\n" + player1.toString() + "\n" +
        "Player 2:\n" + player2.toString() + "\n" +
        "Dealer:\nHand: " + dealer.toString();

    String actual = round.toString();

    assertEquals("toString() should accurately represent the round's state with multiple hands per player",
        expected, actual);
  }

  // ================================
  // Additional Edge Case Tests
  // ================================

  /**
   * Tests that modifying a player's hand affects the {@link Round}'s state as
   * expected.
   * <p>
   * Scenario: Modifying a player's hand after initializing the round.
   * </p>
   * <p>
   * Expected Outcome: The round correctly reflects the changes in the player's
   * hand when evaluating if all hands are resolved.
   * </p>
   */
  @Test
  public void testPlayerHandModificationAffectsRound() {
    Round round = new Round(1);
    Player player = round.getPlayers().get(0);

    Hand playerHand = player.getHands().get(0);
    assertEquals("Player's hand should be empty initially", 0, playerHand.getSize());

    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.SEVEN);

    assertFalse("Player's hand is unresolved", round.allHandsResolved());

    playerHand.add(Card.Rank.THREE);

    assertFalse("Player's hand is still unresolved at 20", round.allHandsResolved());

    playerHand.add(Card.Rank.THREE);

    assertTrue("Player's hand is now resolved (Bust)", round.allHandsResolved());
  }

  /**
   * Tests that modifying the dealer's hand affects the {@link Round}'s state as
   * expected.
   * <p>
   * Scenario: Modifying the dealer's hand after initializing the round.
   * </p>
   * <p>
   * Expected Outcome: The round correctly reflects the changes in the dealer's
   * hand when evaluating if all hands are resolved.
   * </p>
   */
  @Test
  public void testDealerHandModificationAffectsRound() {
    Round round = new Round(1);
    Dealer dealer = round.getDealer();

    Hand dealerHand = dealer.getHand();
    assertEquals("Dealer's hand should be empty initially", 0, dealerHand.getSize());

    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.KING);

    assertTrue("All player hands are resolved (no players have unresolved hands)", round.allHandsResolved());

    dealerHand.add(Card.Rank.THREE);

    assertTrue("All player hands are resolved even after modifying dealer's hand", round.allHandsResolved());
  }

  /**
   * Tests that adding multiple hands to a player affects
   * {@link Round#allHandsResolved()} correctly.
   * <p>
   * Scenario: A player has multiple hands, some resolved and some unresolved.
   * </p>
   * <p>
   * Expected Outcome: The method accurately reflects whether all hands across all
   * players are resolved.
   * </p>
   */
  @Test
  public void testAllHandsResolvedPlayerWithMultipleHands() {
    Round round = new Round(1);
    Player player = round.getPlayers().get(0);

    Hand hand1 = player.getHands().get(0);
    assertEquals("Player should have one hand initially", 1, player.getHands().size());

    Hand hand2 = new Hand();
    player.addHand(hand2);
    assertEquals("Player should have two hands now", 2, player.getHands().size());

    hand1.add(Card.Rank.TEN);
    hand1.add(Card.Rank.KING);
    hand1.add(Card.Rank.THREE);

    hand2.add(Card.Rank.NINE);
    hand2.add(Card.Rank.NINE);

    assertFalse("One of the player's hands is unresolved", round.allHandsResolved());

    hand2.add(Card.Rank.TWO);

    assertFalse("Player's hands are still not all resolved at 20", round.allHandsResolved());

    hand2.add(Card.Rank.THREE);

    assertTrue("All of the player's hands are resolved", round.allHandsResolved());
  }

}
