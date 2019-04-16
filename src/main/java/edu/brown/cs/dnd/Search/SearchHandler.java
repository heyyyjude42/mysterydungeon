package edu.brown.cs.dnd.Search;

import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.REPL.Command;
import edu.brown.cs.dnd.REPL.CommandFailedException;
import edu.brown.cs.dnd.REPL.CommandHandler;

import java.sql.SQLException;

/**
 * Class that handles REPL queries involving search.
 */
public class SearchHandler {
  public SearchHandler() {
    Database.load("data/srd.db");
  }

  public void registerCommands(CommandHandler handler) {
    handler.register("search", new SearchCommand());
    handler.register("s", new SearchCommand());
    handler.register("roll", new RollCommand());
  }

  private class SearchCommand implements Command {
    @Override
    public void run(String[] args) throws CommandFailedException {
      if (args.length < 2) {
        return;
      }

      // todo account for single search term / multiple words??
      // todo: syntax: search "mage hand" vs search in spells "mage hand"
      String table = args[1];
      String term = args[2];

      try {
        QueryResult result = Database.searchTable(term, table);
        System.out.println("\n" + result.prettify() + "\n");
      } catch (SQLException e) {
        throw new CommandFailedException("ERROR: database search failed");
      }
    }
  }

  private class RollCommand implements Command {
    @Override
    public void run(String[] args) throws CommandFailedException {
      if (args.length != 2) {
        throw new CommandFailedException("ERROR: expected syntax in the form " +
            "of roll xdy, such as 4d6 or 1d8.");
      }

      String dice = args[1];

      try {
        System.out.println("Result: " + rollDice(dice));
      } catch (NumberFormatException e) {
        throw new CommandFailedException("ERROR: expected syntax in the form " +
            "of roll xdy, such as 4d6 or 1d8.");
      }
    }

    /**
     * Takes in a string in the form of xdy and returns the corresponding
     * roll amount. For instance, 2d8 will return a number between 2 and 16.
     *
     * @param dice String representation of dice to be rolled.
     * @return The final roll in form of an int.
     */
    private int rollDice(String dice) {
      String[] nums = dice.split("d");

      int numDice = Integer.parseInt(nums[0]);
      int diceFaces = Integer.parseInt(nums[1]);
      int sum = 0;

      for (int i = 0; i < numDice; i++) {
        sum += Math.random() * diceFaces;
      }

      return sum;
    }
  }
}
