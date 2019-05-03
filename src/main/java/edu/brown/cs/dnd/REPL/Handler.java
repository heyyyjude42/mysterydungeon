package edu.brown.cs.dnd.REPL;

/**
 * Interface representing a handler for commmands.
 */
public interface Handler {
  /**
   * Method registers the commands taken by the REPL.
   * @param handler   A CommandHandler object used to register the commands
   */
  void registerCommands(CommandHandler handler);
}
