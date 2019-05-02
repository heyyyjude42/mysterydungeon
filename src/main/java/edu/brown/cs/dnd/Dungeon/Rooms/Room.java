package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.RandomTools.NormalDistribution;

import java.util.Random;


/**
 * Class representing a room in the dungeon.
 */
public class Room extends AbsRoom {

  // Standard Deviation to use when generating rooms in the dungeon
  // based on a normal distribution
  private static final double STD_DEV_RATIO = 1.3;

  /**
   * A Constructor for a Room.
   * @param width   An int that is the width of the room
   * @param height    An int that is the height of the room
   * @param loc
   */
  public Room(int width, int height, Location loc) {
    super(width, height, loc);
  }

  @Override
  public String getSymbol() {
    return "x ";
  }

  /**
   * Method generates a random room from a normal distribution of the room's
   * area
   * @param averageArea   A double that is the average area of the room
   * @param loc   A Location that is the randomly-generated top-left corner of
   *              the room
   * @return    A Room that is the randomly-generated room
   */
  public static Room randomRoom(double averageArea, Location loc) {
    double averageDim = Math.sqrt(averageArea);
    NormalDistribution N = new
            NormalDistribution(averageDim, averageDim * STD_DEV_RATIO);
    int width = (int) Math.round(N.draw());
    int height = (int) Math.round(N.draw());

    if (width < 3) {
      width = 3;
    }

    if (height < 3) {
      height = 3;
    }

    return new Room(width, height, loc);
  }

}
