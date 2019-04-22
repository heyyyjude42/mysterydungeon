package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

import java.util.List;
import java.util.Objects;

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

  public double distanceTo(AbsRoom that) {
    return this.getMidpoint().distanceTo(that.getMidpoint());
  }

  public Location getMidpoint() {
    int midX = Math.round((float) (this.topLeftCorner.getX() + .5 * this.width));
    int midY = Math.round((float) (this.topLeftCorner.getY() + .5 * this.height));
    return new Location(midX, midY);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbsRoom absRoom = (AbsRoom) o;
    return width == absRoom.width &&
            height == absRoom.height &&
            Objects.equals(topLeftCorner, absRoom.topLeftCorner) &&
            Objects.equals(elements, absRoom.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topLeftCorner, width, height, elements);
  }
}
