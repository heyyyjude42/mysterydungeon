package edu.brown.cs.dnd.Data;

import java.util.ArrayList;
import java.util.List;

public class Result {
  private ReturnType type;
  private List<QueryResult> results;

  // empty constructor for GSON serialization
  public Result() {

  }

  public Result(ReturnType type, List<? extends QueryResult> results) {
    this.type = type;
    this.results = new ArrayList<>();
    this.results.addAll(results);
  }

  public ReturnType getType() {
    return type;
  }

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
