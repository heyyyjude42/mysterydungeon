package edu.brown.cs.dnd.Data;

public class SearchOperator {
  private Comparator comparator;
  private String columnName;
  private String term;

  public SearchOperator(Comparator comparator, String columnName, String term) {
    this.comparator = comparator;
    this.columnName = columnName;
    this.term = term;
  }

  public String getTerm() {
    return this.term;
  }

  @Override
  public String toString() {
    switch (comparator) {
      case LESS_THAN:
        return this.columnName + " < " + this.term;
      case GREATER_THAN:
        return this.columnName + " > " + this.term;
      case LESS_THAN_OR_EQUALS:
        return this.columnName + " <= " + this.term;
      case GREATER_THAN_OR_EQUALS:
        return this.columnName + " >= " + this.term;
      case IS:
        return "instr(UPPER(" + this.columnName + "), UPPER(?)) > 0";
      default:
        return "";
    }
  }
}
