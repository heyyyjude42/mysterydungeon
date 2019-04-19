package edu.brown.cs.dnd.Data;

public class SearchOperator {
  private Comparator comparator;
  private String columnName;
  private String term;

  public SearchOperator(Comparator comparator, String columnName, String term) {
    this.comparator = comparator;
    this.columnName = columnName;
    this.term = term;

    if (columnName.equals("name")) {
      this.term = capitalizeEveryWord(term);
    }
  }

  private String capitalizeEveryWord(String term) {
    String[] words = term.split(" ");
    String toReturn = "";

    for (String word : words) {
      toReturn += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    return toReturn.substring(0, toReturn.length() - 1); // removes the last
    // space
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
        return "instr(" + this.columnName + ", ?) > 0";
      default:
        return "";
    }
  }
}
