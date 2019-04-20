package edu.brown.cs.dnd.Data;

public class StringResult implements QueryResult {
  private String result;

  public StringResult(String result) {
    this.result = result;
  }

  @Override
  public String prettify() {
    return result;
  }

  @Override
  public String simplify() {
    return result;
  }

  public String getResult() {
    return result;
  }
}
