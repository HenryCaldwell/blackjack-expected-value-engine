package henrycaldwell;

/**
 * Utility class for coloring text in console output.
 */
public class ConsoleUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GRAY = "\u001B[90m";

    /**
     * Applies ANSI color codes to a given text.
     * @param text The text to color.
     * @param color The ANSI color code to apply.
     * @return The colored text string.
     */
    public static String colorText(String text, String color) {
        return color + text + ANSI_RESET;
    }
}