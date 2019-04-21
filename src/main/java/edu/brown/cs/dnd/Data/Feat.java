package edu.brown.cs.dnd.Data;

public class Feat implements QueryResult {
  private String name;
  private String desc;

  // empty constructor for GSON serialization
  public Feat() {

  }

  public Feat(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  @Override
  public String prettify() {
    return this.name.toUpperCase() + "\n" + this.desc;
  }

  @Override
  public String simplify() {
    return this.name;
  }

  public String getDesc() {
    return desc;
  }

  public String getName() {
    return name;
  }

}
