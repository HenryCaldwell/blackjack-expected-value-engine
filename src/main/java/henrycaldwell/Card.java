package henrycaldwell;

/**
 * Represents a card with a specific rank in a deck of playing cards.
 */
public class Card {

	/**
     * Enumeration of card ranks with their respective names and values typically found in a standard deck of cards.
     */
    public enum Ranks {

        ACE(1, "Ace"),
        TWO(2, "Two"),
        THREE(3, "Three"),
        FOUR(4, "Four"),
        FIVE(5, "Five"),
        SIX(6, "Six"),
        SEVEN(7, "Seven"),
        EIGHT(8, "Eight"),
        NINE(9, "Nine"),
        TEN(10, "Ten"),
        JACK(10, "Jack"),
        QUEEN(10, "Queen"),
        KING(10, "King");

        private final int value; // The numeric value of the card rank.
        private final String name; // The name of the card rank.

        /**
         * Constructs a rank with the specified value and name.
         * @param value Numeric value associated with the card rank.
         * @param name Name of the card rank.
         */
        private Ranks(int value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * Retrieves the numeric value of the card rank.
         * @return The numeric value.
         */
        public int getValue() {
            return value;
        }

        /**
         * Retrieves the name of the card rank.
         * @return The name of the rank.
         */
        public String getName() {
            return name;
        }
    }

	private final Ranks rank; // The rank of the card.

    /**
     * Constructs a card with the specified rank.
     * @param rank The rank of the card.
     */
    public Card(Ranks rank) {
        this.rank = rank;
    }

    /**
     * Retrieves the rank of the card.
     * @return The rank.
     */
    public Ranks getRank() {
        return rank;
    }

    /**
     * Provides a string representation of the card, indicating its rank.
     * @return A string representing the card's rank.
     */
    @Override
    public String toString() {
        return rank.getName();
    }
}