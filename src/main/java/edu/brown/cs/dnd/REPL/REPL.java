package edu.brown.cs.dnd.REPL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class that parses user input. Call beginParsing to begin. Meant to be
 * reused across projects.
 */
public class REPL {
  private CommandHandler handler;

  /**
   * Constructor.
   */
  public REPL(CommandHandler handler) {
    this.handler = handler;
  }

  /**
   * Begins parsing user input using a while loop.
   */
  void beginParsing() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        System.in))) {
      String nextInput;

      while ((nextInput = br.readLine()) != null) {
        parse(nextInput);
      }
    } catch (IOException e) {
      // not possible
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("ERROR: one of your arguments could not be read "
          + "as a number.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("ERROR: please load a file first with stars"
          + " <filepath>.");
    }
  }

  private void parse(String nextInput) {
    try {
      handler.runCommand(nextInput.split("\\s+"));
    } catch (InvalidInputException e) {
      e.getMessage();
    }
  }
}
