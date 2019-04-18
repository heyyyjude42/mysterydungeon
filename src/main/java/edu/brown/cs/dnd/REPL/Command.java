package edu.brown.cs.dnd.REPL;

public interface Command {
  String run(String[] args) throws InvalidInputException, CommandFailedException;
}
