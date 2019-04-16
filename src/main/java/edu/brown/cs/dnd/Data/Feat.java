package edu.brown.cs.dnd.Data;

public class Feat implements QueryResult {
  private String name;
  private String desc;

  public Feat(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  @Override
  public String prettify() {
    return this.name.toUpperCase() + "\n" + this.desc;
  }

  public String getDesc() {
    return desc;
  }

  public String getName() {
    return name;
  }

}
