package henrycaldwell.model;

/**
 * Represents a Dealer in a card game.
 */
public class Dealer {

    private Hand hand; // The hand held by the dealer.

    /**
     * Constructs a dealer with no hand;
     */
    public Dealer() {
        this.hand = null;
    }

    /**
     * Constructs a dealer with a predefined hand.
     * @param hand The pre-existing hand to be assigned to the dealer.
     */
    public Dealer(Hand hand) {
        this.hand = hand;
    }

    /**
     * Adds a hand to the dealer if the hand is not null.
     * @param hand The hand to add.
     * @return True if the hand was added successfully, false otherwise.
     */
    public Boolean addHand(Hand hand) {
        if (hand != null) {
            this.hand = hand;
            return true;
        }

        return false;
    }

    /**
     * Retrieves the hand held by the player.
     * @return The hand.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Creates a deep copy of the dealer.
     * @return A new Dealer object with a cloned hand.
     */
    @Override
    public Dealer clone() {
        Hand clonedHand = hand.clone();
        return new Dealer(clonedHand);
    }

    /**
     * Provides a string representation of the hand held by the dealer, listing all cards and the total value of the hand.
     * @return A string detailing the hand held by the dealer.
     */
    @Override
    public String toString() {
        return hand.toString();
    }
}