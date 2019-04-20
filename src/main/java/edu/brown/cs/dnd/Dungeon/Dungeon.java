package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;

import java.util.*;

/**
 * Class representing the dungeon and its utilities.
 */
public class Dungeon implements IDungeon {

  private List<AbsRoom> rooms;
  private boolean[][] occupiedCells;
  private int width;
  private int height;

  private static final int TOLERANCE = 20;

  public Dungeon(int width, int height) {
    this.width = width;
    this.height = height;
    this.occupiedCells = new boolean[height][width];
    this.rooms = new ArrayList<>();
    generateRooms(0.6, RoomSize.SMALL);
  }

  private void generateRooms(double dungeonDensity, RoomSize averageRoomSize) {
    assert dungeonDensity >= 0 && dungeonDensity < 1;
    Random rand = new Random();
    double areaUsed = 0;

    int numFailed = 0;
    while (areaUsed < dungeonDensity && numFailed < TOLERANCE) {
      Location loc = Location.randLocation(width, height);
      Room r = Room.randomRoom(getArea() * averageRoomSize.getRoomRatio(), loc);

      if (isValidRoom(r)) {
        rooms.add(r);
        fillCells(r);
        areaUsed += r.getArea() / ((double) this.height * (double) this.width);
      } else {
        numFailed++;
        System.out.println(numFailed);
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

  /**
   * Method sets the cells of the dungeon to true or false given a room.
   * @param r   A Room to fill the dungeon with
   */
  public void fillCells(Room r) {
    int x = r.getTopLeft().getX();
    int y = r.getTopLeft().getY();

    for (int i = y; i < y + r.getHeight(); i++) {
      for (int j = x; j < x + r.getWidth(); j++) {
        this.occupiedCells[i][j] = true;
      }
    }
  }

  /**
   * Method checks if the room can be inserted into the dungeon.
   * @param r   A Room that is the room to check for insertion
   * @return    A boolean that is true if the room can be inserted.
   */
  public boolean isValidRoom(Room r) {
    int x = r.getTopLeft().getX();
    int y = r.getTopLeft().getY();

    for (int i = y; i < y + r.getHeight(); i++) {
      for (int j = x; j < x + r.getWidth(); j++) {
        if (i >= this.height || j >= this.width) {
          return false;
        }

        if (this.occupiedCells[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  public void printDungeon() {
    for (int i = 0; i < this.occupiedCells.length; i++) {
      for (int j = 0; j < this.occupiedCells[i].length; j++) {
        if (this.occupiedCells[i][j]) {
          System.out.print("x ");
        } else {
          System.out.print(" ");
        }
      }
      System.out.println("");
    }
  }
}
