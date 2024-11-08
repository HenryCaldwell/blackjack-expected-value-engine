package henrycaldwell.controller;

import java.util.ArrayList;

import henrycaldwell.model.Card;
import henrycaldwell.model.Dealer;
import henrycaldwell.model.Deck;
import henrycaldwell.model.Hand;
import henrycaldwell.model.Player;
import henrycaldwell.model.Round;
import henrycaldwell.view.ConsoleUtil;
import henrycaldwell.view.ConsoleView;

/**
 * Represents and manages the core gameplay logic and state for Blackjack.
 * <p>
 * The {@code GameEngine} class handles the initiation and progression of the
 * game,
 * including setting up rounds, processing player and dealer actions, evaluating
 * outcomes,
 * and interacting with the user through the console.
 * </p>
 * <p>
 * It utilizes other components such as {@link Deck}, {@link Player},
 * {@link Dealer},
 * {@link Round}, and {@link PredictionModel} to facilitate game mechanics and
 * strategy
 * calculations.
 * </p>
 */
public class GameEngine {

  private Deck deck; // The deck used in the game.
  private PredictionModel pred; // The prediction model used for calculating expected return values.
  private ConsoleView consoleView; // The view for user interactions.

  /**
   * Constructs a {@code GameEngine} instance by initializing the deck, prediction
   * model,
   * and console view.
   */
  public GameEngine() {
    this.deck = new Deck();
    this.pred = new PredictionModel();
    this.consoleView = new ConsoleView();
  }

  /**
   * Starts the main game loop, continuously prompting players to play new rounds
   * until
   * the game is terminated by the user.
   * <p>
   * The method repeatedly:
   * <ul>
   * <li>Prompts the user for the number of players.</li>
   * <li>Checks if the deck needs to be reset based on its current size.</li>
   * <li>Initiates a new round with the specified number of players.</li>
   * </ul>
   * The loop exits when the user indicates they no longer wish to continue
   * playing.
   * </p>
   */
  public void startGame() {
    while (true) {
      int initialPlayers = consoleView.promptNumberOfPlayers();

      if (initialPlayers == 0) {
        break;
      }

      if (consoleView.promptReset(deck.getSize())) {
        deck.initializeDeck();
      }

      startRound(initialPlayers);
    }
  }

  /**
   * Manages the setup and gameplay for a single round.
   *
   * @param initialPlayers The number of players participating in the round.
   * @throws IllegalArgumentException if {@code initialPlayers} is negative.
   */
  private void startRound(int initialPlayers) {
    Round currRound = new Round(initialPlayers);
    setUpRound(currRound, false);
    processRound(currRound);
    evaluateRound(currRound);
  }

  /**
   * Configures the game setup for a new round, distributing initial cards and
   * handling initial game settings.
   *
   * @param currRound The current round instance being played.
   * @param isSplit   Indicates whether the setup is part of a split scenario.
   * @throws IllegalArgumentException if {@code currRound} is {@code null}.
   */
  private void setUpRound(Round currRound, boolean isSplit) {
    Dealer dealer = currRound.getDealer();

    for (int dealRound = 0; dealRound < 2; dealRound++) {
      for (int playerIndex = 0; playerIndex < currRound.getNumPlayers(); playerIndex++) {
        Player player = currRound.getPlayers().get(playerIndex);

        for (int handIndex = 0; handIndex < player.getNumHands(); handIndex++) {
          Hand hand = player.getHands().get(handIndex);

          if (hand.getSize() < 2) {
            consoleView.displayPlayerHand(playerIndex, handIndex, hand);
            Card card = consoleView.promptCard(false, deck);

            if (card != null) {
              hand.add(card.getRank());
              deck.remove(card.getRank());
            } else {
              consoleView.displayError("Failed to draw a card for the player.");
            }
          }
        }
      }

      if (!isSplit) {
        consoleView.displayDealerHand(dealer);
        String dealerMessage = dealRound == 0 ? "DEALER UP CARD" : "DEALER DOWN CARD ('?' IF UNKNOWN)";
        consoleView.displayMessage(dealerMessage);
        boolean isDownCard = dealRound == 1;
        Card dealerCard = consoleView.promptCard(isDownCard, deck);

        if (dealerCard != null) {
          dealer.getHand().add(dealerCard.getRank());
          deck.remove(dealerCard.getRank());
        } else {
          consoleView.displayError("Failed to draw a card for the dealer.");
        }
      }
    }

    if (!isSplit) {
      consoleView.displayRound(currRound);
    }
  }

