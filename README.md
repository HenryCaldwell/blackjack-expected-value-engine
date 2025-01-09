# Blackjack Prediction Model

This Java program calculates the expected value of any Blackjack hand by simulating every possible outcome against the dealer. It evaluates all potential decisions (hit, stand, double, split, surrender) and computes the player's expected return based on probabilities and outcomes for each possible option. The model uses top-down programming for efficient calculation and is ideal for analyzing optimal strategies or fine-tuning Blackjack play. It provides a clear understanding of long-term profitability for each possible decision in any given hand scenario.

## Classes Overview

### `Card`

The `Card` class represents a playing card with a specific rank and includes methods to retrieve the rank's value and name.

#### Attributes
- `rank`: Enumerated type representing card ranks like Ace, King, Queen, etc.

#### Methods
- `getRank()`: Returns the rank of the card.
- `toString()`: Provides a string representation of the card's rank.
- `Ranks.fromAbbreviation(String abbreviation)`: Returns the `Ranks` enum corresponding to the given abbreviation, or `null` if not found.
- `Ranks.fromValue(int value)`: Returns the `Ranks` enum corresponding to the given value, or `null` if not found.
- `Ranks.getAbbreviation()`: Retrieves the abbreviation of the card rank.
- `Ranks.getValue()`: Retrieves the numeric value of the card rank.
- `Ranks.getName()`: Retrieves the name of the card rank.

### `ConsoleUtil`

The `ConsoleUtil` class provides utility methods to color text in the console, enhancing the game's readability and user experience.

#### Methods
- `colorText(String text, String color)`: Applies ANSI color codes to the given text.

### `Dealer`

The `Dealer` class represents the dealer in the game, managing the dealer's hand.

#### Attributes
- `hand`: Represents the hand of cards held by the dealer.

#### Methods
- `addHand(Hand hand)`: Adds a hand to the dealer.
- `getHand()`: Retrieves the hand held by the dealer.
- `clone()`: Creates a deep copy of the dealer.
- `toString()`: Provides a detailed string representation of the dealer's hand.

### `Deck`

The `Deck` class encapsulates a deck of playing cards, supporting operations like shuffling and card management.

#### Attributes
- `cards`: List of `Card` objects representing the deck.
- `count`: Running count of the deck for card counting strategies.

#### Methods
- `shuffle()`: Shuffles the cards in the deck.
- `add(Card card)`, `remove(Card compCard)`: Manages cards in the deck.
- `contains(Card compCard)`: Checks if a card is present in the deck.
- `getCards()`, `getSize()`, `getCount()`, `toString()`: Various utilities and representations for the deck.

### `GameEngine`

The `GameEngine` class is the core controller for the Blackjack game, handling game initialization, player interactions, and the main game loop.

#### Attributes
- `deck`: The game's deck of cards.
- `pred`: Prediction model used for calculating expected returns.
- `scanner`: Scanner for reading user input.

#### Methods
- `startGame()`: Starts and manages the game loop.
- `promptNumberOfPlayers(boolean nullPossible)`, `promptShuffle()`, `promptCard()`: Handle various user prompts.
- `startRound(int initialPlayers)`: Manages the setup and gameplay for a single round.
- `processRound(Round currRound)`: Processes actions during a round.
- `processPlayerTurn(Round currRound, int playerIndex, int handIndex)`: Manages a player's turn.
- `processDealerTurn(Round currRound)`: Manages the dealer's turn.
- `evaluatePredictions(Hand playerHand, Hand dealerHand)`: Calculates and displays expected values for player actions.

### `Hand`

The `Hand` class represents a hand of playing cards, including methods to manage the cards and evaluate the hand's value.

#### Attributes
- `deck`: The deck associated with the hand.
- `cards`: List of cards in the hand.

#### Methods
- `add(Card card)`, `remove(Card compCard)`, `addDrop(Card card)`, `dropAdd(Card compCard)`: Manage cards within the hand.
- `contains(Card compCard)`: Checks for the presence of a card.
- `evaluateHand()`, `isSoftHand()`: Evaluate the value of the hand.
- `getCards()`, `getSize()`, `clone()`, `toString()`: Various utilities and representations for the hand.
- `setCards(List<Card> cards)`: Sets the Hand's cards.

