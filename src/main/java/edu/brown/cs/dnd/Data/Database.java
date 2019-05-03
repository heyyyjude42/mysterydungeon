package edu.brown.cs.dnd.Data;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import edu.brown.cs.dnd.REPL.CommandFailedException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for querying the database. SQL queries should be contained here.
 */
public final class Database {

  /**
   * A Default Constructor for a Database.
   */
  private Database() {

  }

  private static Connection conn;
  private static final String[] TABLES = {"spells", "feats", "monsters"};

  /**
   * Method loads the path of the database.
   * @param path    A String that is the path of the database.
   */
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
   * @param term    The term to search by
   * @return    A Result object that is the result of the query
   * @throws SQLException    Thrown when query fails
   */
  public static Result searchSRD(String term) throws SQLException {
    int index = 0;
    Result result = new Result(ReturnType.NONE, new ArrayList<>());

    List<SearchOperator> o = new ArrayList<>();
    o.add(new SearchOperator(Comparator.IS, "name", term));

    while (result.getResults().isEmpty() && index < TABLES.length) {
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
   * @param o   The search operator of the query
   * @param table   The table to search in
   * @return    A Result object that is the result of the query
   * @throws CommandFailedException Thrown when error in command
   * @throws SQLException   Thrown when query fails
   */
  public static Result searchTable(List<SearchOperator> o,
                                                        String table)
          throws CommandFailedException, SQLException {
    switch (table.toLowerCase()) {
      case "spell":
      case "spells":
        return new Result(ReturnType.SPELL, searchSpells(o));
      case "monster":
      case "monsters":
      case "creature":
      case "creatures":
        return new Result(ReturnType.MONSTER, searchMonsters(o));
      case "feats":
      case "feat":
        return new Result(ReturnType.FEAT, searchFeats(o));
      default:
        throw new CommandFailedException("ERROR: did not find the table named"
                + " "
                + table);
    }
  }

  /**
   * Method searches the database for feats.
   * @param o   The search operator of the query
   * @return    A List of Feats that are the results of the query
   * @throws SQLException   Thrown when query fails
   */
  private static List<Feat> searchFeats(List<SearchOperator> o)
          throws SQLException {
    List<Object[]> results = Database.searchTableRaw(o, "feats");
    List<Feat> typedResults = new ArrayList<>();

    for (Object[] result : results) {
      String name = (String) result[1];
      String desc = (String) result[2];
      typedResults.add(new Feat(name, desc));
    }

    return typedResults;
  }

  /**
   * Method searches the database for monsters.
   * @param o   The search operator of the query
   * @return    A List of Monsters that are the results of the query
   * @throws SQLException   Thrown when query fails
   */
  private static List<Monster> searchMonsters(List<SearchOperator> o)
          throws SQLException {
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
      String speed = (String) result[8];
      int str = (Integer) result[9];
      int dex = (Integer) result[10];
      int con = (Integer) result[11];
      int intelligence = (Integer) result[12];
      int wis = (Integer) result[13];
      int cha = (Integer) result[14];
      double cr;
      try {
        cr = (Double) result[15];
      } catch (ClassCastException e) {
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

  /**
   * Method searches the database for spells.
   * @param o   The search operator of the query
   * @return    A List of Spells that are the results of the query
   * @throws SQLException   Thrown when query fails
   */
  private static List<Spell> searchSpells(List<SearchOperator> o) throws SQLException {
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

  /**
   * Method searches the table without a specified subsection.
   * @param operators   The operators of the search query
   * @param table   The table to search through
   * @return    A List of Objects that are the query results
   * @throws SQLException   Thrown when query fails
   */
  private static List<Object[]> searchTableRaw(List<SearchOperator> operators,
                                               String table)
          throws SQLException {
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

  /**
   * Method converts a JSON String to a HashMap.
   * @param term    A term that is the JSON String
   * @return    A HashMap that is the parsed version of the JSON String
   */
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
