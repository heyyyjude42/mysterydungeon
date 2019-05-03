package edu.brown.cs.dnd.Data;

/**
 * Takes in an input like "class" and converts it to what the actual SQL
 * column name is called, which would be "classes".
 */
final class ColumnConverter {

  /**
   * A Default Constructor for a ColumnConverter.
   */
  private ColumnConverter() {

  }

  static String convert(String column) {
    switch (column.toLowerCase().replace("_", "")) {
      case "class":
        return "classes";
      case "strength":
        return "str";
      case "dexterity":
        return "dex";
      case "constitution":
        return "con";
      case "wisdom":
        return "wis";
      case "intelligence":
        return "int";
      case "charisma":
        return "cha";
      case "hitpoints":
        return "hp";
      case "challenge":
        return "cr";
      case "trait":
        return "traits";
      case "action":
        return "actions";
      case "legendaryaction":
        return "legendaryActions";
      case "armorclass":
        return "ac";
      case "description":
        return "desc";
      case "material":
        return "materials";
      case "v":
        return "verbal";
      case "c":
        return "concentration";
      case "s":
        return "somatic";
      case "time":
        return "castingTime";
      default:
        return column; // no changes
    }
  }
}
