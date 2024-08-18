package henrycaldwell.engine;

import java.util.HashMap;
import java.util.Map;

import henrycaldwell.model.Card;
import henrycaldwell.model.Deck;
import henrycaldwell.model.Hand;

/**
 * Represents the prediction logic for calculating expected values (EV) for various actions in a blackjack game.
 * Expected values represent the average return on investment over infinite time.
 */
public class PredictionModel {

    private Deck deck; // The deck used for the calculations.
    private Map<String, Double> memoizationCache; // The caching mechanism to store previously calculated outcomes.

    /**
     * Constructs a PredictionModel with a specific deck.
     * @param deck The deck associated with the predictions.
     */
    public PredictionModel(Deck deck) {
        this.deck = deck;
        this.memoizationCache = new HashMap<>();
    }

    /**
     * Calculates and returns the counts of each card rank left in the deck.
     * @return The array representing the frequency of each card rank within the deck.
     */
    private int[] calculateCardCounts() {
        int[] cardCounts = new int[10];

        for (Card card : this.deck.getCards()) {
            int value = card.getRank().getValue();
            cardCounts[value - 1]++;
        }

        return cardCounts;
    }

    /**
     * Calculates the expected value when the player chooses to stand.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of standing.
     */
    public double calculateStandEV(Hand playerHand, Hand dealerHand) {
        return calculateStandEV(calculateCardCounts(), playerHand, dealerHand, false);
    }

