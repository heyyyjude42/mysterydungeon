package edu.brown.cs.dnd.REPL;

import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
  private Map<String, Command> commands;

  public CommandHandler() {
    this.commands = new HashMap<>();
  }

  public void register(String name, Command cmd) {
    this.commands.put(name, cmd);
  }

  public void runCommand(String[] args) throws InvalidInputException, CommandFailedException {
    if (args == null || args.length == 0) {
      throw new InvalidInputException("ERROR: no arguments given");
    }

    String name = args[0];
    Command cmd = this.commands.get(name);

    if (cmd == null) {
      throw new InvalidInputException("ERROR: command not found");
    }

    try {
      cmd.run(args);
    } catch (CommandFailedException | InvalidInputException e) {
      System.out.println(e.getMessage());
    }
  }
}
