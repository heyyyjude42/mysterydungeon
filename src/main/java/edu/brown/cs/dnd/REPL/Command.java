package edu.brown.cs.dnd.REPL;

import edu.brown.cs.dnd.Data.Result;

public interface Command {
  Result run(String[] args) throws InvalidInputException, CommandFailedException;
}
