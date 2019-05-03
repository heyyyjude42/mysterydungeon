package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;

/**
 * Class representing a room element such as a Monster, Trap, etc.
 */
public abstract class RoomElement {

  // A relative location to the room
  private Location coordinate;

  /**
   * A constructor for a RoomElement.
   */
  protected RoomElement() {
  }
}
