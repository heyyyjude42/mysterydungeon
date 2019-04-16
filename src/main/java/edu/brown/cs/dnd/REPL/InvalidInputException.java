package edu.brown.cs.dnd.REPL;

public class InvalidInputException extends Throwable {
  public InvalidInputException(String s) {
    super(s);
  }
}
