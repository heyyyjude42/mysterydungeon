package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

import java.util.List;
import java.util.Random;

/**
 * Class representing the dungeon and its utilities.
 */
public class Dungeon implements IDungeon {

  private List<AbsRoom> rooms;
  private int width;
  private int height;

  public Dungeon(int width, int height) {
    this.width = width;
    this.height = height;

  }

  private void generateRooms(double dungeonDensity, RoomSize averageRoomSize) {
    assert dungeonDensity >= 0 && dungeonDensity < 1;
    Random rand = new Random();
    double areaUsed = 0;
    while (areaUsed < dungeonDensity) {
      Location loc = Location.randLocation(width, height);
      Room r = Room.randomRoom(getArea() * averageRoomSize.getRoomRatio(), loc);
      // Replace this with the condition for if the room does not intersect any already existing rooms.
      if (true) {
        rooms.add(r);
        areaUsed += r.getArea() / ((double) this.height * (double) this.width);
      }
    }
  }


  @Override
  public List<Room> getRooms() {
    return null;
  }

  @Override
  public int getArea() {
    return width * height;
  }
}
