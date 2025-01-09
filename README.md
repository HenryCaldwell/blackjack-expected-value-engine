# Blackjack Prediction Model

This Java program calculates the expected value of any Blackjack hand by simulating every possible outcome against the dealer. It evaluates all potential decisions (hit, stand, double, split, surrender) and computes the player's expected return based on probabilities and outcomes for each possible option. The model uses top-down programming for efficient calculation and is ideal for analyzing optimal strategies or fine-tuning Blackjack play. It provides a clear understanding of long-term profitability for each possible decision in any given hand scenario.

---

## Example Usage

Below is a walkthrough of an example game run using the Blackjack Prediction Model. Each part is explained to clarify how the program operates and what the output means.

---

```
Enter the number of players (e.g., '3' for 3 or '0' to quit):
2
```

**Explanation**: The program starts by prompting the user to specify the number of players. In this case, 2 players will participate in the game.

---

```
CARDS REMAINING IN DECK: 312 Shuffle deck ('Y' or 'N'):
N
```

**Explanation**: This example deck contains 312 cards (6 decks of 52 cards each). The user is asked whether they want to shuffle the deck before starting the game. Here, the user chooses not to shuffle (`N`).

---

```
Player 1: Hand 1: - 0 Total Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Ace
A
```

**Explanation**: The game begins by dealing cards to Player 1. They are prompted to enter the value of their card, which is an Ace (entered as `A`). The `NEXT CARD IN DECK` is based upon the current state of the deck, however the program is designed to be used alongside a real hand. The hand value is updated to 11 (Aces count as 11 initially).

---

```
Player 2: Hand 1: - 0 Total Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Two
2
```

**Explanation**: Player 2 is dealt their first card, which is a 2, and the hand value updates to 2.

---

```
Dealer: Hand: - 0 Total DEALER UP CARD Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
J
```

**Explanation**: The dealer is dealt their face-up card, which is a Jack (entered as `J`). The hand value updates to 10.

---

```
Player 1: Hand 1: Ace - 11 Total Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
A
```

**Explanation**: Player 1 is dealt a second Ace, giving them a total hand value of 12 (Aces are re-evaluated as needed).

---

```
Player 2: Hand 1: Two - 2 Total Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
10
```

**Explanation**: Player 2 is dealt their second card, a 10, which brings their total hand value to 12.

---

```
Dealer: Hand: Jack - 10 Total
DEALER DOWN CARD ('?' IF UNKNOWN)
Enter a card value (e.g., 'K' for King):
Or enter '?' if the card is unknown.
NEXT CARD IN DECK: Three
?
```

**Explanation**: The dealer’s face-down card is unknown, so the user inputs `?` to indicate this.

---

```
Failed to draw a card for the dealer.
```

**Explanation**: The program attempts to draw a card for the dealer but encounters an error or missing card information since we do know what the dealer's down card is.

---

```
Player 1:
Hand 1: Ace, Ace - 12 Total
Player 2:
Hand 1: Two, Ten - 12 Total
Dealer:
Hand: Jack - 10 Total
```

**Explanation**: The current game state is displayed, showing Player 1 with a hand of Ace, Ace (12 total), Player 2 with a hand of Two, Ten (12 total), and the dealer showing a Jack (10 total).

---

```
Player 1:
Hand 1: Ace, Ace - 12 Total
StandEV: -53.96046551563714 %
HitEV: -6.337774091288553 %
DoubleEV: -50.34951514298304 %
SplitEV: 18.032643521751024 %
SurrenderEV: -50.0 %
```

**Explanation**: The program calculates the expected value (EV) for each possible action Player 1 can take:

- **StandEV**: Expected loss of 53.96%.
- **HitEV**: Expected loss of 6.34%.
- **DoubleEV**: Expected loss of 50.35%.
- **SplitEV**: Expected gain of 18.03%.
- **SurrenderEV**: Expected loss of 50%.

---

```
Choose an action ('Hit', 'Stand', 'Double', 'Split', 'Surrender'):
True count: 0.50814332247557
split
```

**Explanation**: The program calculates and displays the true count (a card counting metric) at this point in the game, which can influence strategy decisions. Based on the EV calculations, splitting the hand provides the highest expected return (18.03%), so Player 1 chooses to split.

---

```
Player 1:
Hand 1: Ace - 11 Total
Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
Q
```

