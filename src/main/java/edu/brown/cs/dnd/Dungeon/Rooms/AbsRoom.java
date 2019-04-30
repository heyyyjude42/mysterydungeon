package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * An open space in a dungeon. May contain traps, monsters, doors, etc.
 */
public abstract class AbsRoom {

  private Location topLeftCorner;
  private int width;
  private int height;
  private List<RoomElement> elements;

  private static final int TRAP_FREQ_RATIO = 100;


  public AbsRoom(int width, int height, Location loc) {
    this.width = width;
    this.height = height;
    this.topLeftCorner = loc;
    this.elements = new LinkedList<>();
    this.addTraps(5);
  }

  public double distanceTo(AbsRoom that) {
    return this.getMidpoint().distanceTo(that.getMidpoint());
  }

  public Location getMidpoint() {
    int midX = Math.round((float) (this.topLeftCorner.getX() + .5 * this.width));
    int midY = Math.round((float) (this.topLeftCorner.getY() + .5 * this.height));
    return new Location(midX, midY);
  }

  /**
   * Adds the provided element to this rooms elements.
   * @param toAdd - the RoomElement to add.
   */
  protected void addElement(RoomElement toAdd) {
    this.elements.add(toAdd);
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

  /**
   * Method gets the bottom right point of the room.
   * @return    A Location that is the bottom right point of the room
   */
  public Location getBottomRight() {
    return this.topLeftCorner.addX(width - 1).minusY(height - 1);
  }

  /**
   * Method gets the top right point of the room.
   * @return    A Location that is the top right point of the room
   */
  public Location getTopRight() {
    return this.topLeftCorner.addX(width - 1);
  }

  /**
   * Method gets the top right point of the room.
   * @return    A Location that is the top right point of the room
   */
  public Location getBottomLeft() {
    return this.topLeftCorner.minusY(height - 1);
  }

  /**
   * Returns a symbol for this room.
   * @return - the symbol, a String.
   */
  public abstract String getSymbol();

  /**
   * Adds traps to this room.
   * @param level - the level of the traps.
   */
  protected void addTraps(int level) {
    Random rand = new Random();
    for (int r = 0; r < this.getHeight(); r++) {
      for (int c = 0; c < this.getWidth(); c++) {
        int rng = rand.nextInt(TRAP_FREQ_RATIO);
        if (rng == 0) {
          Trap t = Trap.randomTrap(level, new Location(c, r));
          this.addElement(t);
        }
      }
    }
  };

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbsRoom absRoom = (AbsRoom) o;
    return width == absRoom.width &&
            height == absRoom.height &&
            Objects.equals(topLeftCorner, absRoom.topLeftCorner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topLeftCorner, width, height);
  }

  @Override
  public String toString() {
    return "AbsRoom{" +
            "topLeftCorner=" + topLeftCorner +
            ", width=" + width +
            ", height=" + height +
            ", elements=" + elements +
            '}';
  }
}
