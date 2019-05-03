package edu.brown.cs.dnd.REPL;

import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.StringResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a Command Handler.
 */
public class CommandHandler {
  private Map<String, Command> commands;

  CommandHandler() {
    this.commands = new HashMap<>();
  }

  /**
   * Method registers the command.
   * @param name    A String that is the REPL command
   * @param cmd   A Command that is the command to register
   */
  public void register(String name, Command cmd) {
    this.commands.put(name, cmd);
  }

  /**
   * Method runs the command.
   * @param args    A String array that are the args of the command
   * @return    A Result object that is the result of the command
   * @throws InvalidInputException    Thrown when encounters invalid user input
   */
  public Result runCommand(String[] args) throws InvalidInputException {
    if (args == null || args.length == 0) {
      throw new InvalidInputException("ERROR: no arguments given");
    }

    String name = args[0];
    Command cmd = this.commands.get(name);

    if (cmd == null) {
      throw new InvalidInputException("ERROR: command not found");
    }

    try {
      return cmd.run(args);
    } catch (CommandFailedException | InvalidInputException e) {
      return new Result(ReturnType.STRING,
          Collections.singletonList(new StringResult(e.getMessage())));
    }
  }
}
