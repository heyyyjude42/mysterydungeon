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
  }

  private class SearchCommand implements Command {
    @Override
    public void run(String[] args) throws CommandFailedException {
      if (args.length < 2) {
        return;
      }

      // todo account for single search term / multiple words??
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
}
