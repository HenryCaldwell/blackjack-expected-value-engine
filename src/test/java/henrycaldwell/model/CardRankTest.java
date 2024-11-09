package henrycaldwell.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link Card.Rank} enum.
 */
public class CardRankTest {

  // ================================
  // Tests for fromAbbreviation Method
  // ================================

  /**
   * Tests {@link Card.Rank#fromAbbreviation(String)} with valid abbreviations.
   */
  @Test
  public void testFromAbbreviationValid() {
    assertEquals("Abbreviation 'A' should return ACE", Card.Rank.ACE, Card.Rank.fromAbbreviation("A"));
    assertEquals("Abbreviation '2' should return TWO", Card.Rank.TWO, Card.Rank.fromAbbreviation("2"));
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} is case-insensitive.
   */
  @Test
  public void testFromAbbreviationCaseInsensitive() {
    assertEquals("Abbreviation 'a' should return ACE", Card.Rank.ACE, Card.Rank.fromAbbreviation("a"));
    assertEquals("Abbreviation 'q' should return QUEEN", Card.Rank.QUEEN, Card.Rank.fromAbbreviation("q"));
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException}
   * for null input.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromAbbreviationNull() {
    Card.Rank.fromAbbreviation(null);
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException}
   * for invalid abbreviations.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromAbbreviationInvalid() {
    Card.Rank.fromAbbreviation("InvalidAbbr");
  }

  /**
   * Tests that {@link Card.Rank#fromAbbreviation(String)} throws
   * {@link IllegalArgumentException}
   * for empty string input.
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
   */
  @Test
  public void testFromValueValid() {
    assertEquals("Value 1 should return ACE", Card.Rank.ACE, Card.Rank.fromValue(1));
    assertEquals("Value 10 should return TEN", Card.Rank.TEN, Card.Rank.fromValue(10));
  }

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException}
   * for invalid values.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromValueInvalid() {
    Card.Rank.fromValue(15);
  }

  // Uncomment the following tests if you intend to include them.

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException}
   * for negative values.
   * <p>
   * Expected: {@link IllegalArgumentException}
   * </p>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFromValueNegative() {
    Card.Rank.fromValue(-1);
  }

  /**
   * Tests that {@link Card.Rank#fromValue(int)} throws
   * {@link IllegalArgumentException}
   * for zero value.
   * <p>
   * Expected: {@link IllegalArgumentException}
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
   */
  @Test
  public void testGetName() {
    assertEquals("ACE name should be 'Ace'", "Ace", Card.Rank.ACE.getName());
    assertEquals("KING name should be 'King'", "King", Card.Rank.KING.getName());
  }

  /**
   * Tests the {@link Card.Rank#getAbbreviation()} method.
   */
  @Test
  public void testGetAbbreviation() {
    assertEquals("ACE abbreviation should be 'A'", "A", Card.Rank.ACE.getAbbreviation());
    assertEquals("QUEEN abbreviation should be 'Q'", "Q", Card.Rank.QUEEN.getAbbreviation());
  }

  /**
   * Tests the {@link Card.Rank#getValue()} method.
   */
  @Test
  public void testGetValue() {
    assertEquals("ACE value should be 1", 1, Card.Rank.ACE.getValue());
    assertEquals("KING value should be 10", 10, Card.Rank.KING.getValue());
  }

}
