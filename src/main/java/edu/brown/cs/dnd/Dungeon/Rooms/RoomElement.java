package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;

/**
 * Class representing a room element such as a Monster, Trap, etc.
 */
public abstract class RoomElement {

  // A relative location to the room
  protected Location coordinate;

  /**
   * A constructor for a RoomElement.
   * @param coordinate - a Location, the coordinate of this element relative to the room it's in.
   */
  public RoomElement(Location coordinate) {
    this.coordinate = coordinate;
  }
}
