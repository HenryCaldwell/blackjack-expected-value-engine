package henrycaldwell;

import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Scanner;

/**
 * Represents and manages the core gameplay logic and state for Blackjack.
 */
public class GameEngine {

    // Maps card abbreviations to their corresponding ranks.
    private final Map<String, Card.Ranks> RANK_NAMES = Map.ofEntries(
        new SimpleEntry<>("A", Card.Ranks.ACE),
        new SimpleEntry<>("2", Card.Ranks.TWO),
        new SimpleEntry<>("3", Card.Ranks.THREE),
        new SimpleEntry<>("4", Card.Ranks.FOUR),
        new SimpleEntry<>("5", Card.Ranks.FIVE),
        new SimpleEntry<>("6", Card.Ranks.SIX),
        new SimpleEntry<>("7", Card.Ranks.SEVEN),
        new SimpleEntry<>("8", Card.Ranks.EIGHT),
        new SimpleEntry<>("9", Card.Ranks.NINE),
        new SimpleEntry<>("10", Card.Ranks.TEN),
        new SimpleEntry<>("J", Card.Ranks.JACK),
        new SimpleEntry<>("Q", Card.Ranks.QUEEN),
        new SimpleEntry<>("K", Card.Ranks.KING)
    );

    private Deck deck; // The deck used in the game.
    private PredictionModel pred; // The prediction model used for calculating expected return values.
    private Scanner scanner; // Scanner for reading user input.

