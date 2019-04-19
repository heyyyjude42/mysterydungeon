package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

import java.util.List;
import java.util.Random;

/**
 * Class representing the dungeon and its utilities.
 */
public class Dungeon extends IDungeon {

  private List<AbsRoom> rooms;
  private int width;
  private int height;

  public Dungeon(int width, int height) {
    this.width = width;
    this.height = height;

  }

  private void generateRooms() {
    Random rand = new Random();
    double areaUsed = 0;
    while (areaUsed < .6) {
      Room r = new Room(rand.nextInt(Math.floorDiv(width, 10)),
              rand.nextInt(Math.floorDiv(height, 10)),
              Location.randLocation(width, height));
      rooms.add(r);
      areaUsed += r.getArea() / (this.height * this.width);
    }
  }
}
