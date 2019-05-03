package edu.brown.cs.dnd.Data;

/**
 * Class representing a string resuilt of a query.
 */
public class StringResult implements QueryResult {
  private String result;

  /**
   * A Constructor for a StringResult.
   * @param result    A String that is the result
   */
  public StringResult(String result) {
    this.result = result;
  }

  @Override
  public String prettify() {
    return result;
  }

  @Override
  public String simplify() {
    return result;
  }

  /**
   * Method returns the string result.
   * @return    A String that is the string result
   */
  public String getResult() {
    return result;
  }
}
