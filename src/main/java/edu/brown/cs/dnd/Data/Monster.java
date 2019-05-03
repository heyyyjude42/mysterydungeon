package edu.brown.cs.dnd.Data;

import edu.brown.cs.dnd.Dungeon.Rooms.RoomElement;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing a Monster in D&D.
 */
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
  private Map<String, String> traits;
  private Map<String, String> actions;
  private Map<String, String> legendaryActions;
  private String background;

  // empty constructor for GSON serialization
  public Monster() {
  }

  /**
   * A Constructor for a Monster.
   * @param name    A String that is the name of the monster
   * @param size    A String that is the size of the monster
   * @param type    A String that is the type of the monster
   * @param alignment   A String that is the alignment of the monster
   * @param ac    An int that is the ac, or energy, or the monster
   * @param hp    An int that is the hp of the monster
   * @param hpDice    A String that is the hp dice of the monster
   * @param speed   A String that is the speed of the monster
   * @param str   An int that is the strength of the monster
   * @param dex   An int that is the dexterity of the monster
   * @param con   An int that is the con of the monster
   * @param intelligence    An int that is the intelligence of the monster
   * @param wis   An int that is the wisdom of the monster
   * @param cha   An int that is the charisma of the monster
   * @param cr    An int that is the cr of the monster
   * @param traits    A Map that contains the traits of the monster
   * @param actions   A Map that contains the actions of the monster
   * @param legendaryActions    A Map that contains the legendary actions
   *                            of the monster
   */
  public Monster(String name, String size, String type, String alignment,
                 int ac, int hp, String hpDice, String speed, int str,
                 int dex, int con, int intelligence, int wis, int cha,
                 double cr,
                 Map<String, String> traits,
                 Map<String, String> actions,
                 Map<String, String> legendaryActions) {
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
    str += "STR " + this.str + "  |  DEX " + this.dex + "  |  CON " + this.con +
        "  |  INT " + this.intelligence + "  |  WIS " + this.wis + "  |  CHA " +
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

  public Map<String, String> getTraits() {
    return traits;
  }

  public Map<String, String> getActions() {
    return actions;
  }

  public Map<String, String> getLegendaryActions() {
    return legendaryActions;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setAlignment(String alignment) {
    this.alignment = alignment;
  }

  public void setAc(int ac) {
    this.ac = ac;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public void setHpDice(String hpDice) {
    this.hpDice = hpDice;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public void setStr(int str) {
    this.str = str;
  }

  public void setDex(int dex) {
    this.dex = dex;
  }

  public void setCon(int con) {
    this.con = con;
  }

  public void setIntelligence(int intelligence) {
    this.intelligence = intelligence;
  }

  public void setWis(int wis) {
    this.wis = wis;
  }

  public void setCha(int cha) {
    this.cha = cha;
  }

  public void setCr(double cr) {
    this.cr = cr;
  }

  public void setBackground(String background) {
    this.background = background;
  }
}
