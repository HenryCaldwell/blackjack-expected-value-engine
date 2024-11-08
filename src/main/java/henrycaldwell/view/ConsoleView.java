package henrycaldwell.view;

import java.util.Scanner;

import henrycaldwell.model.Card;
import henrycaldwell.model.Dealer;
import henrycaldwell.model.Deck;
import henrycaldwell.model.Hand;
import henrycaldwell.model.Round;

/**
 * Handles all console-based user interactions for the Blackjack game.
 * <p>
 * The {@code ConsoleView} class manages prompts, displays messages, and handles
 * user inputs via the console. It utilizes the {@link ConsoleUtil} class to
 * enhance the visual presentation of messages with ANSI colors.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * ConsoleView consoleView = new ConsoleView();
 * int numberOfPlayers = consoleView.promptNumberOfPlayers();
 * }</pre>
 */
public class ConsoleView {
  private Scanner scanner; // Scanner for reading user input from the console.

  /**
   * Constructs a {@code ConsoleView} instance and initializes the scanner.
   */
  public ConsoleView() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Prompts the user to enter the number of players participating in the game.
   *
   * @return The number of players as an integer. Returns {@code 0} if the user
   *         chooses to quit.
   */
  public int promptNumberOfPlayers() {
    System.out.println(
        ConsoleUtil.colorText("Enter the number of players (e.g., '3' for 3 or '0' to quit): ",
            ConsoleUtil.ANSI_BLUE));

    while (!scanner.hasNextInt()) {
      scanner.next(); // Consume invalid input
      System.out.println(ConsoleUtil.colorText("ENTER A VALID INTEGER", ConsoleUtil.ANSI_RED));
    }

    int input = scanner.nextInt();
    scanner.nextLine(); // Consume the remaining newline

    System.out.println(); // Empty line separator

    return input;
  }

  /**
   * Prompts the user to decide whether to reset (shuffle) the deck based on the
   * current deck size.
   *
   * @param deckSize The number of remaining cards in the deck.
   * @return {@code true} if the user chooses to shuffle the deck; {@code false}
   *         otherwise.
   */
  public boolean promptReset(int deckSize) {
    System.out.println(ConsoleUtil.colorText("CARDS REMAINING IN DECK: " + deckSize, ConsoleUtil.ANSI_GRAY));
    System.out.println(ConsoleUtil.colorText("Shuffle deck ('Y' or 'N'):", ConsoleUtil.ANSI_BLUE));
    String input = scanner.nextLine().trim().toUpperCase();

    while (!input.equals("Y") && !input.equals("N")) {
      System.out.println(ConsoleUtil.colorText("ENTER A VALID OPTION (Y/N)", ConsoleUtil.ANSI_RED));
      input = scanner.nextLine().trim().toUpperCase();
    }

    System.out.println(); // Empty line separator

    return input.equals("Y");
  }

  /**
   * Prompts the user to enter a card value or an unknown card indicator.
   *
   * @param allowUnknown If {@code true}, the user can enter '?' to indicate an
   *                     unknown card.
   * @param deck         The current deck from which cards are drawn.
   * @return A {@code Card} object representing the drawn card, or {@code null} if
   *         an unknown card is indicated.
   * @throws IllegalStateException if the deck is empty and no card can be drawn.
   */
  public Card promptCard(boolean allowUnknown, Deck deck) {
    System.out.println(ConsoleUtil.colorText("Enter a card value (e.g., 'K' for King):", ConsoleUtil.ANSI_BLUE));

    if (allowUnknown) {
      System.out.println(ConsoleUtil.colorText("Or enter '?' if the card is unknown.", ConsoleUtil.ANSI_BLUE));
    }

    String nextCardInfo = deck.getCards().isEmpty() ? "None" : deck.getCards().get(0).toString();
    System.out.println(
        ConsoleUtil.colorText("NEXT CARD IN DECK: " + nextCardInfo, ConsoleUtil.ANSI_GRAY));

    while (true) {
      String input = scanner.nextLine().trim().toUpperCase();

      if (allowUnknown && input.equals("?")) {
        System.out.println(); // Empty line separator
        return null;
      }

      Card.Rank rank;
      try {
        rank = Card.Rank.fromAbbreviation(input);
      } catch (IllegalArgumentException e) {
        System.out.println(ConsoleUtil.colorText("ENTER A VALID CARD RANK (e.g., 'A', '2', ..., 'K')",
            ConsoleUtil.ANSI_RED));
        continue;
      }

      if (deck.contains(rank)) {
        System.out.println(); // Empty line separator
        return new Card(rank);
      }

      System.out.println(ConsoleUtil.colorText("CARD UNAVAILABLE IN DECK", ConsoleUtil.ANSI_RED));
    }
  }

  /**
   * Displays the current hand of a player.
   *
   * @param playerIndex The index of the player (starting from 0).
   * @param handIndex   The index of the hand (starting from 0) if the player has
   *                    multiple hands.
   * @param hand        The {@link Hand} object representing the player's hand.
   */
  public void displayPlayerHand(int playerIndex, int handIndex, Hand hand) {
    System.out.println("Player " + (playerIndex + 1) + ":\nHand " + (handIndex + 1) + ": " + hand);
  }