  /**
   * Processes the actions of each player and the dealer during a round.
   * Each player decides on their moves until all hands are settled, followed by
   * the dealer's turn according to game rules.
   *
   * @param currRound The current round instance being played.
   */
  private void processRound(Round currRound) {
    Dealer dealer = currRound.getDealer();

    if (dealer.getHand().evaluateHand() == 21) {
      return;
    }

    for (int playerIndex = 0; playerIndex < currRound.getNumPlayers(); playerIndex++) {
      Player player = currRound.getPlayers().get(playerIndex);

      for (int handIndex = 0; handIndex < player.getNumHands(); handIndex++) {
        processPlayerTurn(currRound, playerIndex, handIndex);
        Hand hand = player.getHands().get(handIndex);
        consoleView.displayFinalPlayerHand(playerIndex, handIndex, hand);
      }
    }

    processDealerTurn(currRound);
  }

  /**
   * Processes a single player's turn, allowing them to make decisions such as
   * hit, stand, double, split, or surrender.
   *
   * @param currRound   The current round instance being played.
   * @param playerIndex The index of the player taking the turn.
   * @param handIndex   The index of the hand currently being played.
   * @throws IllegalArgumentException if {@code currRound} is {@code null}.
   */
  private void processPlayerTurn(Round currRound, int playerIndex, int handIndex) {
    Player player = currRound.getPlayers().get(playerIndex);
    Dealer dealer = currRound.getDealer();

    while (true) {
      Hand hand = player.getHands().get(handIndex);

      if (hand.evaluateHand() == 21 && hand.getSize() == 2) {
        return;
      }

      consoleView.displayPlayerHand(playerIndex, handIndex, hand);
      evaluatePredictions(hand, dealer.getHand());

      String action = consoleView.promptAction(deck.getCount());

      switch (action.toUpperCase()) {
        case "HIT":
          Card hitCard = consoleView.promptCard(false, deck);

          if (hitCard != null) {
            hand.add(hitCard.getRank());
            deck.remove(hitCard.getRank());

            if (hand.evaluateHand() >= 21) {
              return;
            }
          } else {
            consoleView.displayError("Failed to draw a card for HIT action.");
          }

          break;
        case "STAND":
          return;
        case "DOUBLE":
          if (hand.getSize() == 2) {
            Card doubleCard = consoleView.promptCard(false, deck);

            if (doubleCard != null) {
              hand.add(doubleCard.getRank());
              deck.remove(doubleCard.getRank());

              return;
            } else {
              consoleView.displayError("Failed to draw a card for DOUBLE action.");
            }
          } else {
            consoleView.displayError("DOUBLE UNAVAILABLE");
          }

          break;
        case "SPLIT":
          if (hand.canSplit()) {
            player.splitHand(handIndex);
            setUpRound(currRound, true);

            break;
          }

          consoleView.displayError("SPLIT UNAVAILABLE");

          break;
        case "SURRENDER":
          if (GameRules.SURRENDER) {
            hand.setCards(new ArrayList<>());
            return;
          }

          consoleView.displayError("SURRENDER UNAVAILABLE");

          break;
        default:
          consoleView.displayError("INVALID OPTION");

          continue;
      }
    }
  }

  /**
   * Processes the dealer's turn following all player actions.
   * The dealer will hit or stand according to the game rules, specifically if the
   * dealer hits on a soft 17.
   *
   * @param currRound The current round instance being played.
   */
  private void processDealerTurn(Round currRound) {
    Dealer dealer = currRound.getDealer();

    if (!currRound.allHandsResolved() || GameRules.DEALER_ALWAYS_PLAYS_OUT) {
      while (dealer.shouldHit()) {
        consoleView.displayDealerHand(dealer);
        Card dealerCard = consoleView.promptCard(false, deck);

        if (dealerCard != null) {
          dealer.getHand().add(dealerCard.getRank());
          deck.remove(dealerCard.getRank());
        } else {
          consoleView.displayError("Failed to draw a card for the dealer.");
        }
      }
    } else if (dealer.getHand().getSize() == 1) {
      // If dealer hasn't revealed the down card yet
      consoleView.displayDealerHand(dealer);
      Card dealerCard = consoleView.promptCard(false, deck);

      if (dealerCard != null) {
        dealer.getHand().add(dealerCard.getRank());
        deck.remove(dealerCard.getRank());
      } else {
        consoleView.displayError("Failed to draw the dealer's down card.");
      }
    }

    consoleView.displayFinalDealerHand(dealer);
  }

