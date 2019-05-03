package edu.brown.cs.dnd.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a result of a command.
 */
public class Result {

  private ReturnType type;
  private List<QueryResult> results;

  // empty constructor for GSON serialization
  public Result() {

  }

  /**
   * A Constructor for a Result.
   * @param type    A ReturnType that is the return type of the result
   * @param results   A List of query results that form the result
   */
  public Result(ReturnType type, List<? extends QueryResult> results) {
    this.type = type;
    this.results = new ArrayList<>();
    this.results.addAll(results);
  }

  /**
   * Method gets the return type.
   * @return    A ReturnType object that is the return type
   */
  public ReturnType getType() {
    return type;
  }

  /**
   * Method gets the list of query results.
   * @return    A List of QueryResult objects that are the query results
   */
  public List<QueryResult> getResults() {
    return results;
  }

  @Override
  public String toString() {
    if (this.results.isEmpty()) {
      return "Didn't find anything :(\n";
    } else if (this.results.size() < 5) {
      String toReturn = "";
      for (QueryResult r : this.results) {
        toReturn += r.prettify() + "\n\n";
      }
      return toReturn.substring(0, toReturn.length() - 2);
    } else {
      String toReturn = "";
      for (QueryResult r : this.results) {
        toReturn += "* " + r.simplify() + "\n";
      }
      return toReturn.substring(0, toReturn.length() - 1);
    }
  }
}
