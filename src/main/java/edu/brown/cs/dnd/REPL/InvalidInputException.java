package edu.brown.cs.dnd.REPL;

/**
 * Class representing an exception thrown when the user enters invalid input.
 */
public class InvalidInputException extends Throwable {
  /**
   * A Constructor for an InvalidInputException.
   * @param s   A String that is the error message
   */
  public InvalidInputException(String s) {
    super(s);
  }
}
