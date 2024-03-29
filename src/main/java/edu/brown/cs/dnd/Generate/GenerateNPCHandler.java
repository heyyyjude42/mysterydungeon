package edu.brown.cs.dnd.Generate;

import edu.brown.cs.dnd.Data.Database;
import edu.brown.cs.dnd.Data.Monster;
import edu.brown.cs.dnd.Data.Result;
import edu.brown.cs.dnd.Data.ReturnType;
import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.REPL.Command;
import edu.brown.cs.dnd.REPL.Handler;
import edu.brown.cs.dnd.REPL.CommandHandler;
import edu.brown.cs.dnd.REPL.InvalidInputException;
import edu.brown.cs.dnd.REPL.CommandFailedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

        Map<String, String> traits =
                Database.sqlStringToMap(rs.getString(17));
        Map<String, String> actions =
                Database.sqlStringToMap(rs.getString(18));
        Map<String, String> legendaryActions =
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

    private Map<String, String> flagValues;

    GenerateNPCCommand() {
      this.flagValues = new HashMap<>();
    }

    @Override
    public Result run(String[] args) throws
            InvalidInputException, CommandFailedException {
      Monster m;
      List<QueryResult> sb = new ArrayList<>();
      // No flags
      if (args.length == 1) {
        m = randomNPC();
      } else {
        // With flags
        m = randomNPCFlags(args);
      }
      sb.add(m);

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
     * Method generates a random NPC based on flags supplied by the user.
     * @param args    A String array that contain the user flags
     * @return    A Monster that is the randomly generated NPC based on the
     * user flags
     */
    Monster randomNPCFlags(String[] args) throws
            InvalidInputException, CommandFailedException {
      Monster m;
      String queryBody = createQueryBody(args);

      try {
        PreparedStatement prep;
        Connection conn = Database.getConnection();

        prep = conn.prepareStatement("SELECT * FROM monsters WHERE "
                + queryBody + " ORDER BY Random() LIMIT 1");

        ResultSet rs = prep.executeQuery();

        if (!rs.isBeforeFirst()) {
          m = customNPC(args);
        } else {
          m = extractMonsterResult(rs).get(0);
        }

      } catch (SQLException e) {
        return customNPC(args);
      }

      return m;
    }

    /**
     * Method creates the query body based on the user flags.
     * @param args    A String array that contain the flags specified by the
     *                user
     * @return    A String that is the query body
     */
    String createQueryBody(String[] args) throws
            InvalidInputException, CommandFailedException {
      StringBuilder result = new StringBuilder();
      int iteration = 0;

      for (int i = 1; i < args.length - 1; i++) {
        String flag = args[i];

        if (flag.charAt(flag.length() - 1) != ':') {
          throw new InvalidInputException("ERROR: Flag format should be "
                  + "<Attribute>: <Attribute Value>");
        }

        StringBuilder value = new StringBuilder();
        value.append(args[i + 1]); i++;

        // Accumulate the attribute value if it's multiple words
        while (i < args.length - 1
                && args[i + 1].charAt(args[i + 1].length() - 1) != ':') {
          value.append(" ");
          value.append(args[i + 1]); i++;
        }
        flag = flag.substring(0, flag.length() - 1);

        this.flagValues.put(flag, value.toString());

        if (iteration == 0) {
          result.append(flag + " LIKE '%" + value.toString() + "%'");
        } else {
          result.append(" AND " + flag + " LIKE '%" + value.toString() + "%'");
        }
        iteration++;
      }

      return result.toString();
    }


    /**
     * Method generates a random NPC customized with the user's exact values.
     * @param args    A String array that are contain the values of the user's
     *                flags
     * @return    A Monster that is the customized NPC
     */
    private Monster customNPC(String[] args) throws
            InvalidInputException, CommandFailedException {
      Monster m = randomNPC();

      for (String flag : this.flagValues.keySet()) {
        String value = this.flagValues.get(flag);

        switch (flag) {
          case "type":
            m.setType(value);
            break;
          case "name":
            m.setName(value);
            break;
          case "size":
            m.setSize(value);
            break;
          case "alignment":
            m.setAlignment(value);
            break;
          case "background":
            m.setBackground(value);
            break;
          default:
            throw new InvalidInputException("ERROR: Flag not valid");
        }
      }

      return m;
    }

  }

}