    /**
     * Constructs a GameEngine instance by initializing the deck and setting up the input scanner.
     */
    public GameEngine() {
        this.deck = new Deck();
        this.pred = new PredictionModel(deck);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game loop, continuously prompting players until the game is terminated.
     */
    public void startGame() {
        while (true) {
            int initialPlayers = promptNumberOfPlayers();

            if (initialPlayers == 0) {
                break;
            }

            if (promptShuffle()) {
                deck.shuffle();
            }

            startRound(initialPlayers);
        }

        scanner.close();
    }

    /**
     * Prompts for the number of players participating in the round.
     * @return The number of players as an integer.
     */
    private int promptNumberOfPlayers() {
        System.out.println(ConsoleUtil.colorText("Enter the number of players (e.g., '3' for 3 or '0' to quit):", ConsoleUtil.ANSI_BLUE));

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println(ConsoleUtil.colorText("ENTER A VALID INTEGER", ConsoleUtil.ANSI_RED));
        }

        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    /**
     * Prompts whether to shuffle the deck before starting the round.
     * @return True if the deck should be shuffled, false otherwise.
     */
    private boolean promptShuffle() {
        System.out.println(ConsoleUtil.colorText("CARDS REMAINING IN DECK: " + deck.getSize(), ConsoleUtil.ANSI_GRAY));
        System.out.println(ConsoleUtil.colorText("Shuffle deck ('Y' or 'N'):", ConsoleUtil.ANSI_BLUE));

        String input = scanner.nextLine().trim().toUpperCase();

        while (!input.equals("Y") && !input.equals("N")) {
            System.out.println(ConsoleUtil.colorText("ENTER A VALID OPTION", ConsoleUtil.ANSI_RED));
            input = scanner.nextLine().trim().toUpperCase();
        }

        return input.equals("Y");
    }

    /**
     * Prompts the user to select a card from the deck, providing the option to specify unknown with '?'.
     * @return The selected card or null if '?' is entered.
     */
    private Card promptCard(boolean nullPossible) {
        System.out.println(ConsoleUtil.colorText("Enter a card value (e.g., 'K' for King):", ConsoleUtil.ANSI_BLUE));
        System.out.println(ConsoleUtil.colorText("NEXT CARD IN DECK: " + deck.getCards().get(0), ConsoleUtil.ANSI_GRAY));
    
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("?") && nullPossible) {
                return null;
            }
    
            if (!RANK_NAMES.containsKey(input)) {
                System.out.println(ConsoleUtil.colorText("ENTER A VALID CARD RANK", ConsoleUtil.ANSI_RED));
                continue;
            }
    
            Card card = new Card(RANK_NAMES.get(input));
            
            if (deck.contains(card)) {
                return card;
            }

            System.out.println(ConsoleUtil.colorText("CARD UNAVAILABLE", ConsoleUtil.ANSI_RED));
        }
    }

    /**
     * Manages the setup and gameplay for a single round.
     * @param initialPlayers The number of players participating in the round.
     */
    private void startRound(int initialPlayers) {
        Round currRound = new Round(deck, initialPlayers);
        setUpRound(currRound, false);
        processRound(currRound);
        evaluateRound(currRound);
    }

    /**
     * Configures the game setup for a new round, distributing initial cards and handling initial game settings.
     * @param currRound The current round instance being played.
     * @param isSplit Indicates whether the setup is part of a split scenario.
     */
    private void setUpRound(Round currRound, boolean isSplit) {
        Dealer dealer = currRound.getDealer();

        for (int dealRound = 0; dealRound < 2; dealRound++) {
            for (int playerIndex = 0; playerIndex < currRound.getNumPlayers(); playerIndex++) {
                Player player = currRound.getPlayers().get(playerIndex);

                for (int handIndex = 0; handIndex < player.getNumHands(); handIndex++) {
                    Hand hand = player.getHands().get(handIndex);

                    if (hand.getSize() < 2) {
                        System.out.println("Player " + (playerIndex + 1) + ":\nHand " + (handIndex + 1) + ": " + hand);
                        hand.addDrop(promptCard(false));
                    }
                }
            }

            if (!isSplit) {
                System.out.println("Dealer:\nHand: " + dealer);
                String dealerMessage = dealRound == 0 ? "DEALER UP CARD" : "DEALER DOWN CARD ('?'' IF UNKNOWN)";
                System.out.println(ConsoleUtil.colorText(dealerMessage, ConsoleUtil.ANSI_GRAY));
                boolean isDownCard = dealRound == 1 ? true : false;
                dealer.getHand().addDrop(promptCard(isDownCard));
            }
        }

        if (!isSplit) {
            System.out.println(currRound + "\n");
        }
    }

    /**
     * Processes the actions of each player and the dealer during a round.
     * Each player decides on their moves until all hands are settled, followed by the dealer's turn according to game rules.
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
                System.out.println(ConsoleUtil.colorText("Player " + (playerIndex + 1) + ":\nHand " + (handIndex + 1) + " FINAL: " + hand + "\n", ConsoleUtil.ANSI_YELLOW));
            }
        }

        processDealerTurn(currRound);
    }

    /**
     * Processes a single player's turn, allowing them to make decisions such as hit, stand, double, split, or surrender.
     * @param currRound The current round instance being played.
     * @param playerIndex The index of the player taking the turn.
     * @param handIndex The index of the hand currently being played.
     */
    private void processPlayerTurn(Round currRound, int playerIndex, int handIndex) { 
        Player player = currRound.getPlayers().get(playerIndex);
        Dealer dealer = currRound.getDealer();

        while (true) {
            Hand hand = player.getHands().get(handIndex);

            if (hand.evaluateHand() == 21 && hand.getSize() == 2) {
                return;
            }

            System.out.println("Player " + (playerIndex + 1) + ":\nHand " + (handIndex + 1) + ": " + hand);
            evaluatePredictions(hand, dealer.getHand());

            System.out.println(ConsoleUtil.colorText("Choose an action ('Hit', 'Stand', 'Double', 'Split', 'Surrender'):", ConsoleUtil.ANSI_BLUE));            
            System.out.println(ConsoleUtil.colorText("True count: " + this.deck.getCount(), ConsoleUtil.ANSI_GRAY));

            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "HIT":
                    hand.addDrop(promptCard(false));
                    if (hand.evaluateHand() >= 21) {
                        return;
                    }
                    break;
                case "STAND":
                    return;
                case "DOUBLE":
                    hand.addDrop(promptCard(false));
                    return;
                case "SPLIT":
                    if (hand.getSize() == 2 && hand.getCards().get(0).getRank().getValue() == hand.getCards().get(1).getRank().getValue()) {
                        player.splitHand(handIndex);
                        setUpRound(currRound, true);
                        break;
                    }
                    System.out.println(ConsoleUtil.colorText("SPLIT UNAVAILABLE", ConsoleUtil.ANSI_RED));
                    break;
                case "SURRENDER":
                    if (GameRules.SURRENDER) {
                        hand.setCards(new ArrayList<>());
                        return;
                    }
                    System.out.println(ConsoleUtil.colorText("SURRENDER UNAVAILABLE", ConsoleUtil.ANSI_RED));
                    break;
                default:
                    System.out.println(ConsoleUtil.colorText("INVALID OPTION", ConsoleUtil.ANSI_RED));
                    continue;
            }
        }
    } 

    /**
     * Processes the dealer's turn following all player actions.
     * The dealer will hit or stand according to the game rules, specifically if the dealer hits on a soft 17.
     * @param currRound The current round instance being played.
     */
    private void processDealerTurn(Round currRound) {
        Dealer dealer = currRound.getDealer();
    
        if (!currRound.allPlayerHandsResolved() || GameRules.DEALER_ALWAYS_PLAYS_OUT) {
            while (dealer.getHand().evaluateHand() < 17 || (dealer.getHand().evaluateHand() == 17 && dealer.getHand().isSoftHand() && GameRules.DEALER_HITS_ON_SOFT_17)) {
                System.out.println("Dealer: " + dealer);
                dealer.getHand().addDrop(promptCard(false));
            }
        } else if (dealer.getHand().getSize() == 1) {
            System.out.println("Dealer: " + dealer);
            dealer.getHand().addDrop(promptCard(false));
        }
    
        System.out.println(ConsoleUtil.colorText("Dealer FINAL: " + dealer + "\n", ConsoleUtil.ANSI_YELLOW));
    }

    /**
     * Calculates and displays expected value (EV) percentages for different player actions during a blackjack game.
     * Highlights the optimal decision by comparing EVs and color-coding the highest EV.
     * @param playerHand The current hand of the player being evaluated.
     * @param dealerHand The current hand of the dealer.
     */
    private void evaluatePredictions(Hand playerHand, Hand dealerHand) {
        int firstCardVal = playerHand.getCards().get(0).getRank().getValue();
        int secondCardVal = playerHand.getCards().get(1).getRank().getValue();

        double standEV = pred.calculateStandEV(playerHand, dealerHand);
        double hitEV = pred.calculateHitEV(playerHand, dealerHand);
        double doubleEV = playerHand.getSize() == 2 ? pred.calculateDoubleEV(playerHand, dealerHand) : Double.NEGATIVE_INFINITY;
        double splitEV = playerHand.getSize() == 2 && firstCardVal == secondCardVal ? pred.calculateSplitEV(playerHand, dealerHand) : Double.NEGATIVE_INFINITY;
        double surrenderEV = playerHand.getSize() == 2 && GameRules.SURRENDER ? pred.calculateSurrenderEV(playerHand, dealerHand) : Double.NEGATIVE_INFINITY;

        double maxEV = Math.max(Math.max(Math.max(standEV, hitEV), Math.max(doubleEV, splitEV)), surrenderEV);

        String doubleMessage = playerHand.getSize() == 2 ? "DoubleEV: " + doubleEV * 100 + " %" : "DoubleEV: N/A";
        String splitMessage = playerHand.getSize() == 2 && firstCardVal == secondCardVal ? "SplitEV: " + splitEV * 100 + " %" : "SplitEV: N/A";
        String surrenderMessage = playerHand.getSize() == 2 && GameRules.SURRENDER ? "SurrenderEV: " + surrenderEV * 100 + " %" : "SurrenderEV: N/A";
        String standEVColor = maxEV == standEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
        String hitEVColor = maxEV == hitEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
        String doubleEVColor = maxEV == doubleEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
        String splitEVColor = maxEV == splitEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;
        String surrenderEVColor = maxEV == surrenderEV ? ConsoleUtil.ANSI_GREEN : ConsoleUtil.ANSI_GRAY;

        System.out.println(ConsoleUtil.colorText("StandEV: " + standEV * 100 + " %", standEVColor));
        System.out.println(ConsoleUtil.colorText("HitEV: " + hitEV * 100 + " %", hitEVColor));
        System.out.println(ConsoleUtil.colorText(doubleMessage, doubleEVColor));
        System.out.println(ConsoleUtil.colorText(splitMessage, splitEVColor));
        System.out.println(ConsoleUtil.colorText(surrenderMessage, surrenderEVColor));
    }

    /**
     * Evaluates and displays the outcome of the current round, comparing each player's hand against the dealer's hand.
     * @param currRound The current round instance being played.
     */
    private void evaluateRound(Round currRound) {
        Dealer dealer = currRound.getDealer();
        int dealerScore = dealer.getHand().evaluateHand();
        int dealerSize = dealer.getHand().getSize();

        System.out.println("Dealer: " + dealer);

        for (int playerIndex = 0; playerIndex < currRound.getNumPlayers(); playerIndex++) {
            Player player = currRound.getPlayers().get(playerIndex);
            System.out.println("Player " + (playerIndex + 1) + ": ");

            for (int handIndex = 0; handIndex < player.getNumHands(); handIndex++) {
                Hand hand = player.getHands().get(handIndex);
                int playerScore = hand.evaluateHand();
                int playerSize = hand.getSize();

                String result = evaluateHandResult(playerScore, playerSize, dealerScore, dealerSize);
                System.out.println("Hand " + (handIndex + 1) + ": " + hand + " - " + result);
            }
        }
    }

    /**
     * Evaluates the result of a hand in comparison to the dealer's hand. 
     * Handles all possible outcomes such as busts, blackjacks, pushes, wins, and losses based on card values.
     * @param playerScore The total score of the player's hand.
     * @param playerSize The number of cards in the player's hand.
     * @param dealerScore The total score of the dealer's hand.
     * @param dealerSize The number of cards in the dealer's hand.
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
     * Main method to start the game.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.startGame();
    }
}