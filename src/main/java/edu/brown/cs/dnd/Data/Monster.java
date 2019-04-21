package edu.brown.cs.dnd.Data;

import edu.brown.cs.dnd.Dungeon.RoomElement;

import java.util.HashMap;

public class Monster extends RoomElement implements QueryResult {
  private String name;
  private String size;
  private String type;
  private String alignment;
  private int ac;
  private int hp;
  private String hpDice;
  private String speed;
  private int str;
  private int dex;
  private int con;
  private int intelligence;
  private int wis;
  private int cha;
  private double cr;
  private HashMap<String, String> traits;
  private HashMap<String, String> actions;
  private HashMap<String, String> legendaryActions;

  // empty constructor for GSON serialization
  public Monster() {
  }

  public Monster(String name, String size, String type, String alignment,
                 int ac, int hp, String hpDice, String speed, int str,
                 int dex, int con, int intelligence, int wis, int cha,
                 double cr,
                 HashMap<String, String> traits,
                 HashMap<String, String> actions,
                 HashMap<String, String> legendaryActions) {
    this.name = name;
    this.size = size;
    this.type = type;
    this.alignment = alignment;
    this.ac = ac;
    this.hp = hp;
    this.hpDice = hpDice;
    this.speed = speed;
    this.str = str;
    this.dex = dex;
    this.con = con;
    this.intelligence = intelligence;
    this.wis = wis;
    this.cha = cha;
    this.cr = cr;
    this.traits = traits;
    this.actions = actions;
    this.legendaryActions = legendaryActions;
  }

  @Override
  public String prettify() {
    String str = "";
    str += this.name.toUpperCase() + "\n";
    str += this.size + " " + this.type + ", " + this.alignment + "\n";

    str += "—————\n";
    str += "Armor Class:  " + this.ac + "\n";
    str += "Hit Points:   " + this.hp + " (" + this.hpDice + ")\n";
    str += "Speed:        " + this.speed + "\n";

    str += "—————\n";
    str += "STR " + this.str + " | DEX " + this.dex + " | CON " + this.con +
        " | INT " + this.intelligence + " | WIS + " + this.wis + " | CHA " +
        this.cha + "\n";

    str += "—————\n";
    str += "Challenge:    " + this.cr + "\n";

    if (traits != null) {
      for (String key : traits.keySet()) {
        str += "* " + key.toUpperCase() + ": " + traits.get(key) + "\n";
      }
    }

    str += "—————\n";
    str += "ACTIONS\n";
    str += "—————\n";

    if (actions != null) {
      for (String key : actions.keySet()) {
        str += "* " + key.toUpperCase() + ": " + actions.get(key) + "\n";
      }
    }

    // only add legendary actions if they actually exist for this monster
    if (legendaryActions != null && legendaryActions.size() > 0) {
      str += "—————\n";
      str += "LEGENDARY ACTIONS\n";
      str += "—————\n";

      for (String key : legendaryActions.keySet()) {
        str += "* " + key.toUpperCase() + ": " + legendaryActions.get(key) +
            "\n";
      }
    }

    return str;
  }

  @Override
  public String simplify() {
    return this.name + " | CR " + this.cr;
  }

  public String getName() {
    return name;
  }

  public String getSize() {
    return size;
  }

  public String getType() {
    return type;
  }

  public String getAlignment() {
    return alignment;
  }

  public int getAc() {
    return ac;
  }

  public int getHp() {
    return hp;
  }

  public String getHpDice() {
    return hpDice;
  }

  public String getSpeed() {
    return speed;
  }

  public int getStr() {
    return str;
  }

  public int getDex() {
    return dex;
  }

  public int getCon() {
    return con;
  }

  public int getIntelligence() {
    return intelligence;
  }

  public int getWis() {
    return wis;
  }

  public int getCha() {
    return cha;
  }

  public double getCr() {
    return cr;
  }

  public HashMap<String, String> getTraits() {
    return traits;
  }

  public HashMap<String, String> getActions() {
    return actions;
  }

  public HashMap<String, String> getLegendaryActions() {
    return legendaryActions;
  }
}