  /**
   * Calculates and displays expected value (EV) percentages for different player
   * actions during a blackjack game.
   * Highlights the optimal decision by comparing EVs and color-coding the highest
   * EV.
   *
   * @param playerHand The current hand of the player being evaluated.
   * @param dealerHand The current hand of the dealer.
   * @throws IllegalArgumentException if {@code playerHand} or {@code dealerHand}
   *                                  is {@code null}.
   */
  private void evaluatePredictions(Hand playerHand, Hand dealerHand) {
    if (playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException("Arguments to evaluatePredictions cannot be null.");
    }

    int[] valueCounts = deck.getValueCounts();

    double standEV = pred.calculateStandEV(valueCounts, playerHand, dealerHand);
    double hitEV = pred.calculateHitEV(valueCounts, playerHand, dealerHand);
    double doubleEV = playerHand.getSize() == 2 ? pred.calculateDoubleEV(valueCounts, playerHand, dealerHand)
        : Double.NEGATIVE_INFINITY;
    double splitEV = (playerHand.canSplit()) ? pred.calculateSplitEV(valueCounts, playerHand, dealerHand)
        : Double.NEGATIVE_INFINITY;
    double surrenderEV = playerHand.getSize() == 2 && GameRules.SURRENDER
        ? pred.calculateSurrenderEV(playerHand, dealerHand)
        : Double.NEGATIVE_INFINITY;

    consoleView.displayEVs(standEV, hitEV, doubleEV, splitEV, surrenderEV);
  }

  /**
   * Evaluates and displays the outcome of the current round, comparing each
   * player's hand against the dealer's hand.
   *
   * @param currRound The current round instance being played.
   * @throws IllegalArgumentException if {@code currRound} is {@code null}.
   */
  private void evaluateRound(Round currRound) {
    if (currRound == null) {
      throw new IllegalArgumentException("Current round cannot be null.");
    }

    Dealer dealer = currRound.getDealer();
    int dealerScore = dealer.getHand().evaluateHand();
    int dealerSize = dealer.getHand().getSize();

    consoleView.displayDealerHand(dealer);

    for (int playerIndex = 0; playerIndex < currRound.getNumPlayers(); playerIndex++) {
      Player player = currRound.getPlayers().get(playerIndex);
      consoleView.displayMessage("Player " + (playerIndex + 1) + ": ");

      for (int handIndex = 0; handIndex < player.getNumHands(); handIndex++) {
        Hand hand = player.getHands().get(handIndex);
        int playerScore = hand.evaluateHand();
        int playerSize = hand.getSize();

        String result = evaluateHandResult(playerScore, playerSize, dealerScore, dealerSize);
        consoleView.displayHandResult(handIndex, hand, result);
      }
    }
  }

  /**
   * Evaluates the result of a hand in comparison to the dealer's hand.
   * Handles all possible outcomes such as busts, blackjacks, pushes, wins, and
   * losses based on card values.
   *
   * @param playerScore The total score of the player's hand.
   * @param playerSize  The number of cards in the player's hand.
   * @param dealerScore The total score of the dealer's hand.
   * @param dealerSize  The number of cards in the dealer's hand.
   * @return A formatted string indicating the result of the hand.
   */
  private String evaluateHandResult(int playerScore, int playerSize, int dealerScore, int dealerSize) {
    if (playerScore == 0) {
      return ConsoleUtil.colorText("N/A", ConsoleUtil.ANSI_GRAY);
    } else if (playerScore > 21) {
      return ConsoleUtil.colorText("PLAYER BUSTED", ConsoleUtil.ANSI_RED);
    } else if (playerScore == 21 && playerSize == 2 && dealerScore == 21 && dealerSize == 2) {
      return ConsoleUtil.colorText("PUSH", ConsoleUtil.ANSI_GRAY);
    } else if (playerScore == 21 && playerSize == 2) {
      return ConsoleUtil.colorText("PLAYER BLACKJACK", ConsoleUtil.ANSI_YELLOW);
    } else if (dealerScore > 21) {
      return ConsoleUtil.colorText("DEALER BUSTED", ConsoleUtil.ANSI_GREEN);
    } else if (playerScore > dealerScore) {
      return ConsoleUtil.colorText("PLAYER WINS", ConsoleUtil.ANSI_GREEN);
    } else if (playerScore == dealerScore) {
      return ConsoleUtil.colorText("PUSH", ConsoleUtil.ANSI_GRAY);
    } else {
      return ConsoleUtil.colorText("PLAYER LOSES", ConsoleUtil.ANSI_RED);
    }
  }

  /**
   * Main method to start the Blackjack game.
   *
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args) {
    GameEngine game = new GameEngine();
    game.startGame();
  }
}