  /**
   * Displays the final state of a player's hand after their turn is complete.
   *
   * @param playerIndex The index of the player (starting from 0).
   * @param handIndex   The index of the hand (starting from 0) if the player has
   *                    multiple hands.
   * @param hand        The {@link Hand} object representing the player's final
   *                    hand.
   */
  public void displayFinalPlayerHand(int playerIndex, int handIndex, Hand hand) {
    System.out.println(
        ConsoleUtil.colorText("Player " + (playerIndex + 1) + ":\nHand " + (handIndex + 1) + " FINAL: " + hand,
            ConsoleUtil.ANSI_YELLOW));
    System.out.println(); // Empty line separator
  }

  /**
   * Displays the dealer's current hand.
   *
   * @param dealer The {@link Dealer} object representing the dealer.
   */
  public void displayDealerHand(Dealer dealer) {
    System.out.println("Dealer:\nHand: " + dealer.getHand());
    System.out.println(); // Empty line separator
  }

  /**
   * Displays the dealer's final hand after the dealer's turn is complete.
   *
   * @param dealer The {@link Dealer} object representing the dealer.
   */
  public void displayFinalDealerHand(Dealer dealer) {
    System.out.println(
        ConsoleUtil.colorText("Dealer FINAL: " + dealer.getHand(), ConsoleUtil.ANSI_YELLOW));
    System.out.println(); // Empty line separator
  }

  /**
   * Displays the current state of the round, including all players' hands and the
   * dealer's hand.
   *
   * @param round The {@link Round} object representing the current round.
   */
  public void displayRound(Round round) {
    System.out.println(round);
    System.out.println(); // Empty line separator
  }

  /**
   * Displays a generic message to the console.
   *
   * @param message The message to display.
   */
  public void displayMessage(String message) {
    System.out.println(message);
    // System.out.println(); // Empty line separator (commented out as per original
    // code)
  }

  /**
   * Displays an error message in red color to indicate an issue or invalid
   * action.
   *
   * @param error The error message to display.
   */
  public void displayError(String error) {
    System.out.println(ConsoleUtil.colorText(error, ConsoleUtil.ANSI_RED));
    System.out.println(); // Empty line separator
  }

  /**
   * Prompts the user to choose an action during their turn.
   *
   * @param trueCount The current true count (for card counting strategies).
   * @return The action chosen by the user as an uppercase string.
   */
  public String promptAction(double trueCount) {
    System.out.println(ConsoleUtil.colorText(
        "Choose an action ('Hit', 'Stand', 'Double', 'Split', 'Surrender'):", ConsoleUtil.ANSI_BLUE));
    System.out.println(ConsoleUtil.colorText("True count: " + trueCount, ConsoleUtil.ANSI_GRAY));

    String action = scanner.nextLine().trim().toUpperCase();
    System.out.println(); // Empty line separator

    return action;
  }

  /**
   * Displays the expected value (EV) percentages for different player actions.
   * Highlights the optimal decision by color-coding the highest EV.
   *
   * @param standEV     The EV percentage for choosing to stand.
   * @param hitEV       The EV percentage for choosing to hit.
   * @param doubleEV    The EV percentage for choosing to double. If doubling is
   *                    unavailable, it shows "N/A".
   * @param splitEV     The EV percentage for choosing to split. If splitting is
   *                    unavailable, it shows "N/A".
   * @param surrenderEV The EV percentage for choosing to surrender. If
   *                    surrendering is unavailable, it shows "N/A".
   */
  public void displayEVs(double standEV, double hitEV, double doubleEV, double splitEV, double surrenderEV) {
    double maxEV = Math.max(Math.max(Math.max(standEV, hitEV), Math.max(doubleEV, splitEV)), surrenderEV);

    String standEVColor = maxEV == standEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
    String hitEVColor = maxEV == hitEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
    String doubleEVColor = maxEV == doubleEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
    String splitEVColor = maxEV == splitEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
    String surrenderEVColor = maxEV == surrenderEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;

    System.out.println(ConsoleUtil.colorText("StandEV: " + (standEV * 100) + " %", standEVColor));
    System.out.println(ConsoleUtil.colorText("HitEV: " + (hitEV * 100) + " %", hitEVColor));
    System.out.println(ConsoleUtil.colorText(
        "DoubleEV: " + (doubleEV != Double.NEGATIVE_INFINITY ? (doubleEV * 100) + " %" : "N/A"),
        doubleEVColor));
    System.out.println(ConsoleUtil.colorText(
        "SplitEV: " + (splitEV != Double.NEGATIVE_INFINITY ? (splitEV * 100) + " %" : "N/A"),
        splitEVColor));
    System.out.println(ConsoleUtil.colorText(
        "SurrenderEV: " + (surrenderEV != Double.NEGATIVE_INFINITY ? (surrenderEV * 100) + " %" : "N/A"),
        surrenderEVColor));
    System.out.println(); // Empty line separator
  }

  /**
   * Displays the result of a player's hand after comparing it to the dealer's
   * hand.
   *
   * @param handIndex The index of the hand (starting from 0) if the player has
   *                  multiple hands.
   * @param hand      The {@link Hand} object representing the player's hand.
   * @param result    The result of the hand comparison (e.g., "PLAYER WINS",
   *                  "PLAYER BUSTED").
   */
  public void displayHandResult(int handIndex, Hand hand, String result) {
    System.out.println("Hand " + (handIndex + 1) + ": " + hand + " - " + result);
    System.out.println(); // Empty line separator
  }
}
