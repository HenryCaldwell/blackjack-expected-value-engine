package henrycaldwell.controller;

import java.util.HashMap;
import java.util.Map;

import henrycaldwell.model.Card;
import henrycaldwell.model.Hand;

/**
 * Represents the prediction logic for calculating expected values (EV) for
 * various actions in a blackjack game.
 * Expected values represent the average return on investment over an infinite
 * number of hands.
 * <p>
 * This class uses memoization to cache results and optimize recursive
 * calculations.
 * </p>
 */
public class PredictionModel {

  private Map<String, Double> memoizationCache; // The caching mechanism to store previously calculated outcomes.

  /**
   * Constructs a {@code PredictionModel} instance.
   */
  public PredictionModel() {
    this.memoizationCache = new HashMap<>();
  }

  /**
   * Calculates the expected value when the player chooses to stand.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The current hand of the player.
   * @param dealerHand  The current hand of the dealer.
   * @return The expected value of standing.
   * @throws IllegalArgumentException if any of the arguments are {@code null}.
   */
  public double calculateStandEV(int[] valueCounts, Hand playerHand, Hand dealerHand) {
    if (valueCounts == null || playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException(
          "Arguments to calculateStandEV cannot be null: valueCounts, playerHand, and dealerHand are required.");
    }

    return calculateStandEV(valueCounts, playerHand, dealerHand, false);
  }

  /**
   * Computes the expected value for standing based on the current card
   * distribution in the deck and both the player's and the dealer's hands.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @param isSplit     Indicates if the hand is a result of a split.
   * @return The expected value of standing.
   */
  private double calculateStandEV(int[] valueCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
    String stateKey = getStateKey(valueCounts, playerHand, dealerHand, isSplit, "stand");

    if (memoizationCache.containsKey(stateKey)) {
      return memoizationCache.get(stateKey);
    }

    int dealerScore = dealerHand.evaluateHand();
    int dealerFirstCardValue = dealerHand.getCards().get(0).getRank().getValue();
    boolean isSoftHand = dealerHand.isSoftHand();

    if ((dealerScore > 17) ||
        (dealerScore == 17 && !isSoftHand) ||
        (dealerScore == 17 && isSoftHand && !GameRules.DEALER_HITS_ON_SOFT_17)) {
      double outcome = evaluateOutcome(playerHand, dealerHand, isSplit);
      memoizationCache.put(stateKey, outcome);

      return outcome;
    }

    double totalValue = 0.0;
    int totalCards = 0;

    for (int i = 0; i < valueCounts.length; i++) {
      if (valueCounts[i] > 0) {
        if (GameRules.DEALER_PEAKS_FOR_21 && dealerHand.getSize() == 1 &&
            ((dealerFirstCardValue == 10 && i == 0) ||
                (dealerFirstCardValue == 1 && i == 9))) {
          continue;
        }

        int[] newValueCounts = valueCounts.clone();
        newValueCounts[i]--;

        Hand newDealerHand = dealerHand.clone();
        newDealerHand.add(Card.Rank.fromValue(i + 1));

        double outcome = calculateStandEV(newValueCounts, playerHand, newDealerHand, isSplit) * valueCounts[i];
        totalValue += outcome;
        totalCards += valueCounts[i];
      }
    }

    double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
    memoizationCache.put(stateKey, EV);
    return EV;
  }

  /**
   * Calculates the expected value when the player chooses to hit.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @return The expected value of hitting.
   * @throws IllegalArgumentException if any of the arguments are {@code null}.
   */
  public double calculateHitEV(int[] valueCounts, Hand playerHand, Hand dealerHand) {
    if (valueCounts == null || playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException(
          "Arguments to calculateHitEV cannot be null: valueCounts, playerHand, and dealerHand are required.");
    }

    return calculateHitEV(valueCounts, playerHand, dealerHand, false);
  }

