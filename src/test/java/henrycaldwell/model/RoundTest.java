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
 * class,
 * including its constructors, {@code getPlayers}, {@code getDealer},
 * {@code getNumPlayers},
 * {@code allHandsResolved}, and {@code toString} methods.
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
   */
  @Before
  public void setUp() {
    // Initialize a Round with zero players
    roundWithNoPlayers = new Round(0);

    // Initialize a Round with three players
    roundWithMultiplePlayers = new Round(3);
  }

  // ================================
  // Constructor Tests
  // ================================

  /**
   * Tests that initializing a {@link Round} with zero players results in an empty
   * player list.
   */
  @Test
  public void testConstructorWithZeroPlayers() {
    assertNotNull("Round should not be null", roundWithNoPlayers);
    assertEquals("Number of players should be zero", 0, roundWithNoPlayers.getNumPlayers());
    List<Player> players = roundWithNoPlayers.getPlayers();
    assertNotNull("Players list should not be null", players);
    assertTrue("Players list should be empty", players.isEmpty());

    // Verify that the dealer is initialized correctly with an empty Hand
    Dealer dealer = roundWithNoPlayers.getDealer();
    assertNotNull("Dealer should not be null", dealer);
    Hand dealerHand = dealer.getHand();
    assertNotNull("Dealer's hand should not be null", dealerHand);
    assertEquals("Dealer's hand should be empty", 0, dealerHand.getSize());
  }

  /**
   * Tests that initializing a {@link Round} with multiple players results in the
   * correct number of players.
   */
  @Test
  public void testConstructorWithMultiplePlayers() {
    assertNotNull("Round should not be null", roundWithMultiplePlayers);
    assertEquals("Number of players should be three", 3, roundWithMultiplePlayers.getNumPlayers());
    List<Player> players = roundWithMultiplePlayers.getPlayers();
    assertNotNull("Players list should not be null", players);
    assertEquals("Players list should contain three players", 3, players.size());

    // Verify each player has at least one Hand
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      assertNotNull("Player should not be null", player);
      List<Hand> hands = player.getHands();
      assertNotNull("Player's hands should not be null", hands);
      assertFalse("Player should have at least one hand", hands.isEmpty());
      // Assuming players start with one empty hand
      assertEquals("Player should have one hand", 1, hands.size());
      Hand hand = hands.get(0);
      assertNotNull("Player's hand should not be null", hand);
      assertEquals("Player's hand should be empty", 0, hand.getSize());
    }

    // Verify that the dealer is initialized correctly with an empty Hand
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
   */
  @Test
  public void testAllHandsResolvedAllResolved() {
    // Set up all players' hands to be resolved (Blackjack or Bust)
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    // Player 1: Blackjack
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);

    // Player 2: Bust
    players.get(1).getHands().get(0).add(Card.Rank.TEN);
    players.get(1).getHands().get(0).add(Card.Rank.KING);
    players.get(1).getHands().get(0).add(Card.Rank.THREE);

    // Player 3: Blackjack
    players.get(2).getHands().get(0).add(Card.Rank.JACK);
    players.get(2).getHands().get(0).add(Card.Rank.ACE);

    // Now, verify that allHandsResolved() returns true
    assertTrue("All hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code false} when at
   * least one player hand is not resolved.
   */
  @Test
  public void testAllHandsResolvedSomeUnresolved() {
    // Set up some players' hands to be unresolved
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    // Player 1: Unresolved (e.g., score = 18)
    players.get(0).getHands().get(0).add(Card.Rank.NINE);
    players.get(0).getHands().get(0).add(Card.Rank.NINE);

    // Player 2: Bust
    players.get(1).getHands().get(0).add(Card.Rank.TEN);
    players.get(1).getHands().get(0).add(Card.Rank.KING);
    players.get(1).getHands().get(0).add(Card.Rank.THREE);

    // Player 3: Blackjack
    players.get(2).getHands().get(0).add(Card.Rank.ACE);
    players.get(2).getHands().get(0).add(Card.Rank.KING);

    // Now, verify that allHandsResolved() returns false
    assertFalse("Not all hands should be resolved",
        roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code true} when players
   * have multiple hands and all are resolved.
   */
  @Test
  public void testAllHandsResolvedMultipleHandsPerPlayerAllResolved() {
    // Set up players with multiple hands, all resolved
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    // Player 1: Two hands, both resolved
    players.get(0).addHand(new Hand());
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);
    players.get(0).getHands().get(1).add(Card.Rank.TEN);
    players.get(0).getHands().get(1).add(Card.Rank.TEN);
    players.get(0).getHands().get(1).add(Card.Rank.TWO);

    // Player 2: One hand, resolved
    players.get(1).getHands().get(0).add(Card.Rank.QUEEN);
    players.get(1).getHands().get(0).add(Card.Rank.ACE);

    // Player 3: One hand, resolved (Bust)
    players.get(2).getHands().get(0).add(Card.Rank.TEN);
    players.get(2).getHands().get(0).add(Card.Rank.KING);
    players.get(2).getHands().get(0).add(Card.Rank.THREE);

    // Now, verify that allHandsResolved() returns true
    assertTrue("All hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code false} when at
   * least one player has at least one unresolved hand.
   */
  @Test
  public void testAllHandsResolvedMultipleHandsPerPlayerSomeUnresolved() {
    // Set up players with multiple hands, some unresolved
    List<Player> players = roundWithMultiplePlayers.getPlayers();

    // Player 1: Two hands, one resolved, one unresolved
    players.get(0).addHand(new Hand());
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);
    players.get(0).getHands().get(1).add(Card.Rank.TWO);
    players.get(0).getHands().get(1).add(Card.Rank.THREE);

    // Player 2: One hand, resolved
    players.get(1).getHands().get(0).add(Card.Rank.JACK);
    players.get(1).getHands().get(0).add(Card.Rank.ACE);

    // Player 3: One hand, resolved (Bust)
    players.get(2).getHands().get(0).add(Card.Rank.TEN);
    players.get(2).getHands().get(0).add(Card.Rank.KING);
    players.get(2).getHands().get(0).add(Card.Rank.THREE);

    // Now, verify that allHandsResolved() returns false
    assertFalse("Not all hands should be resolved", roundWithMultiplePlayers.allHandsResolved());
  }

  /**
   * Tests that {@link Round#allHandsResolved()} returns {@code true} when players
   * have no hands.
   */
  @Test
  public void testAllHandsResolvedPlayersWithNoHands() {
    // Initialize a Round with two players
    Round round = new Round(2);
    List<Player> players = round.getPlayers();

    // Remove all hands from Player 1
    Player player1 = players.get(0);
    player1.getHands().clear();

    // Remove all hands from Player 2
    Player player2 = players.get(1);
    player2.getHands().clear();

    // Now, allHandsResolved() should return true as there are no hands to resolve
    assertTrue("All hands are resolved when players have no hands", round.allHandsResolved());
  }

  // ================================
  // Tests for toString Method
  // ================================

  /**
   * Tests that {@link Round#toString()} provides an accurate representation of
   * the round's state.
   */
  @Test
  public void testToString() {
    // Initialize a Round with two players
    Round round = new Round(2);
    List<Player> players = round.getPlayers();
    Dealer dealer = round.getDealer();

    // Set up Player 1's Hand: Blackjack
    players.get(0).getHands().get(0).add(Card.Rank.ACE);
    players.get(0).getHands().get(0).add(Card.Rank.KING);

    // Set up Player 2's Hand: 18
    players.get(1).getHands().get(0).add(Card.Rank.NINE);
    players.get(1).getHands().get(0).add(Card.Rank.NINE);

    // Set up Dealer's Hand: 20
    dealer.getHand().add(Card.Rank.JACK);
    dealer.getHand().add(Card.Rank.QUEEN);

    // Expected string representation
    String expected = "Player 1:\n" + players.get(0).toString() + "\n" +
        "Player 2:\n" + players.get(1).toString() + "\n" +
        "Dealer:\nHand: " + dealer.toString();

    // Actual string representation
    String actual = round.toString();

    assertEquals("toString() should accurately represent the round's state",
        expected, actual);
  }

  /**
   * Tests that {@link Round#toString()} accurately reflects an empty round (no
   * players).
   */
  @Test
  public void testToStringEmptyRound() {
    // Initialize a Round with zero players
    Round emptyRound = new Round(0);
    String expected = "Dealer:\nHand: " + emptyRound.getDealer().toString();
    String actual = emptyRound.toString();
    assertEquals("toString() should accurately represent an empty round", expected, actual);
  }

  /**
   * Tests that {@link Round#toString()} accurately reflects players with multiple
   * hands.
   */
  @Test
  public void testToStringMultipleHandsPerPlayer() {
    // Initialize a Round with two players
    Round round = new Round(2);
    List<Player> players = round.getPlayers();
    Dealer dealer = round.getDealer();

    // Add a second hand to Player 1
    Player player1 = players.get(0);
    player1.addHand(new Hand());

    Hand player1Hand1 = player1.getHands().get(0);
    Hand player1Hand2 = player1.getHands().get(1);

    // Set up Player 1's hands
    player1Hand1.add(Card.Rank.ACE);
    player1Hand1.add(Card.Rank.KING); // 21 (Blackjack)
    player1Hand2.add(Card.Rank.TEN);
    player1Hand2.add(Card.Rank.FIVE); // 15

    // Set up Player 2's hand: 18
    Player player2 = players.get(1);
    Hand player2Hand = player2.getHands().get(0);
    player2Hand.add(Card.Rank.NINE);
    player2Hand.add(Card.Rank.NINE); // 18

    // Set up Dealer's Hand: 20
    dealer.getHand().add(Card.Rank.JACK);
    dealer.getHand().add(Card.Rank.QUEEN); // 20

    // Expected string representation
    String expected = "Player 1:\n" + player1.toString() + "\n" +
        "Player 2:\n" + player2.toString() + "\n" +
        "Dealer:\nHand: " + dealer.toString();

    // Actual string representation
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
   */
  @Test
  public void testPlayerHandModificationAffectsRound() {
    // Initialize a Round with one player
    Round round = new Round(1);
    Player player = round.getPlayers().get(0);

    // Initially, player's hand should be empty
    Hand playerHand = player.getHands().get(0);
    assertEquals("Player's hand should be empty initially", 0, playerHand.getSize());

    // Add cards to the player's hand
    playerHand.add(Card.Rank.TEN);
    playerHand.add(Card.Rank.SEVEN); // Total: 17

    // Verify that allHandsResolved() returns false
    assertFalse("Player's hand is unresolved", round.allHandsResolved());

    // Add another card to make it 20
    playerHand.add(Card.Rank.THREE); // Total: 20

    // Depending on how evaluateHand() interprets 20, it might still be unresolved
    // For this test, assume 20 is unresolved
    assertFalse("Player's hand is still unresolved at 20", round.allHandsResolved());

    // Add another card to make it 23 (Bust)
    playerHand.add(Card.Rank.THREE); // Total: 23

    // Assuming evaluateHand() returns 0 for Bust, now all hands should be resolved
    assertTrue("Player's hand is now resolved (Bust)", round.allHandsResolved());
  }

  /**
   * Tests that modifying the dealer's hand affects the {@link Round}'s state as
   * expected.
   */
  @Test
  public void testDealerHandModificationAffectsRound() {
    // Initialize a Round with one player
    Round round = new Round(1);
    Dealer dealer = round.getDealer();

    // Initially, dealer's hand should be empty
    Hand dealerHand = dealer.getHand();
    assertEquals("Dealer's hand should be empty initially", 0, dealerHand.getSize());

    // Add cards to the dealer's hand
    dealerHand.add(Card.Rank.ACE);
    dealerHand.add(Card.Rank.KING); // Total: 21

    // Verify that allHandsResolved() remains true since players have empty hands
    // However, depending on the game logic, empty player hands might be considered
    // resolved
    // In the provided Round class, allHandsResolved checks player hands, not
    // dealer's
    // Thus, it should still return true
    assertTrue("All player hands are resolved (no players have unresolved hands)", round.allHandsResolved());

    // Modify the dealer's hand further
    dealerHand.add(Card.Rank.THREE); // Total: 24 (Bust)

    // Still, allHandsResolved() should return true as it only checks player hands
    assertTrue("All player hands are resolved even after modifying dealer's hand", round.allHandsResolved());
  }

  /**
   * Tests that adding multiple hands to a player affects
   * {@link Round#allHandsResolved()} correctly.
   */
  @Test
  public void testAllHandsResolvedPlayerWithMultipleHands() {
    // Initialize a Round with one player
    Round round = new Round(1);
    Player player = round.getPlayers().get(0);

    // Player starts with one empty hand
    Hand hand1 = player.getHands().get(0);
    assertEquals("Player should have one hand initially", 1, player.getHands().size());

    // Add a second hand
    Hand hand2 = new Hand();
    player.addHand(hand2);
    assertEquals("Player should have two hands now", 2, player.getHands().size());

    // Set up hand1 as resolved (Bust)
    hand1.add(Card.Rank.TEN);
    hand1.add(Card.Rank.KING);
    hand1.add(Card.Rank.THREE); // Total: 23 (Bust)

    // Set up hand2 as unresolved (score = 18)
    hand2.add(Card.Rank.NINE);
    hand2.add(Card.Rank.NINE); // Total: 18

    // Verify that allHandsResolved() returns false
    assertFalse("One of the player's hands is unresolved", round.allHandsResolved());

    // Resolve hand2 by adding a card to make it 21
    hand2.add(Card.Rank.TWO); // Total: 20

    // Depending on evaluateHand(), assume 20 is unresolved
    assertFalse("Player's hands are still not all resolved at 20", round.allHandsResolved());

    // Resolve hand2 by adding another card to make it 23 (Bust)
    hand2.add(Card.Rank.THREE); // Total: 23 (Bust)

    // Now, both hands are resolved (both Bust)
    assertTrue("All of the player's hands are resolved", round.allHandsResolved());
  }

}
