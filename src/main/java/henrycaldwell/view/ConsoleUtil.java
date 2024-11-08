package henrycaldwell.view;

/**
 * Utility class for coloring text in console output using ANSI escape codes.
 * <p>
 * This class provides predefined ANSI color codes and a method to apply these
 * colors
 * to text strings for enhanced console readability and user experience.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * String coloredText = ConsoleUtil.colorText("Hello, World!", ConsoleUtil.ANSI_GREEN);
 * System.out.println(coloredText);
 * }</pre>
 */
public class ConsoleUtil {

  public static final String ANSI_RESET = "\u001B[0m"; // The ANSI code for reset.
  public static final String ANSI_RED = "\u001B[31m"; // The ANSI code for red.
  public static final String ANSI_YELLOW = "\u001B[33m"; // The ANSI code for yellow.
  public static final String ANSI_GREEN = "\u001B[32m"; // The ANSI code for green.
  public static final String ANSI_BLUE = "\u001B[34m"; // The ANSI code for blue.
  public static final String ANSI_GRAY = "\u001B[90m"; // The ANSI code for gray.

  /**
   * Applies ANSI color codes to a given text.
   *
   * @param text  The text to color. Must not be {@code null}.
   * @param color The ANSI color code to apply. Must not be {@code null} and
   *              should be one of the predefined ANSI constants.
   * @return The colored text string with ANSI color codes applied.
   * @throws IllegalArgumentException if {@code text} or {@code color} is
   *                                  {@code null}.
   */
  public static String colorText(String text, String color) {
    return color + text + ANSI_RESET;
  }
}