package edu.brown.cs.dnd.Data;

import java.util.Objects;
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

  public double distanceTo(Location that) {
    return Math.sqrt(Math.pow(this.x - that.x, 2.0) + Math.pow(this.y - that.y, 2.0));
  }

  public Location plus(Location that) {
    return new Location(this.x + that.x, this.y + that.y);
  }

  public Location minus(Location that) {
    return new Location(this.x - that.x, this.y - that.y);
  }

  public Location addX(int toAdd) {
    return new Location(this.x + toAdd, this.y);
  }

  public Location addY(int toAdd) {
    return new Location(this.x, this.y + toAdd);
  }

  public Location minusX(int toAdd) {
    return new Location(this.x - toAdd, this.y);
  }

  public Location minusY(int toAdd) {
    return new Location(this.x, this.y - toAdd);
  }

  @Override
  public String toString() {
    return "Location{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return x == location.x &&
            y == location.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
