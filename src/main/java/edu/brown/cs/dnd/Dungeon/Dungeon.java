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
  private AbsRoom[][] occupiedCells;
  private int width;
  private int height;
  private Random rand;

  private static final int TOLERANCE = 100;
  private static final double MAIN_ROOM_FACTOR = 1.25;
  private static final int RAND_ROOM_LEVEL = 50;

  public Dungeon() { }

  public Dungeon(int width, int height) {
    this.width = width;
    this.height = height;
    this.occupiedCells = new AbsRoom[height][width];
    this.rand = new Random();
    this.rooms = new ArrayList<>();
    generateRooms(0.8, RoomSize.SMALL);
    filterRooms();
    connectRooms();
    fillAllRooms();
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
        areaUsed += r.getArea() / ((double) this.height * (double) this.width);
      } else {
        numFailed++;
      }
    }
  }

//  private void filterRooms() {
//    double totalRoomArea = 0;
//    for (AbsRoom r : rooms) {
//      totalRoomArea += r.getArea();
//    }
//    double averageRoomSize = totalRoomArea / (double) rooms.size();
//    for (AbsRoom r : rooms) {
//      if (r.getArea() < averageRoomSize * MAIN_ROOM_FACTOR) {
//        if (rand.nextInt(100) < RAND_ROOM_LEVEL) {
//          rooms.remove(r);
//        }
//      }
//    }
//  }

  private void filterRooms() {
    double totalRoomArea = 0;
    List<AbsRoom> untouched = new ArrayList<>();
    for (AbsRoom r : rooms) {
      totalRoomArea += r.getArea();
    }
    double averageRoomSize = totalRoomArea / (double) rooms.size();
    for (AbsRoom r : rooms) {
      if (r.getArea() < averageRoomSize * MAIN_ROOM_FACTOR) {
        if (rand.nextInt(100) >= RAND_ROOM_LEVEL) {
          untouched.add(r);
        }
      }
    }
    this.rooms = untouched;
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
      Collection<Path> toAdd = getPathFromEdge(e);
      rooms.addAll(toAdd);
    }

  }

  private void fillAllRooms() {
    for (AbsRoom r : this.rooms) {
      fillCells(r);
    }
  }


  /**
   * Method creates a path between two rooms based on the edge between them.
   * @param edge    The edge between the two rooms in the mst
   * @return    A Collection of AbsRooms that is either of sizes 1 or 2, which
   * is the path between the two rooms
   */
  public Collection<Path> getPathFromEdge(UndirectedEdge<AbsRoom> edge) {
    Set<Path> result = new HashSet<>();
    AbsRoom r1 = edge.getV1();
    AbsRoom r2 = edge.getV2();

    int pathSize = 1;

    AbsRoom higher = r1.getTopLeft().getY() >= r2.getTopLeft().getY()
            ? r1 : r2;
    AbsRoom lower = r1.getTopLeft().getY() < r2.getTopLeft().getY()
            ? r1 : r2;
    AbsRoom leftMost = r1.getBottomRight().getX() <= r2.getBottomRight().getX()
            ? r1 : r2;
    AbsRoom rightMost = r1.getBottomRight().getX() > r2.getBottomRight().getX()
            ? r1 : r2;

    // For checking horizontal connection
    int b = higher.getBottomRight().getY();
    int c = lower.getTopLeft().getY();

    // For checking vertical connection
    int a = rightMost.getTopLeft().getX();
    int d = leftMost.getBottomRight().getX();

    if (c - b >= pathSize - 1) {
      // If not horizontally adjacent, then connect
      if (!adjacentHoriz(leftMost, rightMost)) {
        int horizAligned = numHorizAligned(higher, lower);
        Location pathTopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(c - horizAligned + 1, c));

        Path p = new Path(rightMost.getTopLeft().getX()
                - leftMost.getBottomRight().getX() - 1, pathSize, pathTopLeft);
        result.add(p);
      }
    } else if (d - a >= pathSize - 1) {
      // If not vertically adjacent, then connect
      if (!adjacentVert(higher, lower)) {
        int vertAligned = numVertAligned(leftMost, rightMost);
        Location pathTopLeft = new Location(Randomizer.generate(d - vertAligned + 1, d),
                higher.getBottomRight().getY() - 1);

        Path p = new Path(pathSize, higher.getBottomRight().getY()
                - lower.getTopLeft().getY() - 1, pathTopLeft);
        result.add(p);
      }
    } else {
      result.addAll(extendedHorizPath(leftMost, rightMost, pathSize));
    }

    return result;
  }

  /**
   * Method generates a path of size two that connects two rooms.
   * @param leftMost    An AbsRoom that is the leftmost room
   * @param rightMost   An AbsRoom that is the rightmost room
   * @return    A Set of Paths that is the connection between the two rooms
   */
  public Set<Path> extendedHorizPath(AbsRoom leftMost,
                                     AbsRoom rightMost,
                                     int pathSize) {
    Set<Path> result = new HashSet<>();
    Path p1;
    Path p2;

    // If rightmost room is above the leftmost room
    if (leftMost.getTopLeft().getY() < rightMost.getTopLeft().getY()) {
      Location pathTopLeft;
      if (adjacentVert(leftMost, rightMost)) {
        pathTopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getTopRight().getY()
                        - leftMost.getHeight() + 1,
                        leftMost.getTopRight().getY() - 1));
      } else {
        pathTopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getTopRight().getY()
                                - leftMost.getHeight() + 1,
                        leftMost.getTopRight().getY()));
      }

      p1 = new Path(rightMost.getTopLeft().getX()
              - leftMost.getBottomRight().getX() - 1
              + Randomizer.generate(1, rightMost.getWidth() - 1 - pathSize),
              pathSize,
              pathTopLeft);

      p2 = new Path(pathSize, rightMost.getBottomRight().getY()
              - p1.getBottomRight().getY(),
              new Location(p1.getBottomRight().getX() + 1,
                      p1.getBottomRight().getY()
                              + rightMost.getBottomRight().getY()
                              - p1.getBottomRight().getY() - 1));
    } else {
      // rightmost room is below leftmost room
      Location pathTopLeft;
      if (adjacentVert(leftMost, rightMost)) {
        pathTopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getTopRight().getY()
                        - leftMost.getHeight() + 2,
                        leftMost.getTopRight().getY()));
      } else {
        pathTopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getTopRight().getY()
                                - leftMost.getHeight() + 1,
                        leftMost.getTopRight().getY()));
      }

      p1 = new Path(rightMost.getTopLeft().getX()
              - leftMost.getBottomRight().getX() - 1
              + Randomizer.generate(1, rightMost.getWidth() - 1 - pathSize),
              pathSize,
              pathTopLeft);

      p2 = new Path(pathSize, leftMost.getTopLeft().getY()
              - rightMost.getTopLeft().getY(),
              new Location(p1.getTopRight().getX() + 1,
                      p1.getTopRight().getY()));
    }

    result.add(p1);
    result.add(p2);

    return result;
  }

  /**
   * Method checks if two rooms are adjacent or not horizontally.
   * @param r1    An AbsRoom that is the first room
   * @param r2    An AbsRoom that is the second room
   * @return    A boolean that is true if the two rooms are adjacent
   */
  public boolean adjacentHoriz(AbsRoom r1, AbsRoom r2) {
    return Math.abs(r1.getBottomRight().getX() - r2.getTopLeft().getX()) <= 1
            || Math.abs(r1.getTopLeft().getX() - r2.getBottomRight().getX())
            <= 1;
  }

  /**
   * Method checks if two rooms are adjacent or not vertically.
   * @param r1    An AbsRoom that is the first room
   * @param r2    An AbsRoom that is the second room
   * @return    A boolean that is true if the two rooms are adjacent
   */
  public boolean adjacentVert(AbsRoom r1, AbsRoom r2) {
    return Math.abs(r1.getBottomRight().getY() - r2.getTopLeft().getY()) <= 1
            || Math.abs(r1.getTopLeft().getY() - r2.getBottomRight().getY())
            <= 1;
  }

  /**
   * Method finds the number of points that are horizontally aligned between
   * the two rooms.
   * @param higher    An AbsRoom that is the higher room
   * @param lower   An AbsRoom that is the lower room
   * @return    An int that is the number of colliding dots
   */
  public int numHorizAligned(AbsRoom higher, AbsRoom lower) {
    int result = 0;
    int c = lower.getTopLeft().getY();
    int b = higher.getBottomRight().getY();

    while (c >= b && c >= lower.getBottomRight().getY()) {
      result++;
      c--;
    }

    return result;
  }

  /**
   * Method finds the number of points that are vertically aligned between
   * the two rooms.
   * @param leftMost    An AbsRoom that is the leftmost room
   * @param rightMost   An AbsRoom that is the rightmost room
   * @return    An int that is the number of colliding dots
   */
  public int numVertAligned(AbsRoom leftMost, AbsRoom rightMost) {
    int result = 0;
    int a = rightMost.getTopLeft().getX();
    int d = leftMost.getBottomRight().getX();

    while (d >= a && d >= leftMost.getBottomLeft().getX()) {
      result++;
      d--;
    }

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
    int x = r.getBottomLeft().getX();
    int y = r.getBottomLeft().getY();

    for (int i = y; i < y + r.getHeight(); i++) {
      for (int j = x; j < x + r.getWidth(); j++) {
        this.occupiedCells[i][j] = r;
      }
    }
  }

  /**
   * Method checks if the room can be inserted into the dungeon.
   * @param r   A Room that is the room to check for insertion
   * @return    A boolean that is true if the room can be inserted.
   */
  public boolean isValidRoom(Room r) {
    int x = r.getBottomLeft().getX();
    int y = r.getBottomLeft().getY();

    if (x < 0 || y < 0) {return false;}



    for (int i = y; i < y + r.getHeight(); i++) {
      for (int j = x; j < x + r.getWidth(); j++) {
        if (i >= this.height || j >= this.width) {
          return false;
        }

        if (this.occupiedCells[i][j] != null) {
          return false;
        }
      }
    }

    return true;
  }

//  public void printDungeon() {
//    for (int i = 0; i < this.occupiedCells.length; i++) {
//      for (int j = 0; j < this.occupiedCells[i].length; j++) {
//        if (this.occupiedCells[i][j] != null) {
//          System.out.print(this.occupiedCells[i][j].getSymbol());
//        } else {
//          System.out.print("  ");
//        }
//      }
//      System.out.println("");
//    }
//  }


  public void printDungeon() {
    for (int i = this.occupiedCells.length - 1; i >= 0; i--) {
      for (int j = 0; j < this.occupiedCells[i].length; j++) {
        if (this.occupiedCells[i][j] != null) {
          System.out.print(this.occupiedCells[i][j].getSymbol());
        } else {
          System.out.print("  ");
        }
      }
      System.out.println("");
    }
  }
}
