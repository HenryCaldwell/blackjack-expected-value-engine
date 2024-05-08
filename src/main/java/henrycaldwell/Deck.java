package henrycaldwell;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a deck of playing cards, capable of shuffling and managing cards.
 */
public class Deck {
	
	private List<Card> cards; // The list holding the cards in the deck.
    private int count; // The running count of the deck for card counting strategies.
	
    /**
     * Constructs a deck and automatically shuffles it to prepare for a game.
     */
	public Deck() {
        this.cards = new ArrayList<>();
		shuffle();
	}
    
    /**
     * Shuffles the deck by refilling it with a standard set of cards and then randomly mixing them.
     */
	public void shuffle() {
        cards.clear();

        for (int currDeck = 0; currDeck < GameRules.NUMBER_OF_DECKS; currDeck++) {
            for (int currSuit = 0; currSuit < 4; currSuit++) {
                for (Card.Ranks currRank : Card.Ranks.values()) {
                    cards.add(new Card(currRank));
                }
            }
        }

        Collections.shuffle(cards);
    }

    /**
     * Adds a card to the deck if the card is not null.
     * @param card The card to be added.
     * @return True if the card was added, false otherwise.
     */
    public boolean add(Card card) {
        if (card != null) {
            cards.add(card);
            return true;
        }

        return false;
    }

    /**
     * Removes a card from the deck that matches the specified card.
     * @param compCard The card to be removed.
     * @return True if the card was found and removed, false otherwise.
     */
    public boolean remove(Card compCard) {
        for (Card card : cards) {
            int value = card.getRank().getValue();

            if (card.getRank().equals(compCard.getRank())) {
                if (value == 1 || value == 10) {
                    count++;
                } else if (value >= 2 && value <= 6) {
                    count--;
                }

                cards.remove(card);
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a specified card is in the deck.
     * @param compCard The card to check for.
     * @return True if the card is in the deck, false otherwise.
     */
    public boolean contains(Card compCard) {
        for(Card card : cards) {
            if(card.getRank().equals(compCard.getRank())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the list of cards in the deck.
     * @return The list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Retrieves the number of cards in the deck.
     * @return The number of cards.
     */
	public int getSize() {
		return cards.size();
	}
	
    /**
     * Calculates and retrieves the current count of the deck adjusted by the number of decks used, useful for card counting strategies.
     * @return The true count value.
     */
    public double getCount() {
        return count / (cards.size() / 52.0);
    }

    /**
     * Provides a string representation of the deck, listing all cards.
     * @return A string listing all cards in the deck.
     */
    @Override
	public String toString() {
        StringBuilder deckOutput = new StringBuilder();
        
        for (Card card : cards) {
            deckOutput.append(card).append("\n");
        }

        return deckOutput.toString();
    }
}