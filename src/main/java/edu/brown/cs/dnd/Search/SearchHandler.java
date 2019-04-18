package edu.brown.cs.dnd.Search;

import edu.brown.cs.dnd.Data.Comparator;
import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.Data.SearchOperator;
import edu.brown.cs.dnd.REPL.Command;
import edu.brown.cs.dnd.REPL.CommandFailedException;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.Handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles REPL queries involving search.
 */
public class SearchHandler implements Handler {
  public SearchHandler() {
    Database.load("data/srd.db");
  }

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("search", new SearchCommand());
    handler.register("s", new SearchCommand());
  }

  private class SearchCommand implements Command {
    @Override
    public String run(String[] args) throws CommandFailedException {
      args = sanitize(args);

      if (args.length < 2) {
        return "";
      }

      List<? extends QueryResult> result;

      // no table specified, only a search term.. will have to account for
      // quotes here
      if (args.length == 2) {
        try {
          result = Database.searchSRD(args[1]);
        } catch (SQLException e) {
          throw new CommandFailedException("ERROR: " + e.getMessage());
        }
      } else {
        // todo account for single search term / multiple words??
        // todo: syntax: search "mage hand" vs search in spells "mage hand"
        // todo: allow search for other conditions as well?
        String table = args[1];
        String term = args[2];

        List<SearchOperator> operators = new ArrayList<>();
        operators.add(new SearchOperator(Comparator.IS, "name", term, true));

        try {
          result = Database.searchTable(operators, table);
        } catch (SQLException e) {
          throw new CommandFailedException("ERROR: " + e.getMessage());
        }
      }

      if (result.isEmpty()) {
        return "Didn't find anything :(\n";
      } else {
        String toReturn = "";
        for (QueryResult r : result) {
          toReturn += r.prettify() + "\n";
        }
        return toReturn;
      }
    }

    /**
     * Takes in the raw input and parses it for quotation marks.
     *
     * @param args
     * @return
     */
    private String[] sanitize(String[] args) {
      List<String> results = new ArrayList<>();

      boolean inQuotes = false;
      String hold = "";

      for (String s : args) {
        if (s.contains("\"")) {
          if (inQuotes) {
            // if we're already in quotes, this closes it
            inQuotes = false;
            hold += " " + s.substring(0, s.indexOf("\""));
            results.add(hold);
            hold = "";
          } else {
            // if we're not in quotes, this opens it
            inQuotes = true;
            hold = s.substring(s.indexOf("\"") + 1);
          }
        } else {
          if (inQuotes) {
            // add to hold
            hold += " " + s;
          } else {
            // add to list
            results.add(s);
          }
        }
      }

      return results.toArray(new String[results.size()]);
    }
  }


}
