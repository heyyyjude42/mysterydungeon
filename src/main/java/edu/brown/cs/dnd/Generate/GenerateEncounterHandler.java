package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;

/**
 * Class representing a handler to support generating an encounter based on
 * user input.
 */
public class GenerateEncounterHandler {

  /**
   * A Constructor for a GenerateEncounterHandler.
   */
  public GenerateEncounterHandler() {
    Database.load("data/srd.db");
  }
}
