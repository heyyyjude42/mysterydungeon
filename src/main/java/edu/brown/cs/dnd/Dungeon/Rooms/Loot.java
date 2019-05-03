package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * A class to represent loot in DnD.
 */
public class Loot extends RoomElement {
  // The elements in this loot "chest".
  private List<ILootElement> contents;
  // The position of this Loot relative to its room.
  private Location position;

  /**
   * Contruct a Loot.
   * @param pos - a Location, the position of this loot relative to its room.
   * @param contents - a list of ILootElmeents, the contents
   */
  public Loot(Location pos, List<ILootElement> contents) {
    this.contents = contents;
    this.position = pos;

  }

  /**
   * generate a random loot.
   * @param pos - a Location, the position of this loot relative to its room.
   * @return - the loot.
   */
  public static Loot randomLoot(Location pos) {
    Money coin = Money.randomMoney();
    List<ILootElement> contents = new LinkedList<>();
    contents.add(coin);
    return new Loot(pos, contents);
  }

  @Override
  public String toString() {
    return "Loot{"
            + "contents=" + contents
            + ", position=" + position
            + '}';
  }
}
