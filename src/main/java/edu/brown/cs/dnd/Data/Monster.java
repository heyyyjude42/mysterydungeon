package edu.brown.cs.dnd.Data;

import edu.brown.cs.dnd.Dungeon.Rooms.RoomElement;
import java.util.Map;

/**
 * Class representing a Monster in DnD.
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

  /**
   * An empty constructor for a Monster for JSON serialization.
   */
  Monster() {
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
    str += "STR " + this.str + "  |  DEX " + this.dex + "  |  CON " + this.con
            + "  |  INT " + this.intelligence + "  |  WIS " + this.wis
            + "  |  CHA "
            + this.cha + "\n";

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
        str += "* " + key.toUpperCase() + ": " + legendaryActions.get(key)
                + "\n";
      }
    }

    return str;
  }

  @Override
  public String simplify() {
    return this.name + " | CR " + this.cr;
  }

  /**
   * Method gets the name of the monster.
   * @return    A String that is the name of the monster
   */
  public String getName() {
    return name;
  }

  /**
   * Method gets the size of the monster.
   * @return    A String that is the size of the monster
   */
  public String getSize() {
    return size;
  }

  /**
   * Method gets the type of the monster.
   * @return    A String that is the type of the monster
   */
  public String getType() {
    return type;
  }

  /**
   * Method gets the alignment of the monster.
   * @return    A String that is the alignment of the monster
   */
  String getAlignment() {
    return alignment;
  }

  /**
   * Method gets the ac of the monster.
   * @return    An int that is the ac of the monster
   */
  int getAc() {
    return ac;
  }

  /**
   * Method gets the hp of the monster.
   * @return    An int that is the hp of the monster
   */
  int getHp() {
    return hp;
  }

  /**
   * Method gets the hp of the monster.
   * @return    A String that is the hp of the monster
   */
  public String getHpDice() {
    return hpDice;
  }

  /**
   * Method gets the speed of the monster.
   * @return    A String that is the speed of the monster
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Method gets the str of the monster.
   * @return    An int that is the str of the monster
   */
  public int getStr() {
    return str;
  }

  /**
   * Method gets the dex of the monster.
   * @return    An int that is the dex of the monster
   */
  int getDex() {
    return dex;
  }

  /**
   * Method gets the con of the monster.
   * @return    An int that is the con of the monster
   */
  int getCon() {
    return con;
  }

  /**
   * Method gets the int of the monster.
   * @return    An int that is the int of the monster
   */
  public int getIntelligence() {
    return intelligence;
  }

  /**
   * Method gets the wis of the monster.
   * @return    An int that is the wis of the monster
   */
  public int getWis() {
    return wis;
  }

  /**
   * Method gets the cha of the monster.
   * @return    An int that is the cha of the monster
   */
  int getCha() {
    return cha;
  }

  /**
   * Method gets the cr of the monster.
   * @return    A double that is the cr of the monster
   */
  double getCr() {
    return cr;
  }

  /**
   * Method gets the traits of the monster.
   * @return    A Map that contains the traits of the monster
   */
  public Map<String, String> getTraits() {
    return traits;
  }

  /**
   * Method gets the actions of the monster.
   * @return    A Map that contains the actions of the monster
   */
  public Map<String, String> getActions() {
    return actions;
  }

  /**
   * Method gets the leg actions of the monster.
   * @return    A Map that contains the leg actions of the monster
   */
  public Map<String, String> getLegendaryActions() {
    return legendaryActions;
  }

  /**
   * Method sets the name of the monster.
   * @param name    A String that is the name of the monster
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Method sets the size of the monster.
   * @param size    A String that is the size of the monster
   */
  public void setSize(String size) {
    this.size = size;
  }

  /**
   * Method sets the type of the monster.
   * @param type    A String that is the type of the monster
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Method sets the alignment of the monster.
   * @param alignment    A String that is the alignment of the monster
   */
  public void setAlignment(String alignment) {
    this.alignment = alignment;
  }

  /**
   * Method sets the ac of the monster.
   * @param ac    A String that is the ac of the monster
   */
  public void setAc(int ac) {
    this.ac = ac;
  }

  /**
   * Method sets the hp of the monster.
   * @param hp    A String that is the hp of the monster
   */
  public void setHp(int hp) {
    this.hp = hp;
  }

  /**
   * Method sets the hp dice of the monster.
   * @param hpDice    A String that is the hp dice of the monster
   */
  public void setHpDice(String hpDice) {
    this.hpDice = hpDice;
  }

  /**
   * Method sets the speed of the monster.
   * @param speed    A String that is the speed of the monster
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Method sets the str of the monster.
   * @param str    An int that is the str of the monster
   */
  public void setStr(int str) {
    this.str = str;
  }

  /**
   * Method sets the dex of the monster.
   * @param dex    An int that is the dex of the monster
   */
  public void setDex(int dex) {
    this.dex = dex;
  }

  /**
   * Method sets the con of the monster.
   * @param con    An int that is the con of the monster
   */
  public void setCon(int con) {
    this.con = con;
  }

  /**
   * Method sets the intelligence of the monster.
   * @param intelligence    An int that is the intelligence of the monster
   */
  public void setIntelligence(int intelligence) {
    this.intelligence = intelligence;
  }

  /**
   * Method sets the wis of the monster.
   * @param wis    A String that is the wis of the monster
   */
  public void setWis(int wis) {
    this.wis = wis;
  }

  /**
   * Method sets the cha of the monster.
   * @param cha    A String that is the cha of the monster
   */
  public void setCha(int cha) {
    this.cha = cha;
  }

  /**
   * Method sets the cr of the monster.
   * @param cr    A String that is the cr of the monster
   */
  public void setCr(double cr) {
    this.cr = cr;
  }

  /**
   * Method sets the background of the monster.
   * @param background    A String that is the background of the monster
   */
  public void setBackground(String background) {
    this.background = background;
  }
}
