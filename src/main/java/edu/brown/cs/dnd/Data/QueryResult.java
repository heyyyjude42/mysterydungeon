package edu.brown.cs.dnd.Data;

public interface QueryResult {
  /**
   * Returns a prettified version of the results.
   *
   * @return
   */
  String prettify();

  String simplify();
}