    /**
     * Computes the expected value for standing based on the current card distribution in the deck and both the player's and the dealer's hands.
     * @param cardCounts The current distribution of card values in the deck.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of standing.
     */
    private double calculateStandEV(int[] cardCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
        String stateKey = getStateKey(cardCounts, playerHand.evaluateHand(), dealerHand.evaluateHand()) + isSplit + "_stand";

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

        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                if (GameRules.DEALER_PEAKS_FOR_21 && dealerHand.getSize() == 1 && (
                    (dealerFirstCardValue == 10 && i == 0) ||
                    (dealerFirstCardValue == 1 && i == 9))) {
                        continue;
                }

                int[] newCardCounts = cardCounts.clone();
                newCardCounts[i]--;

                Hand newDealerHand = dealerHand.clone();
                Card tempCard = new Card(Card.Rank.fromValue(i + 1));
                newDealerHand.add(tempCard);

                double outcome = calculateStandEV(newCardCounts, playerHand, newDealerHand, isSplit) * cardCounts[i];
                totalValue += outcome;
                totalCards += cardCounts[i];
            }
        }

        double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
        memoizationCache.put(stateKey, EV);
        return EV;
    }

    /**
     * Calculates the expected value when the player chooses to hit.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of hitting.
     */
    public double calculateHitEV(Hand playerHand, Hand dealerHand) {
        return calculateHitEV(calculateCardCounts(), playerHand, dealerHand, false);
    }

    /**
     * Computes the expected value for hitting based on the current card distribution in the deck and both the player's and the dealer's hands.
     * @param cardCounts The current distribution of card values in the deck.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of hitting.
     */
    private double calculateHitEV(int[] cardCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
        String stateKey = getStateKey(cardCounts, playerHand.evaluateHand(), dealerHand.evaluateHand()) + isSplit + "_hit";

        if (memoizationCache.containsKey(stateKey)) {
            return memoizationCache.get(stateKey);
        }

        double totalValue = 0.0;
        int totalCards = 0;

        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                int[] newCardCounts = cardCounts.clone();
                newCardCounts[i]--;

                Hand newPlayerHand = playerHand.clone();
                Card tempCard = new Card(Card.Rank.fromValue(i + 1));
                newPlayerHand.add(tempCard);

                if (newPlayerHand.evaluateHand() > 21) {
                    totalValue -= cardCounts[i];
                    totalCards += cardCounts[i];
                } else {
                    double standEV = calculateStandEV(newCardCounts, newPlayerHand, dealerHand, isSplit);
                    double hitEV = calculateHitEV(newCardCounts, newPlayerHand, dealerHand, isSplit);

                    double maxEV = Math.max(standEV, hitEV);
                    totalValue += maxEV * cardCounts[i];
                    totalCards += cardCounts[i];
                }
            }
        }

        double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
        memoizationCache.put(stateKey, EV);
        return EV;
    }

    /**
     * Calculates the expected value when the player chooses to double.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of doubling.
     */
    public double calculateDoubleEV(Hand playerHand, Hand dealerHand) {
        return calculateDoubleEV(calculateCardCounts(), playerHand, dealerHand, false);
    }

    /**
     * Computes the expected value for doubling based on the current card distribution in the deck and both the player's and the dealer's hands.
     * @param cardCounts The current distribution of card values in the deck.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of doubling.
     */
    private double calculateDoubleEV(int[] cardCounts, Hand playerHand, Hand dealerHand, boolean isSplit) {
        String stateKey = getStateKey(cardCounts, playerHand.evaluateHand(), dealerHand.evaluateHand()) + isSplit + "_double";

        if (memoizationCache.containsKey(stateKey)) {
            return memoizationCache.get(stateKey);
        }

        double totalValue = 0.0;
        int totalCards = 0;

        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                int[] newCardCounts = cardCounts.clone();
                newCardCounts[i]--;

                Hand newPlayerHand = playerHand.clone();
                Card tempCard = new Card(Card.Rank.fromValue(i + 1));
                newPlayerHand.add(tempCard);

                if (newPlayerHand.evaluateHand() > 21) {
                    totalValue -= 2.0 * cardCounts[i];
                    totalCards += cardCounts[i];
                } else {
                    double outcome = calculateStandEV(newCardCounts, newPlayerHand, dealerHand, isSplit) * cardCounts[i] * 2.0;
                    totalValue += outcome;
                    totalCards += cardCounts[i];
                }
            }
        }

        double EV = totalCards > 0 ? totalValue / totalCards : 0.0;
        memoizationCache.put(stateKey, EV);
        return EV;
    }

    /**
     * Calculates the expected value when the player chooses to split.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of splitting.
     */
    public double calculateSplitEV(Hand playerHand, Hand dealerHand) {
        return calculateSplitEV(calculateCardCounts(), playerHand, dealerHand);
    }

    /**
     * Computes the expected value for splitting based on the current card distribution in the deck and both the player's and the dealer's hands.
     * @param cardCounts The current distribution of card values in the deck.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of splitting.
     */
    private double calculateSplitEV(int[] cardCounts, Hand playerHand, Hand dealerHand) {
        String stateKey = getStateKey(cardCounts, playerHand.evaluateHand(), dealerHand.evaluateHand()) + "_split";

        if (memoizationCache.containsKey(stateKey)) {
            return memoizationCache.get(stateKey);
        }

        Card splitCard = playerHand.getCards().get(0);
        boolean isAceSplit = splitCard.getRank() == Card.Rank.ACE;

        double totalEV = 0.0;
        int totalCards = 0;

        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                int[] newCardCounts = cardCounts.clone();
                newCardCounts[i]--;

                Hand newPlayerHand = new Hand(deck);
                newPlayerHand.add(splitCard);
                Card tempCard = new Card(Card.Rank.fromValue(i + 1));
                newPlayerHand.add(tempCard);

                double standEV = calculateStandEV(newCardCounts, newPlayerHand, dealerHand, true);
                double hitEV = Double.NEGATIVE_INFINITY;
                double doubleEV = Double.NEGATIVE_INFINITY;

                if (isAceSplit && GameRules.HIT_SPLIT_ACES ||
                    !isAceSplit) {
                    hitEV = calculateHitEV(newCardCounts, newPlayerHand, dealerHand, true);
                } 
                
                if (GameRules.DOUBLE_AFTER_SPLIT &&
                    ((isAceSplit && GameRules.HIT_SPLIT_ACES && GameRules.DOUBLE_SPLIT_ACES) ||
                    !isAceSplit)) {
                    doubleEV = calculateDoubleEV(newCardCounts, newPlayerHand, dealerHand, true);
                }

                double outcome = Math.max(standEV, Math.max(hitEV, doubleEV));
                totalEV += outcome * cardCounts[i] * 2;
                totalCards += cardCounts[i];
            }
        }

        double EV = totalCards > 0 ? totalEV / totalCards : 0.0;
        memoizationCache.put(stateKey, EV);
        return EV;
    }

    /**
     * Calculates the expected value when the player chooses to surrender.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The expected value of surrendering.
     */
    public double calculateSurrenderEV(Hand playerHand, Hand dealerHand) {
        return -0.5;
    }

    /**
     * Evaluates the final outcome of a hand comparison between the player and the dealer.
     * @param playerHand The current hand of the player.
     * @param dealerHand The current hand of the dealer.
     * @return The numeric outcome of the game; positive values indicate player wins, negative values indicate losses, and zero represents a push.
     */
    private double evaluateOutcome(Hand playerHand, Hand dealerHand, boolean isSplit) {
        int playerScore = playerHand.evaluateHand();
        int dealerScore = dealerHand.evaluateHand();
        int playerHandSize = playerHand.getSize();
        int dealerHandSize = dealerHand.getSize();

        boolean playerNaturalBlackjack = playerScore == 21 && playerHandSize == 2 && (!isSplit || GameRules.NATURAL_BLACKJACK_SPLITS);
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
     * @param cardCounts The current distribution of card values in the deck.
     * @param playerScore The current hand of the player.
     * @param dealerScore The current hand of the dealer.
     * @return The formatted string key that uniquely identifies the current game state.
     */
    private String getStateKey(int[] cardCounts, int playerScore, int dealerScore) {
        return playerScore + "_" + dealerScore + "_" + arrayToString(cardCounts);
    }

    /**
     * Converts an array of integers into a single string representation, primarily used in forming keys for memoization.
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