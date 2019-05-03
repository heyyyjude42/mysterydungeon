package edu.brown.cs.dnd.Data;

/**
 * Interface representing a result of a database query.
 */
public interface QueryResult {
  /**
   * Returns a prettified version of the results.
   *
   * @return    A String that is the prettified version of the results
   */
  String prettify();

  /**
   * Returns a simplified version of the results.
   * @return    A String that is the
   */
  String simplify();
}