  /**
   * Computes the expected value for hitting based on the current card
   * distribution in the deck and both the player's and the dealer's hands.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @param isSplit     Indicates if the hand is a result of a split.
   * @return The expected value of hitting.
   */
  private double calculateHitEV(int[] valueCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
    String stateKey = getStateKey(valueCounts, playerHand, dealerHand, isSplit, "hit");

    if (memoizationCache.containsKey(stateKey)) {
      return memoizationCache.get(stateKey);
    }

    double totalValue = 0.0;
    int totalCards = 0;

    for (int i = 0; i < valueCounts.length; i++) {
      if (valueCounts[i] > 0) {
        int[] newValueCounts = valueCounts.clone();
        newValueCounts[i]--;

        Hand newPlayerHand = playerHand.clone();
        newPlayerHand.add(Card.Rank.fromValue(i + 1));

        if (newPlayerHand.evaluateHand() > 21) {
          totalValue -= valueCounts[i];
          totalCards += valueCounts[i];
        } else {
          double standEV = calculateStandEV(newValueCounts, newPlayerHand, dealerHand, isSplit);
          double hitEV = calculateHitEV(newValueCounts, newPlayerHand, dealerHand, isSplit);

          double maxEV = Math.max(standEV, hitEV);
          totalValue += maxEV * valueCounts[i];
          totalCards += valueCounts[i];
        }
      }
    }

    double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
    memoizationCache.put(stateKey, EV);
    return EV;
  }

  /**
   * Calculates the expected value when the player chooses to double.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @return The expected value of doubling.
   * @throws IllegalArgumentException if any of the arguments are {@code null}.
   */
  public double calculateDoubleEV(int[] valueCounts, Hand playerHand, Hand dealerHand) {
    if (valueCounts == null || playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException(
          "Arguments to calculateDoubleEV cannot be null: valueCounts, playerHand, and dealerHand are required.");
    }

    return calculateDoubleEV(valueCounts, playerHand, dealerHand, false);
  }

  /**
   * Computes the expected value for doubling based on the current card
   * distribution in the deck and both the player's and the dealer's hands.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @param isSplit     Indicates if the hand is a result of a split.
   * @return The expected value of doubling.
   */
  private double calculateDoubleEV(int[] valueCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
    String stateKey = getStateKey(valueCounts, playerHand, dealerHand, isSplit, "double");

    if (memoizationCache.containsKey(stateKey)) {
      return memoizationCache.get(stateKey);
    }

    double totalValue = 0.0;
    int totalCards = 0;

    for (int i = 0; i < valueCounts.length; i++) {
      if (valueCounts[i] > 0) {
        int[] newValueCounts = valueCounts.clone();
        newValueCounts[i]--;

        Hand newPlayerHand = playerHand.clone();
        newPlayerHand.add(Card.Rank.fromValue(i + 1));

        if (newPlayerHand.evaluateHand() > 21) {
          totalValue -= 2.0 * valueCounts[i];
          totalCards += valueCounts[i];
        } else {
          double outcome = calculateStandEV(newValueCounts, newPlayerHand, dealerHand, isSplit) * valueCounts[i] * 2.0;
          totalValue += outcome;
          totalCards += valueCounts[i];
        }
      }
    }

    double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
    memoizationCache.put(stateKey, EV);
    return EV;
  }

  /**
   * Calculates the expected value when the player chooses to split.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's current hand.
   * @param dealerHand  The dealer's current hand.
   * @return The expected value of splitting.
   * @throws IllegalArgumentException if any of the arguments are {@code null}.
   */
  public double calculateSplitEV(int[] valueCounts, Hand playerHand, Hand dealerHand) {
    if (valueCounts == null || playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException(
          "Arguments to calculateSplitEV cannot be null: valueCounts, playerHand, and dealerHand are required.");
    }

    String stateKey = getStateKey(valueCounts, playerHand, dealerHand, true, "stand");

    if (memoizationCache.containsKey(stateKey)) {
      return memoizationCache.get(stateKey);
    }

    Card splitCard = playerHand.getCards().get(0);
    boolean isAceSplit = splitCard.getRank() == Card.Rank.ACE;

    double totalEV = 0.0;
    int totalCards = 0;

    for (int i = 0; i < valueCounts.length; i++) {
      if (valueCounts[i] > 0) {
        int[] newValueCounts = valueCounts.clone();
        newValueCounts[i]--;

        Hand newPlayerHand = new Hand();
        newPlayerHand.add(splitCard.getRank());
        newPlayerHand.add(Card.Rank.fromValue(i + 1));

        double standEV = calculateStandEV(newValueCounts, newPlayerHand, dealerHand, true);
        double hitEV = Double.NEGATIVE_INFINITY;
        double doubleEV = Double.NEGATIVE_INFINITY;

        if (isAceSplit && GameRules.HIT_SPLIT_ACES || !isAceSplit) {
          hitEV = calculateHitEV(newValueCounts, newPlayerHand, dealerHand, true);
        }

        if (GameRules.DOUBLE_AFTER_SPLIT
            && ((isAceSplit && GameRules.HIT_SPLIT_ACES && GameRules.DOUBLE_SPLIT_ACES) || !isAceSplit)) {
          doubleEV = calculateDoubleEV(newValueCounts, newPlayerHand, dealerHand, true);
        }

        double outcome = Math.max(standEV, Math.max(hitEV, doubleEV));
        totalEV += outcome * valueCounts[i] * 2;
        totalCards += valueCounts[i];
      }
    }

    double EV = totalCards > 0 ? totalEV / totalCards : 0.0;
    memoizationCache.put(stateKey, EV);
    return EV;
  }

