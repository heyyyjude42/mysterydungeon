package edu.brown.cs.dnd.Data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import edu.brown.cs.dnd.REPL.CommandFailedException;

import java.sql.*;
import java.util.HashMap;

/**
 * Class for querying the database. SQL queries should be contained here.
 */
public class Database {
  private static Connection conn;
  private static String[] TABLES = {"spells", "feats", "monsters"};

  public static void load(String path) {
    try {
      Class.forName("org.sqlite.JDBC");
      String url = "jdbc:sqlite:" + path;
      conn = DriverManager.getConnection(url);
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("ERROR: database loading failed");
    }
  }

  /**
   * Executes a search in the whole database.
   *
   * @param term
   */
  public static QueryResult searchSRD(String term) throws SQLException {
    QueryResult result = null;
    int index = 0;

    while (result == null && index < TABLES.length) {
      try {
        result = searchTable(term, TABLES[index]);
      } catch (CommandFailedException e) {
        // shouldn't happen
        e.printStackTrace();
      }
      index++;
    }

    return result;
  }

  /**
   * Executes a search in a specific table.
   *
   * @param term
   * @param table
   * @return
   * @throws CommandFailedException
   * @throws SQLException
   */
  public static QueryResult searchTable(String term, String table) throws CommandFailedException, SQLException {
    switch (table.toLowerCase()) {
      case "spell":
      case "spells":
        return searchSpells(term);
      case "monster":
      case "monsters":
      case "creature":
      case "creatures":
        return searchMonsters(term);
      case "feats":
      case "feat":
        return searchFeats(term);
      default:
        throw new CommandFailedException("ERROR: did not find the table named" +
            " " + table);
    }
  }

  public static Feat searchFeats(String term) throws SQLException {
    Object[] result = Database.searchTableRaw(term, "feats");

    if (result == null) {
      return null;
    }

    String name = (String) result[1];
    String desc = (String) result[2];

    return new Feat(name, desc);
  }

  public static Monster searchMonsters(String term) throws SQLException {
    Object[] result = Database.searchTableRaw(term, "monsters");

    if (result == null) {
      return null;
    }

    String name = (String) result[1];
    String size = (String) result[2];
    String type = (String) result[3];
    String alignment = (String) result[4];
    int ac = (Integer) result[5];
    int hp = (Integer) result[6];
    String hpDice = (String) result[7];
    String speed = (String) result[8];
    int str = (Integer) result[9];
    int dex = (Integer) result[10];
    int con = (Integer) result[11];
    int intelligence = (Integer) result[12];
    int wis = (Integer) result[13];
    int cha = (Integer) result[14];
    int cr = (Integer) result[15];
    HashMap<String, String> traits =
        Database.sqlStringToMap((String) result[16]);
    HashMap<String, String> actions =
        Database.sqlStringToMap((String) result[17]);
    HashMap<String, String> legendaryActions =
        Database.sqlStringToMap((String) result[18]);

    return new Monster(name, size, type, alignment, ac, hp, hpDice, speed,
        str, dex, con, intelligence, wis, cha, cr, traits, actions,
        legendaryActions);
  }

  public static Spell searchSpells(String term) throws SQLException {
    Object[] result = Database.searchTableRaw(term, "spells");

    if (result == null) {
      return null;
    }

    String name = (String) result[1];
    String school = (String) result[2];
    int level = (Integer) result[3];
    boolean ritual = (Integer) result[4] == 1;
    String range = (String) result[5];
    String castingTime = (String) result[6];
    boolean verbal = (Integer) result[7] == 1;
    boolean somatic = (Integer) result[8] == 1;
    boolean concentration = (Integer) result[9] == 1;
    String materials = (String) result[10];
    String duration = (String) result[11];
    String description = (String) result[12];
    String classes = (String) result[13];

    return new Spell(name, school, level, ritual, range, castingTime, verbal,
        somatic, concentration, materials, duration, description, classes);
  }

  private static Object[] searchTableRaw(String term, String table) throws SQLException {
    String query = "SELECT * FROM " + table + " WHERE name = ?";

    // TODO: other conditions go here

    query += " COLLATE NOCASE;";

    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    prep.setString(1, term);

    ResultSet rs = prep.executeQuery();
    int colNum = rs.getMetaData().getColumnCount();

    if (!rs.next()) {
      return null;
    }

    Object[] results = new Object[colNum];

    for (int i = 1; i <= colNum; i++) {
      results[i - 1] = rs.getObject(i);
    }

    return results;
  }

  private static HashMap<String, String> sqlStringToMap(String term) {
    return new Gson().fromJson(term, new TypeToken<HashMap<String, String>>() {
    }.getType());
  }
}
