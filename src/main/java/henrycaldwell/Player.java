package henrycaldwell;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in a card game.
 */
public class Player {

    private List<Hand> hands; // The list of hands held by the player.

    /**
     * Constructs a player with no hands.
     */
    public Player() {
        this.hands = new ArrayList<>();
    }
    
    /**
     * Constructs a player with a predefined list of hands.
     * @param hands The list of pre-existing hands to be assigned to the player.
     */
    public Player(List<Hand> hands) {
        this.hands = new ArrayList<>(hands);
    }

    /**
     * Adds a hand to the player's list of hands if the hand is not null.
     * @param hand The hand to add.
     * @return True if the hand was added successfully, false otherwise.
     */
    public Boolean addHand(Hand hand) {
        if (hand != null) {
            hands.add(hand);
            return true;
        }

        return false;
    }

    /**
     * Removes a hand from the player's list at the specified index.
     * @param index The index of the hand to be removed.
     * @return True if the hand was removed successfully, false otherwise.
     */
    public Boolean removeHand(int index) {
        if (index >= 0 && index < hands.size()) {
            hands.remove(index);
            return true;
        }

        return false;
    }

    /**
     * Attempts to split a hand at a specified index. Splitting is only allowed if the hand contains exactly two cards of the same rank.
     * @param index The index of the hand to split.
     * @return True if the hand was successfully split, false otherwise.
     */
    public Boolean splitHand(int index) {
        if (index >= 0 && index < hands.size()) {
            Hand hand = hands.get(index);

            if (canSplit(index)) {
                Card splitCard = hand.getCards().get(0);

                Hand firstSplit = new Hand(hand.getDeck());
                firstSplit.add(splitCard);
                Hand secondSplit = new Hand(hand.getDeck());
                secondSplit.add(splitCard);

                hands.set(index, firstSplit);
                hands.add(index + 1, secondSplit);

                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a hand at a given index can be split.
     * @param index The index of the hand to check.
     * @return True if the hand can be split, false otherwise.
     */
    public Boolean canSplit(int index) {
        if (index >= 0 && index < hands.size()) {
            Hand hand = hands.get(index);
            int firstCardValue = hand.getCards().get(0).getRank().getValue();
            int secondCardValue = hand.getCards().get(1).getRank().getValue();

            return hand.getSize() == 2 && firstCardValue == secondCardValue;
        }

        return false;
    }

    /**
     * Retrieves the list of all hands held by the player.
     * @return The list of hands.
     */
    public List<Hand> getHands() {
        return hands;
    }

    /**
     * Retrieves the number of hands the player currently holds.
     * @return The number of hands.
     */
    public int getNumHands() {
        return hands.size();
    }

    /**
     * Creates a deep copy of the player.
     * @return A new Player object with cloned hands.
     */
    @Override
    public Player clone() {
        List<Hand> clonedHands = new ArrayList<>();

        for (Hand hand : this.hands) {
            clonedHands.add(hand.clone());
        }

        return new Player(clonedHands);
    }

    /**
     * Provides a string representation of all hands held by the player, listing each hand on a new line.
     * @return A string detailing each hand held by the player.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int handIndex = 0; handIndex < hands.size(); handIndex++) {
            Hand currHand = hands.get(handIndex);
            output.append("Hand ").append(handIndex + 1).append(": ").append(currHand);

            if (handIndex < hands.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }
}