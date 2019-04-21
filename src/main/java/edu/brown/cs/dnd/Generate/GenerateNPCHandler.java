package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.*;
import edu.brown.cs.dnd.REPL.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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


  /**
   * Method extracts out a Monster from the query result.
   * @param rs    A ResultSet that contains the query result
   * @return    A Monster that is extracted from the query result
   */
  static List<Monster> extractMonsterResult(ResultSet rs) throws
          CommandFailedException {
    List<Monster> result = new ArrayList<>();

    try {
      while (rs.next()) {
        String name = rs.getString(2);
        String size = rs.getString(3);
        String type = rs.getString(4);
        String alignment = rs.getString(5);
        int ac = rs.getInt(6);
        int hp = rs.getInt(7);
        String hpDice = rs.getString(8);
        String speed = rs.getString(9);
        int str = rs.getInt(10);
        int dex = rs.getInt(11);
        int con = rs.getInt(12);
        int intelligence = rs.getInt(13);
        int wis = rs.getInt(14);
        int cha = rs.getInt(15);
        int cr = rs.getInt(16);

        HashMap<String, String> traits =
                Database.sqlStringToMap(rs.getString(17));
        HashMap<String, String> actions =
                Database.sqlStringToMap(rs.getString(18));
        HashMap<String, String> legendaryActions =
                Database.sqlStringToMap(rs.getString(19));

        result.add(new Monster(name, size, type, alignment, ac, hp, hpDice,
                speed, str, dex, con, intelligence, wis, cha, cr, traits,
                actions, legendaryActions));
      }
      rs.close();
    } catch (SQLException e) {
      throw new CommandFailedException("Could not generate NPC");
    }

    return result;
  }

  /**
   * Class representing the generate-npc command.
   */
  private class GenerateNPCCommand implements Command {

    @Override
    public Result run(String[] args) throws
            InvalidInputException, CommandFailedException {
      Monster m;
      List<QueryResult> sb = new ArrayList<>();
      // No flags
      if (args.length == 1) {
        m = randomNPC();
        sb.add(m);
      } else {
        m = randomNPCFlags(args);
      }

      return new Result(ReturnType.NPC, sb);
    }

    /**
     * Method returns a random NPC without any constraints set by the user.
     * @return    A Monster that is the randomly generated NPC
     */
    Monster randomNPC() throws CommandFailedException {
      PreparedStatement prep;
      Connection conn = Database.getConnection();

      try {
        prep = conn.prepareStatement("SELECT * FROM monsters ORDER BY "
                + "Random() LIMIT 1;");

        ResultSet rs = prep.executeQuery();
        Monster m = extractMonsterResult(rs).get(0);
        prep.close();

        return m;

      } catch (SQLException e) {
        throw new CommandFailedException("Could not generate NPC");
      }
    }

    /**
     * Method generates a random NPC based on
     * @param args
     * @return
     */
    Monster randomNPCFlags(String[] args) {
//      StringBuilder sb = new StringBuilder();
//
//      sb.append("WHERE ");
//
//      for (int i = 1; i < )
      return null;
    }
  }
}