**Explanation**: After Player 1 chooses to split their hand, the game progresses to play out each of the split hands. Player 1 plays their first split hand, which starts with an Ace (value 11). They are dealt a Queen (entered as `Q`), bringing the total hand value to 21.

---

```
Player 1:
Hand 2: Ace - 11 Total
Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
K
```

**Explanation**: Player 1 then plays their second split hand, which also starts with an Ace (value 11). They are dealt a King (entered as `K`), bringing this hand's total to 21 as well.

---

```
Player 1:
Hand 1 FINAL: Ace, Queen - 21 Total

Player 1:
Hand 2 FINAL: Ace, King - 21 Total
```

**Explanation**: Both of Player 1’s split hands result in a total of 21. These results are displayed as "FINAL" for both hands, indicating that no further actions are required for Player 1.

---

```
Player 2:
Hand 1: Two, Ten - 12 Total
StandEV: -53.9194794795191 %
HitEV: -36.87781606236117 %
DoubleEV: -77.5120784753261 %
SplitEV: N/A
SurrenderEV: -50.0 %
```

**Explanation**: The program calculates the expected value (EV) for each possible action Player 2 can take:

- **StandEV**: Expected loss of 53.91%.
- **HitEV**: Expected loss of 36.88%.
- **DoubleEV**: Expected loss of 77.51%.
- **SplitEV**: Not applicable (N/A) because splitting is not an option with this hand.
- **SurrenderEV**: Expected loss of 50%.

---

```
Choose an action ('Hit', 'Stand', 'Double', 'Split', 'Surrender'):
True count: 0.8524590163934427
hit
```

**Explanation**: Player 2 chooses to hit, based on the EV calculations.

---

```
Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
5
```

**Explanation**: Player 2 is dealt a 5, bringing their total hand value to 17.

---

```
Player 2:
Hand 1: Two, Ten, Five - 17 Total
StandEV: -41.780925186774354 %
HitEV: -58.11988117736302 %
DoubleEV: N/A
SplitEV: N/A
SurrenderEV: N/A
```

**Explanation**: The program recalculates the EV for the new hand:

- **StandEV**: Expected loss of 41.78%.
- **HitEV**: Expected loss of 58.12%.
- **DoubleEV**: Not applicable (N/A) because doubling is not an option with this hand.
- **SplitEV**: Not applicable (N/A) because splitting is not an option with this hand.
- **SurrenderEV**: Not applicable (N/A) because surrendering is not an option with this hand.

---

```
Choose an action ('Hit', 'Stand', 'Double', 'Split', 'Surrender'):
True count: 0.6842105263157895
stand
```

**Explanation**: Player 2 chooses to stand, based on the EV calculations.

---

```
Player 2:
Hand 1 FINAL: Two, Ten, Five - 17 Total
```

**Explanation**: Player 2's final hand is Two, Ten, and Five, with a total of 17. No further actions are taken for this hand.

---

```
Dealer:
Hand: Jack - 10 Total
Enter a card value (e.g., 'K' for King):
NEXT CARD IN DECK: Three
9
```

**Explanation**: The dealer starts with a face-up Jack (value 10). They draw a 9 (entered as `9`), bringing their total to 19.

---

```
Dealer FINAL: Jack, Nine - 19 Total
```

**Explanation**: The dealer's hand is complete, with a final total of 19.

---

### Final Results for All Hands:

```
Dealer: Jack, Nine - 19 Total
Player 1:
Hand 1: Ace, Queen - 21 Total - PLAYER BLACKJACK

Hand 2: Ace, King - 21 Total - PLAYER BLACKJACK

Player 2:
Hand 1: Two, Ten, Five - 17 Total - PLAYER LOSES
```

**Explanation**:

- **Dealer**: The dealer’s final total is 19.
- **Player 1**: Both of Player 1’s hands result in a total of 21, achieving Blackjack in both cases. They win both hands.
- **Player 2**: Player 2’s hand totals 17, which is less than the dealer’s 19. As a result, Player 2 loses.

**Summary**:  
The final results show that Player 1 wins decisively with two Blackjacks, while Player 2 loses due to their lower hand value compared to the dealer. This demonstrates how the model calculates and evaluates outcomes for each hand, ensuring a clear resolution of the game.

---

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

```