### `Player`

The `Player` class represents a player in the game, managing one or more hands of cards.

#### Attributes
- `hands`: List of hands held by the player.

#### Methods
- `addHand(Hand hand)`, `removeHand(int index)`, `splitHand(int index)`: Manage the player's hands.
- `canSplit(int index)`: Checks if a hand can be split.
- `getHands()`, `getNumHands()`, `clone()`, `toString()`: Various utilities and representations for the player's hands.

### `PredictionModel`

The `PredictionModel` class calculates the expected values for various actions in the game based on the current state of the deck and the hands.

#### Attributes
- `deck`: Deck associated with the predictions.
- `memoizationCache`: Stores previously calculated outcomes to optimize performance.

#### Methods
- `calculateCardCounts()`, `calculateStandEV(Hand playerHand, Hand dealerHand, boolean isSplit)`, `calculateHitEV(Hand playerHand, Hand dealerHand, boolean isSplit)`, `calculateDoubleEV(Hand playerHand, Hand dealerHand, boolean isSplit)`, `calculateSplitEV(Hand playerHand, Hand dealerHand)`, `calculateSurrenderEV(Hand playerHand, Hand dealerHand)`: Calculate expected values for different actions.
- `evaluateOutcome(Hand playerHand, Hand dealerHand, boolean isSplit)`: Evaluates the outcome of a hand comparison between the player and dealer.

### `Round`

The `Round` class represents a single round in the Blackjack game, managing the players, dealer, and their hands.

#### Attributes
- `players`: List of players in the round.
- `dealer`: Dealer for the round.

#### Methods
- `generateHands(Deck deck, int initialPlayers)`: Initializes players and hands for the round.
- `allPlayerHandsResolved()`: Checks if all player hands are resolved.
- `getPlayers()`, `getDealer()`, `getNumPlayers()`, `toString()`: Various utilities and representations for the round.

## Configuration

The game rules are configurable through a `game_rules.txt` file located in the `src/main/resources` directory. This file allows you to adjust various game constants and settings. 

Below is an example of the default file contents:

```plaintext
###########################################################
#
#  Number of decks used
#  The number of standard decks used in the game (2 - 8 for conventional casino rules)
#

NUMBER_OF_DECKS = 6

###########################################################
#
#  Allow surrender
#  Determines if the player is allowed to surrender as their first move (Used for logic)
#

SURRENDER = true

###########################################################
#
#  Dealer hits soft 17
#  Determines if the dealer will hit on a soft 17 (Used for both logic and predictions)
#

DEALER_HITS_ON_SOFT_17 = true

###########################################################
#
#  Dealer peaks for natural blackjack
#  Determines if the dealer will check for blackjack with an ace or ten card (Used for both logic and predictions)
#

DEALER_PEAKS_FOR_21 = true

###########################################################
#
#  Dealer plays out hand
#  Determines if the dealer will play out their hand if all player hands have already been resolved (Used for logic)
#

DEALER_ALWAYS_PLAYS_OUT = false

###########################################################
#
#  Allow natural blackjack after splitting
#  Determines if a split hand being dealt a natural 21 is considered blackjack (Used for predictions)
#

NATURAL_BLACKJACK_SPLITS = false

###########################################################
#
#  Allow doubling after splitting
#  Determines if the player is allowed to double after splitting (Used for predictions)
#

DOUBLE_AFTER_SPLIT = true

###########################################################
#
#  Allow hitting after splitting aces
#  Determines if the player is allowed to hit after splitting aces (Used for predictions)
#

HIT_SPLIT_ACES = false

###########################################################
#
#  Allow doubling after splitting aces
#  ***HIT_SPLIT_ACES & DOUBLE_AFTER_SPLIT MUST BE TRUE*** Determines if the player is allowed to double after splitting aces (Used for predictions)
#

DOUBLE_SPLIT_ACES = false
