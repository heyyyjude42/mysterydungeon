package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.REPL.Handler;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.Command;

/**
 * Class representing a handler to support generating an NPC based on user
 * input.
 */
public class GenerateNPCHandler implements Handler {

  /**
   * A Constructor for a GenerateNPCHandler.
   */
  public GenerateNPCHandler() {
    Database.load("data/srd.db");
  }

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("generate npc", new GenerateNPCCommand());
  }


  private class GenerateNPCCommand implements Command {

  }


}
