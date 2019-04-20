package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.Data.Monster;
import edu.brown.cs.dnd.REPL.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;
import java.util.HashMap;

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
    handler.register("generate-npc", new GenerateNPCCommand());
  }


  private class GenerateNPCCommand implements Command {

    @Override
    public String run(String[] args) throws InvalidInputException, CommandFailedException {
      Monster m;
      // No flags
      if (args.length == 0) {
        m = randomNPC();
      } else {

      }
    }

    /**
     * Method returns a random NPC without any constraints set by the user.
     * @return
     */
    Monster randomNPC() {
      PreparedStatement prep;
      Connection conn = Database.getConnection();

      try {
        prep = conn.prepareStatement("SELECT * FROM monsters ORDER BY "
                + "Random() LIMIT 1;");

        ResultSet rs = prep.executeQuery();
        Monster m = extractMonsterResult(rs);
        prep.close();

        return m;

      } catch (SQLException e) {
        System.out.println("ERROR: " + e.getMessage());
      }

      return null;
    }

    /**
     * Method extracts out a Monster from the query result.
     * @param rs    A ResultSet that contains the query result
     * @return    A Monster that is extracted from the query result
     */
    public Monster extractMonsterResult(ResultSet rs) {
      try {
        while (rs.next()) {
          String name = rs.getString(2);
          String size = rs.getString(3);
          String type = rs.getString(4);
          String alignment = rs.getString(5);
          int ac = rs.getInt(6);
          int hp = rs.getInt(7);
          String hpDice = rs.getString(8);
          int speed = rs.getInt(9);
          int str = rs.getInt(10);
          int dex = rs.getInt(11);
          int con = rs.getInt(12);
          int intelligence = rs.getInt(13);
          int wis = rs.getInt(14);
          int cha = rs.getInt(15);
          int cr = rs.getInt(16);

          HashMap<String, String> traits =
                  Database.sqlStringToMap(rs.getString(15));
          HashMap<String, String> actions =
                  Database.sqlStringToMap(rs.getString(16));
          HashMap<String, String> legendaryActions =
                  Database.sqlStringToMap(rs.getString(17));

          rs.close();

          return new Monster(name, size, type, alignment, ac, hp, hpDice,
                  speed, str, dex, con, intelligence, wis, cha, cr, traits,
                  actions, legendaryActions);
        }
      } catch (SQLException e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }
}
