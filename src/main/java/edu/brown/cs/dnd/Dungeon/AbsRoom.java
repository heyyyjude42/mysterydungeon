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

  /**
   * Method gets the width of the room.
   * @return    An int that is the width of the room
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Method gets the height of the room.
   * @return    An int that is the height of the room
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Method gets the top left point of the room.
   * @return    A Location that is the top left point of the room
   */
  public Location getTopLeft() {
    return this.topLeftCorner;
  }
}
