package henrycaldwell.model;

/**
 * Represents a card with a specific rank in a deck of playing cards.
 */
public class Card {

	/**
     * Enumeration of card ranks with their respective names and values typically found in a standard deck of cards.
     */
    public enum Rank {

        ACE("Ace", "A", 1),
        TWO("Two", "2", 2),
        THREE("Three", "3", 3),
        FOUR("Four", "4", 4),
        FIVE("Five", "5", 5),
        SIX("Six", "6", 6),
        SEVEN("Seven", "7", 7),
        EIGHT("Eight", "8", 8),
        NINE("Nine", "9", 9),
        TEN("Ten", "10", 10),
        JACK("Jack", "J", 10),
        QUEEN("Queen", "Q", 10),
        KING("King", "K", 10);

        private final String name; // The name of the card rank.
        private final String abbreviation; // The abbreviation of the card rank.
        private final int value; // The numeric value of the card rank.

        /**
         * Constructs a rank with the specified value and name.
         * @param value Numeric value associated with the card rank.
         * @param name Name of the card rank.
         */
        private Rank(String name, String abbreviation, int value) {
            this.name = name;
            this.abbreviation = abbreviation;
            this.value = value;
        }

        /**
         * Returns the Rank enum corresponding to the given abbreviation, or null if not found.
         * @param abbreviation The abbreviation of the rank.
         * @return The corresponding Rank enum, or null if not found.
         */
        public static Rank fromAbbreviation(String abbreviation) {
            for (Rank rank : Rank.values()) {
                if (rank.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                    return rank;
                }
            }

            return null;
        }

        /**
         * Returns the Rank enum corresponding to the given value, or null if not found.
         * @param value The numeric value of the rank.
         * @return The corresponding Rank enum, or null if not found.
         */
        public static Rank fromValue(int value) {
            for (Rank rank : Rank.values()) {
                if (rank.getValue() == value) {
                    return rank;
                }
            }

            return null;
        }

        /**
         * Retrieves the name of the card rank.
         * @return The name of the rank.
         */
        public String getName() {
            return name;
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
    }

	private final Rank rank; // The rank of the card.

    /**
     * Constructs a card with the specified rank.
     * @param rank The rank of the card.
     */
    public Card(Rank rank) {
        this.rank = rank;
    }

    /**
     * Retrieves the rank of the card.
     * @return The rank.
     */
    public Rank getRank() {
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