package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;

/**
 * Class representing a path in the dungeon.
 */
public class Path extends AbsRoom {
  public Path(int width, int height, Location loc) {
    super(width, height, loc);
  }

  @Override
  public String getSymbol() {
    return "o ";
  }
}
