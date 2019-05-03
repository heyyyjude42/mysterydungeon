package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;

/**
 * Class representing a path in the dungeon.
 */
public class Path extends AbsRoom {

  /**
   * A Constructor for a Path.
   * @param width   An int that is the width of the path
   * @param height    An int that is the height of the path
   * @param loc   A Location that is the top-left corner of the path
   */
  public Path(int width, int height, Location loc) {
    super(width, height, loc);
  }

  @Override
  public String getSymbol() {
    return "o ";
  }

}
