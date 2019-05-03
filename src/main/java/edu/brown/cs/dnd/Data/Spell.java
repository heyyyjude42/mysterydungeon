package edu.brown.cs.dnd.Data;

/**
 * Class representing a spell in DnD.
 */
public class Spell implements QueryResult {
  private String name;
  private String school;
  private int level;
  private boolean ritual;
  private String range;
  private String castingTime;
  private boolean verbal;
  private boolean somatic;
  private boolean concentration;
  private String materials;
  private String duration;
  private String description;
  private String classes;

  /**
   * An empty constructor for a spell for GSON serialization.
   */
  public Spell() {

  }

  /**
   * A Constructor for a Spell.
   * @param name    A String that is the name of the spell
   * @param school    A String that is the school of the spell
   * @param level   An int that is the level of the spell
   * @param ritual    A boolean that is the ritual of the spell
   * @param range   A String that is the range of the spell
   * @param castingTime   A String that is the casting time of the spell
   * @param verbal    A boolean that is true if the spell is verbal
   * @param somatic   A boolean that is true if the spell is somatic
   * @param concentration   A boolean that is true if the spell is a
   *                        concentration spell
   * @param materials   A String that are the materials of the spell
   * @param duration    A String that is the duration of the spell
   * @param description   A String that is the description of the spell
   * @param classes   A String that are the classes of the spell
   */
  Spell(String name, String school, int level, boolean ritual,
               String range, String castingTime, boolean verbal,
               boolean somatic, boolean concentration, String materials,
               String duration, String description, String classes) {
    this.name = name;
    this.school = school;
    this.level = level;
    this.ritual = ritual;
    this.range = range;
    this.castingTime = castingTime;
    this.verbal = verbal;
    this.somatic = somatic;
    this.concentration = concentration;
    this.materials = materials;
    this.duration = duration;
    this.description = description;
    this.classes = classes;
  }

  @Override
  public String prettify() {
    String str = "";
    str += this.name.toUpperCase() + "\n";

    if (level > 0) {
      str += this.level + "th level " + this.school + "\n";
    } else {
      str += this.school + " cantrip" + "\n";
    }

    str += "Casting Time: " + castingTime + "\n";
    str += "Range:        " + range + "\n";

    str += "Components:   ";
    if (this.verbal) {
      str += "V ";
    }
    if (this.somatic) {
      str += "S ";
    }
    if (!this.materials.equals("None")) {
      str += "M (" + this.materials + ")";
    }
    str += "\n";

    str += "Duration:     ";
    if (this.concentration) {
      str += "Concentration, ";
    }
    str += this.duration + "\n";

    str += "Classes:      " + this.classes + "\n";
    str += this.description + ".";

    return str;
  }

  @Override
  public String simplify() {
    return this.name;
  }

  /**
   * Method gets the name of the spell.
   * @return    A String that is the name of the spell
   */
  public String getName() {
    return name;
  }

  /**
   * Method gets the school of the spell.
   * @return    A String that is the school of the spell
   */
  public String getSchool() {
    return school;
  }

  /**
   * Method gets the level of the spell.
   * @return    A String that is the level of the spell
   */
  public int getLevel() {
    return level;
  }

  /**
   * Method gets the ritual of the spell.
   * @return    A String that is the ritual of the spell
   */
  public boolean isRitual() {
    return ritual;
  }

  /**
   * Method gets the range of the spell.
   * @return    A String that is the range of the spell
   */
  String getRange() {
    return range;
  }

  /**
   * Method gets the casting time of the spell.
   * @return    A String that is the casting time of the spell
   */
  String getCastingTime() {
    return castingTime;
  }

  /**
   * Method gets whether the spell is verbal.
   * @return    A boolean that is true if the spell is verbal
   */
  public boolean isVerbal() {
    return verbal;
  }

  /**
   * Method gets whether the spell is somatic.
   * @return    A boolean that is true if the spell is somatic
   */
  public boolean isSomatic() {
    return somatic;
  }

  /**
   * Method gets whether the spell is a concentration spell or not.
   * @return    A boolean that is true if the spell is a concentration spell
   */
  public boolean isConcentration() {
    return concentration;
  }

  /**
   * Method gets the materials of the spell.
   * @return    A String that is the materials of the spell
   */
  public String getMaterials() {
    return materials;
  }

  /**
   * Method gets the duration of the spell.
   * @return    A String that is the duration of the spell
   */
  public String getDuration() {
    return duration;
  }

  /**
   * Method gets the description of the spell.
   * @return    A String that is the description of the spell
   */
  public String getDescription() {
    return description;
  }

  /**
   * Method gets the classes of the spell.
   * @return    A String that is the classes of the spell
   */
  public String getClasses() {
    return classes;
  }
}
