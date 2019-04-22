package edu.brown.cs.dnd.Dungeon;

import com.sun.javafx.geom.Edge;
import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Graph.UndirectedGraph;
import edu.brown.cs.dnd.Dungeon.Graph.UndirectedEdge;

import java.util.*;

/**
 * Class representing the dungeon and its utilities.
 */
public class Dungeon implements IDungeon {

  private List<AbsRoom> rooms;
  private boolean[][] occupiedCells;
  private int width;
  private int height;
  private Random rand;

  private static final int TOLERANCE = 100;
  private static final double MAIN_ROOM_FACTOR = 1.25;
  private static final int RAND_ROOM_LEVEL = 50;

  public Dungeon(int width, int height) {
    this.width = width;
    this.height = height;
    this.occupiedCells = new boolean[height][width];
    this.rand = new Random();
    this.rooms = new ArrayList<>();
    generateRooms(0.8, RoomSize.SMALL);
    filterRooms();
    connectRooms();
  }

  private void generateRooms(double dungeonDensity, RoomSize averageRoomSize) {
    assert dungeonDensity >= 0 && dungeonDensity < 1;
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

  private void filterRooms() {
    double totalRoomArea = 0;
    for (AbsRoom r : rooms) {
      totalRoomArea += r.getArea();
    }
    double averageRoomSize = totalRoomArea / (double) rooms.size();
    for (AbsRoom r : rooms) {
      if (r.getArea() < averageRoomSize * MAIN_ROOM_FACTOR) {
        if (rand.nextInt(100) < RAND_ROOM_LEVEL) {
          rooms.remove(r);
        }
      }
    }
  }

  private void connectRooms() {
    UndirectedGraph<AbsRoom> roomUndirectedGraph = new UndirectedGraph<>();
    for (AbsRoom r1 : rooms) {
      for (AbsRoom r2 : rooms) {
        if (!r1.equals(r2)) {
          roomUndirectedGraph.addEdge(r1, r2, r1.distanceTo(r2));
        }
      }
    }
    UndirectedGraph<AbsRoom> mst = roomUndirectedGraph.mst();
    for (UndirectedEdge<AbsRoom> e : mst.getEdges()) {
      Collection<AbsRoom> toAdd = getPathFromEdge(e);
      rooms.addAll(toAdd);

      for (AbsRoom room : toAdd) {
        fillCells(room);
      }
    }
  }

  private Collection<AbsRoom> getPathFromEdge(UndirectedEdge<AbsRoom> edge) {
    Set<AbsRoom> result = new HashSet<>();
    AbsRoom r1 = edge.getV1();
    AbsRoom r2 = edge.getV2();

    int pathSize = this.rand.nextInt(Math.min(Math.min(r1.getWidth(),
            r1.getHeight()), Math.min(r2.getWidth(), r2.getHeight())) - 1) + 1;

    AbsRoom higher = r1.getTopLeft().getY() >= r2.getTopLeft().getY()
            ? r1 : r2;
    AbsRoom lower = r1.getTopLeft().getY() < r2.getTopLeft().getY()
            ? r1: r2;

    AbsRoom leftMost = r1.getTopLeft().getX() <= r2.getTopLeft().getX()
            ? r1 : r2;
    AbsRoom rightMost = r1.getTopLeft().getX() > r2.getTopLeft().getX()
            ? r1 : r2;

    int b = higher.getTopLeft().getY() - higher.getHeight();
    int c = lower.getTopLeft().getY();

    int a = leftMost.getTopLeft().getX() + leftMost.getWidth();
    int d = rightMost.getTopLeft().getX();

    if (c - b > pathSize) {
      // Generates a random number between b + pathSize and c inclusive
      Location pathTopLeft = new Location(leftMost.getTopLeft().getX()
              + leftMost.getWidth(),
              rand.nextInt(c - b - pathSize + 1) + b + pathSize);
      Path p = new Path(rightMost.getTopLeft().getX()
              - leftMost.getTopLeft().getX()
              + leftMost.getWidth(), pathSize, pathTopLeft);
      result.add(p);
    } else if (a - d > pathSize){
      Location pathTopLeft = new Location(
              rand.nextInt(a - d - pathSize + 1) + d + pathSize,
              higher.getBottomRight().getY());
      Path p = new Path(pathSize, higher.getBottomRight().getY()
              - lower.getTopLeft().getY(), pathTopLeft);
      result.add(p);
    } else {
      result.addAll(getExtendedPath(leftMost, rightMost, pathSize));
    }

    return result;
  }

  /**
   * Method finds a horizontal connection of length two between two rooms.
   * @param leftMost    An AbsRoom that is the leftmost room
   * @param rightMost   An Absroom that is the rightmost room
   * @param pathSize    An int that is the path size
   * @return    A Set of Rooms that are the connection of length two
   */
  public Set<AbsRoom> getExtendedPath(AbsRoom leftMost, AbsRoom rightMost,
                                      int pathSize) {
    Set<AbsRoom> result = new HashSet<>();

    AbsRoom path1 = new Path(rightMost.getTopLeft().getX()
            - leftMost.getBottomRight().getX()
            + rand.nextInt(rightMost.getWidth() - pathSize), pathSize,
            new Location(leftMost.getBottomRight().getX(),
                    rand.nextInt(leftMost.getTopLeft().getY()
                            - leftMost.getBottomRight().getY() - pathSize)
                            + leftMost.getBottomRight().getY() + pathSize));

    AbsRoom path2 = new Path(pathSize, path1.getBottomRight().getY()
            - rightMost.getTopLeft().getY(),
            new Location(path1.getBottomRight().getX() + 1,
                    path1.getTopLeft().getY()));

    result.add(path1);
    result.add(path2);

    return result;
  }




  @Override
  public List<AbsRoom> getRooms() {
    return this.rooms;
  }

  @Override
  public int getArea() {
    return width * height;
  }

  /**
   * Method sets the cells of the dungeon to true or false given a room.
   * @param r   An Absroom to fill the dungeon with
   */
  public void fillCells(AbsRoom r) {
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
          System.out.print("  ");
        }
      }
      System.out.println("");
    }
  }
}
