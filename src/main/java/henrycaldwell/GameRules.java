package henrycaldwell;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Manages the configuration and game rules for Blackjack.
 */
public class GameRules {

    public static int NUMBER_OF_DECKS; // The number of standard decks used in the game (2 - 8 is conventional).
    public static double BLACKJACK_ODDS; // The odds paid for a player blackjack (Used for predictions).
    public static boolean SURRENDER; // Determines if the player is allowed to surrender as their first move (Used for logic).

    public static boolean DEALER_HITS_ON_SOFT_17; // Determines if the dealer will hit on a soft 17 (Used for both logic and predictions).
    public static boolean DEALER_PEAKS_FOR_21; // Determines if the dealer will check for blackjack with an ace or ten card (Used for both logic and predictions).
    public static boolean DEALER_ALWAYS_PLAYS_OUT; // Determines if the dealer will play out their hand if all player hands have already been resolved (Used for logic).

    public static boolean NATURAL_BLACKJACK_SPLITS; // Determines if a split hand being dealt a natural 21 is considered blackjack (Used for predictions).
    public static boolean DOUBLE_AFTER_SPLIT; // Determines if the player is allowed to double after splitting (Used for predictions).
    public static boolean HIT_SPLIT_ACES; // Determines if the player is allowed to hit after splitting aces (Used for predictions).
    public static boolean DOUBLE_SPLIT_ACES; // ***HIT_SPLIT_ACES & DOUBLE_AFTER_SPLIT MUST BE TRUE*** Determines if the player is allowed to double after splitting aces (Used for predictions).

    /**
     *  Initializes game constants and rules.
     */
    static {
        loadGameRules();
    }

    /**
     * Loads game rules from the game_rules.txt file in the resources directory.
     */
    public static void loadGameRules() {
        Map<String, String> rules = new HashMap<>();

        try (InputStream inputStream = GameRules.class.getClassLoader().getResourceAsStream("game_rules.txt")) {
            if (inputStream == null) {
                System.out.println(ConsoleUtil.colorText("ERROR READING GAME RULES", ConsoleUtil.ANSI_RED));
                System.exit(0);
            }

            try (Scanner fileScanner = new Scanner(inputStream)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }

                    if (line.contains("=")) {
                        String[] parts = line.split("=");

                        if (parts.length == 2) {
                            rules.put(parts[0].trim(), parts[1].trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(ConsoleUtil.colorText("ERROR READING GAME RULES", ConsoleUtil.ANSI_RED));
            System.exit(0);
        }

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
    }
}
