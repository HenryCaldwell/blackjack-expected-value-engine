package henrycaldwell;

/**
 * Represents a card with a specific rank in a deck of playing cards.
 */
public class Card {

	/**
     * Enumeration of card ranks with their respective names and values typically found in a standard deck of cards.
     */
    public enum Ranks {

        ACE("A", 1, "Ace"),
        TWO("2", 2, "Two"),
        THREE("3", 3, "Three"),
        FOUR("4", 4, "Four"),
        FIVE("5", 5, "Five"),
        SIX("6", 6, "Six"),
        SEVEN("7", 7, "Seven"),
        EIGHT("8", 8, "Eight"),
        NINE("9", 9, "Nine"),
        TEN("10", 10, "Ten"),
        JACK("J", 10, "Jack"),
        QUEEN("Q", 10, "Queen"),
        KING("K", 10, "King");

        private final String abbreviation; // The abbreviation of the card rank.
        private final int value; // The numeric value of the card rank.
        private final String name; // The name of the card rank.

        /**
         * Constructs a rank with the specified value and name.
         * @param value Numeric value associated with the card rank.
         * @param name Name of the card rank.
         */
        private Ranks(String abbreviation, int value, String name) {
            this.abbreviation = abbreviation;
            this.value = value;
            this.name = name;
        }

        /**
         * Returns the Ranks enum corresponding to the given abbreviation, or null if not found.
         * @param abbreviation The abbreviation of the rank.
         * @return The corresponding Ranks enum, or null if not found.
         */
        public static Ranks fromAbbreviation(String abbreviation) {
            for (Ranks rank : Ranks.values()) {
                if (rank.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                    return rank;
                }
            }

            return null;
        }

        /**
         * Returns the Ranks enum corresponding to the given value, or null if not found.
         * @param value The numeric value of the rank.
         * @return The corresponding Ranks enum, or null if not found.
         */
        public static Ranks fromValue(int value) {
            for (Ranks rank : Ranks.values()) {
                if (rank.getValue() == value) {
                    return rank;
                }
            }

            return null;
        }

        /**
         * Retrieves the abbreviation of the card rank.
         * @return The abbreviation.
         */
        public String getAbbreviation() {
            return abbreviation;
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