package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.RandomTools.NormalDistribution;


/**
 * Class representing a room in the dungeon.
 */
public class Room extends AbsRoom {

  private static final double STD_DEV_RATIO = 1.3;

  public Room(int width, int height, Location loc) {
    super(width, height, loc);
  }


  static Room randomRoom(double averageArea, Location loc) {
    double averageDim = Math.sqrt(averageArea);
    NormalDistribution N = new NormalDistribution(averageDim, averageDim * STD_DEV_RATIO);
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
