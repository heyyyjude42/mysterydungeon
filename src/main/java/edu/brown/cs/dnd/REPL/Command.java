package edu.brown.cs.dnd.REPL;

import edu.brown.cs.dnd.Data.Result;

/**
 * Interface representing a command.
 */
public interface Command {
  /**
   * Method runs the command.
   * @param args    A String array that are the args of the command
   * @return    A Result object that is the result of the command
   * @throws InvalidInputException    Thrown when user inputs invalid args
   * @throws CommandFailedException   Thrown when the command fails
   */
  Result run(String[] args)
          throws InvalidInputException, CommandFailedException;
}
