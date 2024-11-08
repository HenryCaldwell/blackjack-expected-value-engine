package henrycaldwell.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import henrycaldwell.view.ConsoleUtil;

/**
 * Manages the configuration and game rules for Blackjack.
 * Loads game rules from a configuration file and provides static variables
 * for use throughout the application.
 */
public class GameRules {

  public static int NUMBER_OF_DECKS; // The number of standard decks used in the game (restricted to 1 - 8).
  public static double BLACKJACK_ODDS; // The payout ratio for a player blackjack (can not be less than 0).
  public static boolean SURRENDER; // Determines if the player is allowed to surrender as their first move.

  public static boolean DEALER_HITS_ON_SOFT_17; // Determines if the dealer hits on a soft 17.
  public static boolean DEALER_PEAKS_FOR_21; // Determines if the dealer checks for blackjack with an ace or ten card.
  public static boolean DEALER_ALWAYS_PLAYS_OUT; // Determines if the dealer plays out their hand if all player hands
                                                 // are resolved.

  public static boolean NATURAL_BLACKJACK_SPLITS; // Determines if a split hand being dealt a natural 21 is considered
                                                  // blackjack.
  public static boolean DOUBLE_AFTER_SPLIT; // Determines if the player is allowed to double after splitting.
  public static boolean HIT_SPLIT_ACES; // Determines if the player is allowed to hit after splitting aces.
  public static boolean DOUBLE_SPLIT_ACES; // Determines if the player is allowed to double after splitting aces.
                                           // Note: HIT_SPLIT_ACES and DOUBLE_AFTER_SPLIT must be true.

  /**
   * Initializes game constants and rules by loading them from a configuration
   * file.
   */
  static {
    loadGameRules();
  }

  /**
   * Loads game rules from the 'game_rules.txt' file in the resources directory.
   * If the file cannot be found or read, default values are used.
   * The method also handles invalid formats by setting default values.
   */
  public static void loadGameRules() {
    Map<String, String> rules = new HashMap<>();

    try (InputStream inputStream = GameRules.class.getClassLoader().getResourceAsStream("game_rules.txt")) {
      if (inputStream == null) {
        System.out.println(
            ConsoleUtil.colorText("ERROR: Game rules file not found. Using default settings.", ConsoleUtil.ANSI_RED));
        setDefaultRules();
        return;
      }

      try (Scanner fileScanner = new Scanner(inputStream)) {
        while (fileScanner.hasNextLine()) {
          String line = fileScanner.nextLine().trim();

          if (line.isEmpty() || line.startsWith("#")) {
            continue; // Skip empty lines and comments
          }

          if (line.contains("=")) {
            String[] parts = line.split("=", 2);

            if (parts.length == 2) {
              rules.put(parts[0].trim(), parts[1].trim());
            }
          }
        }
      }
    } catch (IOException e) {
      System.out.println(
          ConsoleUtil.colorText("ERROR: Unable to read game rules. Using default settings.", ConsoleUtil.ANSI_RED));
      setDefaultRules();
      return;
    }

    try {
      NUMBER_OF_DECKS = Integer.parseInt(rules.getOrDefault("NUMBER_OF_DECKS", "6"));
      BLACKJACK_ODDS = Double.parseDouble(rules.getOrDefault("BLACKJACK_ODDS", "1.5"));
      SURRENDER = Boolean.parseBoolean(rules.getOrDefault("SURRENDER", "true"));
      DEALER_HITS_ON_SOFT_17 = Boolean.parseBoolean(rules.getOrDefault("DEALER_HITS_ON_SOFT_17", "true"));
      DEALER_PEAKS_FOR_21 = Boolean.parseBoolean(rules.getOrDefault("DEALER_PEAKS_FOR_21", "true"));
      DEALER_ALWAYS_PLAYS_OUT = Boolean.parseBoolean(rules.getOrDefault("DEALER_ALWAYS_PLAYS_OUT", "false"));
      DOUBLE_AFTER_SPLIT = Boolean.parseBoolean(rules.getOrDefault("DOUBLE_AFTER_SPLIT", "true"));
      HIT_SPLIT_ACES = Boolean.parseBoolean(rules.getOrDefault("HIT_SPLIT_ACES", "false"));
      DOUBLE_SPLIT_ACES = Boolean.parseBoolean(rules.getOrDefault("DOUBLE_SPLIT_ACES", "false"));
      NATURAL_BLACKJACK_SPLITS = Boolean.parseBoolean(rules.getOrDefault("NATURAL_BLACKJACK_SPLITS", "false"));
    } catch (NumberFormatException e) {
      System.out.println(
          ConsoleUtil.colorText("ERROR: Invalid format in game rules. Using default settings.", ConsoleUtil.ANSI_RED));
      setDefaultRules();
    }

    validateRules();
  }

  /**
   * Sets default game rules in case the configuration file cannot be read or
   * contains invalid values.
   */
  private static void setDefaultRules() {
    NUMBER_OF_DECKS = 6;
    BLACKJACK_ODDS = 1.5;
    SURRENDER = true;
    DEALER_HITS_ON_SOFT_17 = true;
    DEALER_PEAKS_FOR_21 = true;
    DEALER_ALWAYS_PLAYS_OUT = false;
    DOUBLE_AFTER_SPLIT = true;
    HIT_SPLIT_ACES = false;
    DOUBLE_SPLIT_ACES = false;
    NATURAL_BLACKJACK_SPLITS = false;
  }

  /**
   * Validates the loaded game rules and adjusts any values that are out of
   * acceptable ranges.
   * Provides warnings to the user if adjustments are made.
   */
  private static void validateRules() {
    if (NUMBER_OF_DECKS < 1 || NUMBER_OF_DECKS > 8) {
      System.out.println(ConsoleUtil.colorText(
          "WARNING: NUMBER_OF_DECKS out of valid range (1-8). Using default value (6).", ConsoleUtil.ANSI_YELLOW));
      NUMBER_OF_DECKS = 6;
    }

    if (BLACKJACK_ODDS <= 0) {
      System.out.println(ConsoleUtil.colorText("WARNING: BLACKJACK_ODDS must be positive. Using default value (1.5).",
          ConsoleUtil.ANSI_YELLOW));
      BLACKJACK_ODDS = 1.5;
    }

    if (DOUBLE_SPLIT_ACES) {
      if (!HIT_SPLIT_ACES || !DOUBLE_AFTER_SPLIT) {
        System.out.println(ConsoleUtil.colorText(
            "WARNING: DOUBLE_SPLIT_ACES is true, but HIT_SPLIT_ACES and DOUBLE_AFTER_SPLIT must also be true. Adjusting HIT_SPLIT_ACES and DOUBLE_AFTER_SPLIT to be true.",
            ConsoleUtil.ANSI_YELLOW));
        HIT_SPLIT_ACES = true;
        DOUBLE_AFTER_SPLIT = true;
      }
    }
  }
}
