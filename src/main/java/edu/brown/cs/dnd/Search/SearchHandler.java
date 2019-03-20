package edu.brown.cs.dnd.Search;

import edu.brown.cs.dnd.REPL.Command;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.InvalidInputException;

/**
 * Class that handles REPL queries involving search.
 */
public class SearchHandler {
  public SearchHandler() {

  }

  public void registerCommands(CommandHandler handler) {
    handler.register("search", new SearchCommand());
  }

  private class SearchCommand implements Command {
    @Override
    public void run(String[] args) {
      if (args.length < 2) {
        return;
      }

      System.out.println(args.length);
    }
  }
}
