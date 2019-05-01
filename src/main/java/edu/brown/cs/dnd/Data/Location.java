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

  /**
   * Method gets the distance to a given location.
   * @param that    A Location that is the location to find the distance to
   * @return    A double that is the distance to the specified location
   */
  public double distanceTo(Location that) {
    return Math.sqrt(Math.pow(this.x - that.x, 2.0) + Math.pow(this.y - that.y, 2.0));
  }

  /**
   * Method returns a new location with the coordinates of the current location
   * added to those of the location passed in.
   * @param that    A Location that is the location to add its coordinates to
   * @return    A Location whose coordinates are the summed coordinates of the
   * two locations
   */
  public Location plus(Location that) {
    return new Location(this.x + that.x, this.y + that.y);
  }

  /**
   * Method returns a new location with the coordinates of the current location
   * subtracted from those of the location passed in.
   * @param that    A Location that is the location to to subtract
   *                its coordinates from
   * @return    A Location whose coordinates are the subtracted coordinates of
   * the two locations
   */
  public Location minus(Location that) {
    return new Location(this.x - that.x, this.y - that.y);
  }

  /**
   * Method returns a new location with the X coordinate of the current location
   * added to that of the location passed in.
   * @param toAdd    A Location that is the location to add its X coordinate to
   * @return    A Location whose coordinates are the summed X coordinates of the
   * two locations
   */
  public Location addX(int toAdd) {
    return new Location(this.x + toAdd, this.y);
  }

  /**
   * Method returns a new location with the Y coordinate of the current location
   * added to that of the location passed in.
   * @param toAdd    A Location that is the location to add its Y coordinate to
   * @return    A Location whose coordinates are the summed Y coordinates of the
   * two locations
   */
  public Location addY(int toAdd) {
    return new Location(this.x, this.y + toAdd);
  }

  /**
   * Method returns a new location with the X coordinate of the passed in
   * location subtracted from that of the current location
   * @param toAdd    A Location that is the location to subtract its X
   *                 coordinate from the current
   * @return    A Location whose coordinates are the subtracted X coordinates
   * of the two locations
   */
  public Location minusX(int toAdd) {
    return new Location(this.x - toAdd, this.y);
  }

  /**
   * Method returns a new location with the Y coordinate of the passed in
   * location subtracted from that of the current location
   * @param toAdd    A Location that is the location to subtract its Y
   *                 coordinate from the current
   * @return    A Location whose coordinates are the subtracted Y coordinates
   * of the two locations
   */
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
