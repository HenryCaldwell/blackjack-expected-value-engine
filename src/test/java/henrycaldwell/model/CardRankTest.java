package henrycaldwell.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link Card.Rank} enum.
 * <p>
 * This class contains unit tests to verify the correctness of the
 * {@link Card.Rank} enum methods, including parsing abbreviations and values,
 * as well as retrieving properties like name, abbreviation, and numerical
 * value.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>{@code
 * Card.Rank ace = Card.Rank.fromAbbreviation("A");
 * String name = ace.getName(); // "Ace"
 * int value = ace.getValue(); // 1
 * }</pre>
 */
public class CardRankTest {

  // ================================
  // Tests for fromAbbreviation Method
  // ================================

  /**
   * Tests {@link Card.Rank#fromAbbreviation(String)} with valid abbreviations.
   * <p>
   * Scenario: Providing standard abbreviations like "A" for ACE and "2" for TWO.
   * </p>
   * <p>
   * Expected Outcome: Correct {@link Card.Rank} enum values are returned.
   * </p>
   */
  @Test
  public void testFromAbbreviationValid() {
    assertEquals("Abbreviation 'A' should return ACE", Card.Rank.ACE, Card.Rank.fromAbbreviation("A"));
    assertEquals("Abbreviation '2' should return TWO", Card.Rank.TWO, Card.Rank.fromAbbreviation("2"));
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} is case-insensitive.
   * <p>
   * Scenario: Providing lowercase abbreviations like "a" for ACE and "q" for
   * QUEEN.
   * </p>
   * <p>
   * Expected Outcome: Correct {@link Card.Rank} enum values are returned
   * regardless of case.
   * </p>
   */
  @Test
  public void testFromAbbreviationCaseInsensitive() {
    assertEquals("Abbreviation 'a' should return ACE", Card.Rank.ACE, Card.Rank.fromAbbreviation("a"));
    assertEquals("Abbreviation 'q' should return QUEEN", Card.Rank.QUEEN, Card.Rank.fromAbbreviation("q"));
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException} for null input.
   * <p>
   * Scenario: Providing a {@code null} abbreviation.
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromAbbreviationNull() {
    Card.Rank.fromAbbreviation(null);
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException} for invalid abbreviations.
   * <p>
   * Scenario: Providing an invalid abbreviation like "InvalidAbbr".
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromAbbreviationInvalid() {
    Card.Rank.fromAbbreviation("InvalidAbbr");
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException} for empty string input.
   * <p>
   * Scenario: Providing an empty string as abbreviation.
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromAbbreviationEmptyString() {
    Card.Rank.fromAbbreviation("");
  }

  // ================================
  // Tests for fromValue Method
  // ================================

  /**
   * Tests {@link Card.Rank#fromValue(int)} with valid values.
   * <p>
   * Scenario: Providing valid numerical values like 1 for ACE and 10 for TEN.
   * </p>
   * <p>
   * Expected Outcome: Correct {@link Card.Rank} enum values are returned.
   * </p>
   */
  @Test
  public void testFromValueValid() {
    assertEquals("Value 1 should return ACE", Card.Rank.ACE, Card.Rank.fromValue(1));
    assertEquals("Value 10 should return TEN", Card.Rank.TEN, Card.Rank.fromValue(10));
  }

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException} for invalid values.
   * <p>
   * Scenario: Providing an invalid numerical value like 15.
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromValueInvalid() {
    Card.Rank.fromValue(15);
  }

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException} for negative values.
   * <p>
   * Scenario: Providing a negative numerical value like -1.
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromValueNegative() {
    Card.Rank.fromValue(-1);
  }

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException} for zero value.
   * <p>
   * Scenario: Providing zero as the numerical value.
   * </p>
   * <p>
   * Expected Outcome: {@link IllegalArgumentException} is thrown.
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromValueZero() {
    Card.Rank.fromValue(0);
  }

  // ================================
  // Tests for Getter Methods
  // ================================

  /**
   * Tests the {@link Card.Rank#getName()} method.
   * <p>
   * Scenario: Retrieving the name of the card ranks ACE and KING.
   * </p>
   * <p>
   * Expected Outcome: Correct names "Ace" and "King" are returned.
   * </p>
   */
  @Test
  public void testGetName() {
    assertEquals("ACE name should be 'Ace'", "Ace", Card.Rank.ACE.getName());
    assertEquals("KING name should be 'King'", "King", Card.Rank.KING.getName());
  }

  /**
   * Tests the {@link Card.Rank#getAbbreviation()} method.
   * <p>
   * Scenario: Retrieving the abbreviation of the card ranks ACE and QUEEN.
   * </p>
   * <p>
   * Expected Outcome: Correct abbreviations "A" and "Q" are returned.
   * </p>
   */
  @Test
  public void testGetAbbreviation() {
    assertEquals("ACE abbreviation should be 'A'", "A", Card.Rank.ACE.getAbbreviation());
    assertEquals("QUEEN abbreviation should be 'Q'", "Q", Card.Rank.QUEEN.getAbbreviation());
  }

  /**
   * Tests the {@link Card.Rank#getValue()} method.
   * <p>
   * Scenario: Retrieving the numerical value of the card ranks ACE and KING.
   * </p>
   * <p>
   * Expected Outcome: Correct values 1 and 10 are returned.
   * </p>
   */
  @Test
  public void testGetValue() {
    assertEquals("ACE value should be 1", 1, Card.Rank.ACE.getValue());
    assertEquals("KING value should be 10", 10, Card.Rank.KING.getValue());
  }

}
