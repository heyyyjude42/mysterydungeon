package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.Data.Monster;
import edu.brown.cs.dnd.REPL.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class representing a handler to support generating an encounter based on
 * user input.
 */
public class GenerateEncounterHandler implements Handler {

  /**
   * A Constructor for a GenerateEncounterHandler.
   */
  public GenerateEncounterHandler() {
    Database.load("data/srd.db");
  }

  @Override
  public void registerCommands(CommandHandler handler) {
    handler.register("generate-encounter", new GenerateEncounter());
  }

  private class GenerateEncounter implements Command {

    @Override
    public String run(String[] args) throws
            InvalidInputException, CommandFailedException {

      if (args.length != 2) {
        throw new InvalidInputException("ERROR: Must specify party size");
      }

      int partySize = 0;

      try {
        partySize = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        System.out.println("ERROR: party size is not a number");
      }

      List<Monster> encounter = createEncounter(partySize);

      StringBuilder sb = new StringBuilder();

      for (Monster m : encounter) {
        sb.append("\n\n\n");
        sb.append(m.prettify());
      }

      return sb.toString();
    }

    /**
     * Method creates an encounter with the size specified by partySize.
     * @param partySize   An int that is the party size
     * @return    A List of Monsters that is the encounter with size partySize
     */
    List<Monster> createEncounter(int partySize) throws CommandFailedException {
      PreparedStatement prep;
      Connection conn = Database.getConnection();

      try {
        prep = conn.prepareStatement("SELECT * FROM monsters ORDER BY "
                + "Random() LIMIT ?;");

        prep.setInt(1, partySize);

        ResultSet rs = prep.executeQuery();
        List<Monster> encounter = GenerateNPCHandler.extractMonsterResult(rs);

        rs.close();
        return encounter;
      } catch (SQLException e) {
        throw new CommandFailedException("ERROR: Could not create encounter");
      }
    }
  }
}
