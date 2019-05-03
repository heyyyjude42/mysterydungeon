package edu.brown.cs.dnd.Data;

/**
 * Class representing a search operator used in SQL queries.
 */
public class SearchOperator {
  private Comparator comparator;
  private String columnName;
  private String term;

  /**
   * A Constructor for a search operator.
   * @param comparator    A comparator to use for the search terms
   * @param columnName    A String that is the column name
   * @param term    A String that is the term of the query
   */
  public SearchOperator(Comparator comparator, String columnName, String term) {
    this.comparator = comparator;
    this.columnName = ColumnConverter.convert(columnName);
    this.term = term;
  }

  /**
   * Method gets the term of the query.
   * @return    A String that is the term of the query
   */
  String getTerm() {
    return this.term;
  }

  @Override
  public String toString() {
    switch (comparator) {
      case LESS_THAN:
        return this.columnName + " < ?";
      case GREATER_THAN:
        return this.columnName + " > ?";
      case LESS_THAN_OR_EQUALS:
        return this.columnName + " <= ?";
      case GREATER_THAN_OR_EQUALS:
        return this.columnName + " >= ?";
      case IS:
        return "instr(UPPER(" + this.columnName + "), UPPER(?)) > 0";
      default:
        return "";
    }
  }
}
