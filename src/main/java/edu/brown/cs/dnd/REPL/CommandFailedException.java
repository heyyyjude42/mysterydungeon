package edu.brown.cs.dnd.REPL;

/**
 * Class representing an exception thrown when the command fails.
 */
public class CommandFailedException extends Throwable {
  /**
   * A Constructor for a CommandFailedException.
   * @param s   A String that is the error message
   */
  public CommandFailedException(String s) {
    super(s);
  }
}
