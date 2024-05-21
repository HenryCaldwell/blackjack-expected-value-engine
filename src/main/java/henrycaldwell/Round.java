package henrycaldwell;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a single round of the blackjack game, managing the players, dealer, and their hands.
 */
public class Round {

    private List<Player> players; // The list of players participating in this round.
    private Dealer dealer; // The dealer for this round of the game.

    /**
     * Constructs a round with the specified deck and number of players.
     * @param deck The deck used for the round.
     * @param initialPlayers The number of players in the round.
     */
    public Round(Deck deck, int initialPlayers) {
        this.players = new ArrayList<>();
        this.dealer = new Dealer(new Hand(deck));
        generateHands(deck, initialPlayers);   
    }

    /**
     * Initializes the specified number of each players at the start of the round.
     * @param initialPlayers The number of players to generate hands for.
     */
    private void generateHands(Deck deck, int initialPlayers) {
        for(int playerIndex = 0; playerIndex < initialPlayers; playerIndex++) {
            List<Hand> hands = new ArrayList<>();
            hands.add(new Hand(deck));
            players.add(new Player(hands));
        }
    }

    /**
     * Checks if all player hands are resolved, meaning no further action is required from the players.
     * @return True if all hands are resolved, false otherwise.
     */
    public boolean allPlayerHandsResolved() {
        for (Player player : players) {
            for (Hand hand : player.getHands()) {
                int playerScore = hand.evaluateHand();
                
                if (!(playerScore == 21 && hand.getSize() == 2) && (playerScore < 21) && !(playerScore == 0)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Retrieves the list of players in the round.
     * @return The players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Retrieves the dealer of the round.
     * @return The dealer.
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Retrieves the number of players currently in the round.
     * @return The number of players.
     */
    public int getNumPlayers() {
        return players.size();
    }

    /**
     * Provides a string representation of the current state of the round, including all player hands and the dealer's hand.
     * @return A string representing the current state of the round.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int currentPlayer = 0; currentPlayer < players.size(); currentPlayer++) {
            output.append("Player ").append(currentPlayer + 1).append(":\n").append(players.get(currentPlayer)).append("\n");
        }

        output.append("Dealer:\nHand: ").append(dealer);
        return output.toString();
    }
}