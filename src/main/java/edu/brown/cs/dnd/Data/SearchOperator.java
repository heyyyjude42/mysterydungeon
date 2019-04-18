package edu.brown.cs.dnd.Data;

public class SearchOperator {
  private Comparator comparator;
  private String columnName;
  private String term;
  private boolean termIsString;

  public SearchOperator(Comparator comparator, String columnName, String term,
                        boolean termIsString) {
    this.comparator = comparator;
    this.columnName = columnName;
    this.term = term;
    this.termIsString = termIsString;
  }

  public boolean isTermString() {
    return this.termIsString;
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
      case CONTAINS:
        return "instr(" + this.columnName + ", \"" + this.term + "\") > 0";
      case IS:
        return this.columnName + " = " + (!termIsString ? this.term :
            "?"); // insert later in PreparedStatement
      default:
        return "";
    }
  }
}
