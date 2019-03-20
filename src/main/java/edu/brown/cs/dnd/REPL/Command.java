package edu.brown.cs.dnd.REPL;

public interface Command {
  void run(String[] args) throws InvalidInputException;
}
