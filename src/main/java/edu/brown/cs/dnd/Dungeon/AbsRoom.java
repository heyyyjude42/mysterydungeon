package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

import java.util.List;

/**
 * An open space in a dungeon. May contain traps, monsters, doors, etc.
 */
public class AbsRoom {

  private Location topLeftCorner;
  private int width;
  private int height;
  private List<RoomElement> elements;

  public AbsRoom(int width, int height, Location loc) {
    this.width = width;
    this.height = height;
    this.topLeftCorner = loc;
  }

  public int getArea() {return width * height; }
}
