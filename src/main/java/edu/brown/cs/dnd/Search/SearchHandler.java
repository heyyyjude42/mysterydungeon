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
  private String OPTIONS_START = "\\|";
  private String OPTIONS_DELIMITER = ",";
  private String OPTIONS_NAME_BREAK = ":";

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

      String query = args[0];
      for (int i = 1; i < args.length; i++) {
        query += " " + args[i];
      }
      List<SearchOperator> operators = findOperators(query);

      if (args.length < 2) {
        return "";
      }

      // if there's no terms, we only care about name
      if (operators.isEmpty()) {
        return searchByName(args);
      } else {
        return searchByOperators(query, operators);
      }
    }

    private List<SearchOperator> findOperators(String query) {
      if (query.split(OPTIONS_START).length == 1) {
        return new ArrayList<>();
      } else {
        String[] options =
            query.split(OPTIONS_START)[1].split(OPTIONS_DELIMITER);

        List<SearchOperator> ops = new ArrayList<>();

        for (String o : options) {
          String[] delimited = o.split(OPTIONS_NAME_BREAK); // turns
          // class: wizard into [class][ wizard]

          // should have at least two things in it
          if (delimited.length >= 2) {
            String columnName = delimited[0].replace(" ", "");
            String[] restrictions = delimited[1].substring(1).split(" "); // whitespace

            if (restrictions.length == 1) {
              // this is an equality comparison, such as level: 3
              ops.add(new SearchOperator(Comparator.IS, columnName,
                  restrictions[0]));
            } else {
              restrictions = sanitize(restrictions); // this gives us quotes
              if (restrictions.length == 2) {
                // the first is the comparator, and the second is the term
                Comparator c;
                switch (restrictions[0]) {
                  case "<=":
                    c = Comparator.LESS_THAN_OR_EQUALS;
                    break;
                  case "<":
                    c = Comparator.LESS_THAN;
                    break;
                  case ">=":
                    c = Comparator.GREATER_THAN_OR_EQUALS;
                    break;
                  case ">":
                    c = Comparator.GREATER_THAN;
                    break;
                  default:
                    c = Comparator.IS;
                }
                ops.add(new SearchOperator(c, columnName, restrictions[1]));
              }
            }
          }
        }

        return ops;
      }
    }

    private String searchByOperators(String query, List<SearchOperator> ops) throws CommandFailedException {
      String[] preOptions = query.split(OPTIONS_START)[0].split(" ");

      if (preOptions.length < 2) {
        // means no table name was given, since "search" is already the first
        // index
        return "";
      }

      String tableName = preOptions[1];
      List<? extends QueryResult> result;
      try {
        result = Database.searchTable(ops, tableName);
      } catch (SQLException e) {
        throw new CommandFailedException("ERROR: " + e.getMessage());
      }

      return prettifyResults(result);
    }

    private String searchByName(String[] args) throws CommandFailedException {
      List<? extends QueryResult> result;

      // no table specified, only a search term.
      if (args.length == 2) {
        try {
          result = Database.searchSRD(args[1]);
        } catch (SQLException e) {
          throw new CommandFailedException("ERROR: " + e.getMessage());
        }
      } else {
        // TODO account for "search in spells fireball" vs "search spells for
        //  fireball"??

        String table = args[1];
        String term = args[2];

        List<SearchOperator> operators = new ArrayList<>();
        operators.add(new SearchOperator(Comparator.IS, "name", term));

        try {
          result = Database.searchTable(operators, table);
        } catch (SQLException e) {
          throw new CommandFailedException("ERROR: " + e.getMessage());
        }
      }

      return prettifyResults(result);
    }

    private String prettifyResults(List<? extends QueryResult> result) {
      if (result.isEmpty()) {
        return "Didn't find anything :(\n";
      } else if (result.size() < 5) {
        String toReturn = "";
        for (QueryResult r : result) {
          toReturn += r.prettify() + "\n\n";
        }
        return toReturn;
      } else {
        String toReturn = "";
        for (QueryResult r : result) {
          toReturn += "* " + r.simplify() + "\n";
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
