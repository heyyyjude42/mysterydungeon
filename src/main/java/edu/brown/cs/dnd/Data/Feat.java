package edu.brown.cs.dnd.Data;

/**
 * Class representing a Feat in DnD.
 */
public class Feat implements QueryResult {
  private String name;
  private String desc;

  // empty constructor for GSON serialization
  Feat() {

  }

  /**
   * A Constructor for a Feat.
   * @param name    A String that is the feat's name
   * @param desc    A String that is the desc. of the feat
   */
  Feat(String name, String desc) {
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

  String getDesc() {
    return desc;
  }

  /**
   * Method gets the name of the Feat.
   * @return    A String that is the name of the Feat
   */
  public String getName() {
    return name;
  }

}
