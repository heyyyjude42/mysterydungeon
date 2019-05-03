package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.Data.Monster;
import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.REPL.Command;
import edu.brown.cs.dnd.REPL.Handler;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.InvalidInputException;
import edu.brown.cs.dnd.REPL.CommandFailedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

  /**
   * Class representing the generate-encounter command.
   */
  private class GenerateEncounter implements Command {

    @Override
    public Result run(String[] args) throws
        InvalidInputException, CommandFailedException {

      if (args.length != 2) {
        throw new InvalidInputException("ERROR: Must specify the sum of the "
                + "party's levels.");
      }

      int partyLevel = 0;

      try {
        partyLevel = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        throw new InvalidInputException("ERROR: combined party level is not a"
                + " number");
      }

      List<Monster> encounter = createEncounter(partyLevel);

      return new Result(ReturnType.ENCOUNTER, encounter);
    }

    /**
     * Method creates an encounter with the size specified by partyLevel.
     *
     * @param partyLevel An int that is the sum of the party's levels.
     * @return A List of Monsters that is the encounter appropriate for the
     * level sum.
     */
    List<Monster> createEncounter(int partyLevel)
            throws CommandFailedException {
      PreparedStatement prep;
      Connection conn = Database.getConnection();

      try {
        List<Integer> monsterCR = getEncounterMonsterCRs(partyLevel,
            new ArrayList<>());
        List<Monster> encounter = new ArrayList<>();

        for (Integer i : monsterCR) {
          prep =
              conn.prepareStatement("SELECT * FROM monsters WHERE cr = "
                  + i + " ORDER BY " + "Random() LIMIT ?;");
          prep.setInt(1, i);

          ResultSet rs = prep.executeQuery();
          List<Monster> monster = GenerateNPCHandler.extractMonsterResult(rs);
          encounter.add(monster.get(0));
          rs.close();
        }

        return encounter;
      } catch (SQLException e) {
        System.out.println(e.getMessage());
        throw new CommandFailedException("ERROR: Could not create encounter");
      }
    }

    /**
     * Divides up a number into a sum of smaller, random numbers.
     *
     * @param partyLevel Combined party level of the group.
     * @param currCR     The current list of CRs to add to.
     * @return    A List of Integers that are the monster crs
     */
    private List<Integer> getEncounterMonsterCRs(int partyLevel,
                                                 List<Integer> currCR) {
      if (partyLevel == 0) {
        return currCR;
      }

      int nextCR = (int) (Math.random() * partyLevel + 1);

      if (nextCR > partyLevel) {
        nextCR = partyLevel;
      }

      final int twentyFour = 24;

      if (nextCR > twentyFour) {
        nextCR = twentyFour;
      }

      currCR.add(nextCR);
      return getEncounterMonsterCRs(partyLevel - nextCR, currCR);
    }
  }
}
