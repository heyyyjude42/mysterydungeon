package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

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
