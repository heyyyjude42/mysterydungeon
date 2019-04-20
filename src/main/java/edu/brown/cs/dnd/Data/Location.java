package edu.brown.cs.dnd.Data;

import java.util.Random;

/**
 * A dungeon location, an x,y pair.
 */
public class Location {

  private int x;

  private int y;

  /**
   * Constructs a new location.
   * @param x - the x coordinate.
   * @param y - the y coordinate.
   */
  public Location(int x, int y) {
    this.x = x;
    this.y = y;

  }

  /**
   * Get x.
   * @return - x.
   */
  public int getX() {
    return this.x;
  }

  /**
   * Get y.
   * @return - y.
   */
  public int getY() {
    return y;
  }

  /**
   * return a random location (uniform) with the given bounds.
   * @param xBound - the bound for x.
   * @param yBound - the bound for y.
   * @return - the new location.
   */
  public static Location randLocation(int xBound, int yBound) {
    Random rand = new Random();
    return new Location(rand.nextInt(xBound), rand.nextInt(yBound));
  }

  @Override
  public String toString() {
    return "Location{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
