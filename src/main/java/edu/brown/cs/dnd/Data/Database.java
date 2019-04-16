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
  public static void searchSRD(String term) {

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
      case "spells":
        return searchSpells(term);
      default:
        throw new CommandFailedException("ERROR: did not find the table named" +
            " " + table);
    }
  }

  public static Spell searchSpells(String term) throws SQLException {
    Object[] result = Database.searchTableRaw(term, "spells");

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
    String query = "SELECT * FROM " + table + " WHERE name = ?;";
    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    prep.setString(1, term);

    ResultSet rs = prep.executeQuery();
    int colNum = rs.getMetaData().getColumnCount();
    rs.next();

    Object[] results = new Object[colNum];

    for (int i = 1; i <= colNum; i++) {
      results[i - 1] = rs.getObject(i);
    }

    return results;
  }

  public static HashMap<String, String> sqlStringToMap(String term) {
    return new Gson().fromJson(term, new TypeToken<HashMap<String, String>>() {
    }.getType());
  }
}
