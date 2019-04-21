package edu.brown.cs.dnd.Data;

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

  // empty constructor for GSON serialization
  public Spell() {

  }

  public Spell(String name, String school, int level, boolean ritual,
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

  public String getName() {
    return name;
  }

  public String getSchool() {
    return school;
  }

  public int getLevel() {
    return level;
  }

  public boolean isRitual() {
    return ritual;
  }

  public String getRange() {
    return range;
  }

  public String getCastingTime() {
    return castingTime;
  }

  public boolean isVerbal() {
    return verbal;
  }

  public boolean isSomatic() {
    return somatic;
  }

  public boolean isConcentration() {
    return concentration;
  }

  public String getMaterials() {
    return materials;
  }

  public String getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }

  public String getClasses() {
    return classes;
  }
}
