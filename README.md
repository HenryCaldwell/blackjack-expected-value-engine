# Blackjack Prediction Model

This repository contains a Java implementation of a simplified Blackjack game, featuring multiple classes that work together to simulate the gameplay, including card handling, and player interactions, as well as a model that is able to correctly determine the best course of play based on the remaining composition of the deck.

## Classes Overview

### `Card`

The `Card` class represents a playing card with a specific rank and includes methods to retrieve the rank's value and name.

#### Attributes
- `rank`: Enumerated type representing card ranks like Ace, King, Queen, etc.

#### Methods
- `getRank()`: Returns the rank of the card.
- `toString()`: Provides a string representation of the card's rank.

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

This setup provides a structured approach to simulating a Blackjack game, with clear responsibilities assigned to each class, facilitating easy maintenance and potential future enhancements.