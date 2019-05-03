package edu.brown.cs.dnd.REPL;

import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.StringResult;

import java.util.Arrays;

/**
 * HelpHandler class: helps with providing information about REPL commands.
 */
public class HelpHandler implements Handler {

  /**
   * Registers commands to the CommandHandler.
   *
   * @param handler the CommandHandler to register commands to.
   */
  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("/help", new HelpCommand());
  }

  /**
   * The private inner class that gets run whenever /help is run. Parses
   * which command to get information about and returns information about
   * that to the user.
   */
  private class HelpCommand implements Command {
    /**
     * Parses the input for which command the user wants more details about
     * and returns it.
     *
     * @param args The arguments sent into the command line.
     * @return A Result with the information requested.
     * @throws InvalidInputException If there are more than 2 words in the
     * input.
     */
    @Override
    public Result run(String[] args) throws InvalidInputException {
      if (args.length > 2) {
        throw new InvalidInputException("ERROR: expecting syntax help "
                + "<command>");
      } else if (args.length == 1) {
        return toResult("Type help <command> for one of the following "
                + "commands:\n- search\n- roll\n- generate-npc\n- "
                + "generate-encounter\n- generate-dungeon");
      }

      switch (args[1]) {
        case "search":
        case "s": {
          String s = "- search <term>: searches the entire database (e.g. "
                  + "search foresight, or search “danse macabre”)\n";
          s += "- search <table> <term>: searches a specific table (e.g. "
                  + "search monsters bear)\n";
          s += "- search <table> | <flags>: searches with a set of operators "
                  + "in a table (e.g. search spells | class: bard, "
                  + "level: 2, or "
                  + "search monsters | type: humanoid, ac: >= 18, "
                  + "int: >= 14)\n";
          s += "- Tip: you can also search with just “s” for short!";
          return toResult(s);
        }
        case "roll": {
          String s = "- roll <dice>: randomly rolls the result (e.g. roll 2d4"
                  + " will roll two four-sided dice and sum the "
                  + "results together)\n";
          s += "- Tip: you can join dice amounts together, such as roll 2d4 +"
                  + " 10d6 - 5, for you Sneak Attack Rogues!";
          return toResult(s);
        }
        case "npc":
        case "generate-npc": {
          String s = "- generate-npc: generates an entirely random NPC\n";
          s += "generate-npc <flags>: allows finer control over the "
                  + "randomization. Separate flags by a space (e.g. "
                  + "generate-npc "
                  + "size: medium, type: humanoid)";
          return toResult(s);
        }
        case "encounter":
        case "generate-encounter": {
          String s = "- generate-encounter <partySize>: generates a random "
                  + "encounter for partySize number of people (e.g. "
                  + "generate-encounter 5)";
          return toResult(s);
        }
        case "dungeon":
        case "generate-dungeon":
          return toResult("generate-dungeon: <width> "
                  + "<height> <small, medium "
                  + "or large>");
        case "help": {
          String s = "help: see list of all commands\n";
          s += "help <command>: see more details about a specific command";
          return toResult(s);
        }
      }

      // if nothing at this point, then return a help help
      return new Result(ReturnType.STRING, Arrays.asList(new StringResult(
          "Type help <command> for one of the following commands:\n- "
                  + "search\n- roll\n- generate-npc\n- generate-encounter\n- "
                  + "generate-dungeon")));
    }
  }

  /**
   * Takes in a String and returns it as a simple result.
   * @param s the string that is the result
   * @return a Result object
   */
  private Result toResult(String s) {
    return new Result(ReturnType.STRING, Arrays.asList(new StringResult(s)));
  }
}