  /**
   * Calculates the expected value when the player chooses to surrender.
   *
   * @param playerHand The player's current hand.
   * @param dealerHand The dealer's current hand.
   * @return The expected value of surrendering.
   * @throws IllegalArgumentException if any of the arguments are {@code null}.
   */
  public double calculateSurrenderEV(Hand playerHand, Hand dealerHand) {
    if (playerHand == null || dealerHand == null) {
      throw new IllegalArgumentException(
          "Arguments to calculateSurrenderEV cannot be null: playerHand and dealerHand are required.");
    }
    return -0.5;
  }

  /**
   * Evaluates the final outcome of a hand comparison between the player and the
   * dealer.
   *
   * @param playerHand The player's hand.
   * @param dealerHand The dealer's hand.
   * @param isSplit    Indicates if the hand is a result of a split.
   * @return The numeric outcome of the game; positive values indicate player
   *         wins, negative values indicate losses, and zero represents a push.
   */
  private double evaluateOutcome(Hand playerHand, Hand dealerHand, boolean isSplit) {
    int playerScore = playerHand.evaluateHand();
    int dealerScore = dealerHand.evaluateHand();
    int playerHandSize = playerHand.getSize();
    int dealerHandSize = dealerHand.getSize();

    boolean playerNaturalBlackjack = playerScore == 21 && playerHandSize == 2
        && (!isSplit || GameRules.NATURAL_BLACKJACK_SPLITS);
    boolean dealerNaturalBlackjack = dealerScore == 21 && dealerHandSize == 2;

    if (playerNaturalBlackjack && dealerNaturalBlackjack) {
      return 0.0;
    } else if (playerNaturalBlackjack) {
      return GameRules.BLACKJACK_ODDS;
    } else if (dealerNaturalBlackjack) {
      return -1.0;
    } else if (playerScore > 21) {
      return -1.0;
    } else if (dealerScore > 21) {
      return 1.0;
    } else if (playerScore > dealerScore) {
      return 1.0;
    } else if (playerScore < dealerScore) {
      return -1.0;
    } else {
      return 0.0;
    }
  }

  /**
   * Provides a consistent format for generating keys used in memoization.
   *
   * @param valueCounts The current distribution of card values in the deck.
   * @param playerHand  The player's hand.
   * @param dealerHand  The dealer's hand.
   * @param isSplit     Indicates if the hand is a result of a split.
   * @param action      The action being considered ("stand", "hit", "double", or
   *                    "split").
   * @return The formatted string key that uniquely identifies the current game
   *         state.
   */
  private String getStateKey(int[] valueCounts, Hand playerHand, Hand dealerHand, boolean isSplit, String action) {
    int playerScore = playerHand.evaluateHand();
    int dealerScore = dealerHand.evaluateHand();
    boolean playerSoft = playerHand.isSoftHand();

    return arrayToString(valueCounts) + "_" + playerScore + "_" + dealerScore + "_" + playerSoft + "_" + isSplit + "_"
        + action;
  }

  /**
   * Converts an array of integers into a single string representation,
   * primarily used in forming keys for memoization.
   *
   * @param arr The array of integers to convert.
   * @return The string representation of the array.
   */
  private String arrayToString(int[] arr) {
    StringBuilder sb = new StringBuilder();

    for (int count : arr) {
      sb.append(count).append(",");
    }

    return sb.toString();
  }
}