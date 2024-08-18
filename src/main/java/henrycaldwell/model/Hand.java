package henrycaldwell.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hand of playing cards.
 */
public class Hand {

    private Deck deck; // The deck from which cards are drawn or returned.
    private List<Card> cards; // The cards currently in the hand.

    /**
     * Constructs a Hand associated with a specific deck.
     * @param deck The deck to associate with the hand.
     */
    public Hand(Deck deck) {
        this.deck = deck;
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand if the card is not null.
     * @param card The card to be added.
     * @return True if the card was successfully added, false if the card is null.
     */
    public Boolean add(Card card) {
        if (card != null) {
            cards.add(card);
            return true;
        }

        return false;
    }

    /**
     * Removes a card from the hand if it matches the specified card.
     * @param compCard The card to be compared and removed.
     * @return True if the card was found and removed, false otherwise.
     */
    public Boolean remove(Card compCard) {
        for (Card card : cards) {
            if (card.getRank().equals(compCard.getRank())) {
                cards.remove(card);
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a card to the hand and removes the same from the deck, if the card is present in the deck.
     * @param card The card to be transferred from the deck to the hand.
     * @return True if the transfer was successful, false otherwise.
     */
    public Boolean addDrop(Card card) {
        if (card != null && deck.contains(card)) {
            add(card);
            deck.remove(card);
            return true;
        }

        return false;
    }

    /**
     * Removes a card from the hand and adds it back to the deck, if the card is present in the hand.
     * @param compCard The card to be transferred from the hand to the deck.
     * @return True if the transfer was successful, false otherwise.
     */
    public Boolean dropAdd(Card compCard) {
        if (compCard != null && contains(compCard)) {
            remove(compCard);
            deck.add(compCard);
            return true;
        }
        
        return false;
    }

    /**
     * Checks if a specific card is in the hand.
     * @param compCard The card to be compared and checked for.
     * @return True if the card is in the hand, false otherwise.
     */
    private Boolean contains(Card compCard) {
        for(Card card : cards) {
            if(card.getRank().equals(compCard.getRank())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Evaluates and returns the total value of the cards in the hand, considering the special nature of aces.
     * @return The total value of the hand.
     */
    public int evaluateHand() {
	    int totalValue = 0;
	    boolean hasAce = false;

	    for (Card card : cards) {
	        int cardValue = card.getRank().getValue();
	        totalValue += cardValue;

	        if (card.getRank().equals(Card.Rank.ACE)) {
	            hasAce = true;
	        }
	    }

	    if (hasAce && totalValue <= 11) {
	        totalValue += 10;
	    }

	    return totalValue;
	}

    /**
     * Determines if the hand is a 'soft' hand, which means it includes an ace counted as 11 without busting.
     * @return True if the hand is soft, false otherwise.
     */
    public Boolean isSoftHand() {
        int totalValue = 0;
	    boolean hasAce = false;

	    for (Card card : cards) {
	        int cardValue = card.getRank().getValue();
	        totalValue += cardValue;

	        if (card.getRank().equals(Card.Rank.ACE)) {
	            hasAce = true;
	        }
	    }

        return hasAce && totalValue + 10 <= 21;
    }

    /**
     * Retrieves the deck associated with the hand.
     * @return The deck.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Retrieves a list of cards in the hand.
     * @return The list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets the Hand's cards to the specified list of cards.
     * @param cards The list of cards.
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    
    /**
     * Retrieves the number of cards in the hand.
     * @return The size of the hand.
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Creates a deep copy of the hand.
     * @return A new hand object with the same cards.
     */
    @Override
    public Hand clone() {
        Hand clone = new Hand(deck);

        for (Card card : cards) {
            clone.add(card);
        }

        return clone;
    }

    /**
     * Provides a string representation of the hand, listing all cards and the total value of the hand.
     * @return A string summarizing the hand.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int cardIndex = 0; cardIndex < cards.size(); cardIndex++) {
            Card currCard = cards.get(cardIndex);
            output.append(currCard);

            if (cardIndex < cards.size() - 1) {
                output.append(", ");
            }
        }

        output.append(" - ").append(evaluateHand()).append(" Total");
        return output.toString();
    }
}