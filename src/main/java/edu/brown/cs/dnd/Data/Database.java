package edu.brown.cs.dnd.Data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import edu.brown.cs.dnd.REPL.CommandFailedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
   * Executes a search in the whole database, ONLY FOR NAME.
   *
   * @param term
   */
  public static List<? extends QueryResult> searchSRD(String term) throws SQLException {
    List<? extends QueryResult> result = new ArrayList<>();
    int index = 0;

    List<SearchOperator> o = new ArrayList<>();
    o.add(new SearchOperator(Comparator.IS, "name", term));

    while (result.isEmpty() && index < TABLES.length) {
      try {
        result = searchTable(o, TABLES[index]);
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
   * @param o
   * @param table
   * @return
   * @throws CommandFailedException
   * @throws SQLException
   */
  public static List<? extends QueryResult> searchTable(List<SearchOperator> o,
                                                        String table) throws CommandFailedException, SQLException {
    switch (table.toLowerCase()) {
      case "spell":
      case "spells":
        return searchSpells(o);
      case "monster":
      case "monsters":
      case "creature":
      case "creatures":
        return searchMonsters(o);
      case "feats":
      case "feat":
        return searchFeats(o);
      default:
        throw new CommandFailedException("ERROR: did not find the table named" +
            " " + table);
    }
  }

  public static List<Feat> searchFeats(List<SearchOperator> o) throws SQLException {
    List<Object[]> results = Database.searchTableRaw(o, "feats");
    List<Feat> typedResults = new ArrayList<>();

    for (Object[] result : results) {
      String name = (String) result[1];
      String desc = (String) result[2];
      typedResults.add(new Feat(name, desc));
    }

    return typedResults;
  }

  public static List<Monster> searchMonsters(List<SearchOperator> o) throws SQLException {
    List<Object[]> results = Database.searchTableRaw(o, "monsters");
    List<Monster> typedResults = new ArrayList<>();

    for (Object[] result : results) {
      String name = (String) result[1];
      String size = (String) result[2];
      String type = (String) result[3];
      String alignment = (String) result[4];
      int ac = (Integer) result[5];
      int hp = (Integer) result[6];
      String hpDice = (String) result[7];
      int speed = (Integer) result[8];
      int str = (Integer) result[9];
      int dex = (Integer) result[10];
      int con = (Integer) result[11];
      int intelligence = (Integer) result[12];
      int wis = (Integer) result[13];
      int cha = (Integer) result[14];

      double cr;
      if ((result[15].toString()).split("/").length > 1) {
        // the CR is a fraction
        double numerator =
            Double.parseDouble(((String) result[15]).split("/")[0]);
        double denominator = Double.parseDouble(((String) result[15]).split(
            "/")[1]);
        cr = numerator / denominator;
      } else {
        cr = (Integer) result[15];
      }

      HashMap<String, String> traits =
          Database.sqlStringToMap((String) result[16]);
      HashMap<String, String> actions =
          Database.sqlStringToMap((String) result[17]);
      HashMap<String, String> legendaryActions =
          Database.sqlStringToMap((String) result[18]);

      typedResults.add(new Monster(name, size, type, alignment, ac, hp,
          hpDice, speed, str, dex, con, intelligence, wis, cha, cr, traits,
          actions, legendaryActions));
    }

    return typedResults;
  }

  public static List<Spell> searchSpells(List<SearchOperator> o) throws SQLException {
    List<Object[]> results = Database.searchTableRaw(o, "spells");
    List<Spell> typedResults = new ArrayList<>();

    for (Object[] result : results) {
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

      typedResults.add(new Spell(name, school, level, ritual, range,
          castingTime, verbal, somatic, concentration, materials, duration,
          description, classes));
    }

    return typedResults;
  }

  private static List<Object[]> searchTableRaw(List<SearchOperator> operators,
                                               String table) throws SQLException {
    String query = "SELECT * FROM " + table + " WHERE";

    for (SearchOperator o : operators) {
      query += " " + o.toString() + " AND";
    }

    query = query.substring(0, query.length() - 4); // gets rid of the last "
    // AND "

    PreparedStatement prep;
    prep = conn.prepareStatement(query);

    for (int i = 0; i < operators.size(); i++) {
      prep.setString(i + 1, operators.get(i).getTerm());
    }

    ResultSet rs = prep.executeQuery();
    int colNum = rs.getMetaData().getColumnCount();

    List<Object[]> results = new ArrayList<>();

    while (rs.next()) {
      Object[] result = new Object[colNum];

      for (int i = 1; i <= colNum; i++) {
        result[i - 1] = rs.getObject(i);
      }

      results.add(result);
    }

    return results;
  }

  public static HashMap<String, String> sqlStringToMap(String term) {
    return new Gson().fromJson(term, new TypeToken<HashMap<String, String>>() {
    }.getType());
  }

  /**
   * Method gets the connection to the database.
   * @return    A Connection that is the connection to the database
   */
  public static Connection getConnection() {
    return conn;
  }
}
