package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Data.QueryResult;
import edu.brown.cs.dnd.Dungeon.Graph.UndirectedGraph;
import edu.brown.cs.dnd.Dungeon.Graph.UndirectedEdge;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;
import edu.brown.cs.dnd.Dungeon.Rooms.Path;
import edu.brown.cs.dnd.Dungeon.Rooms.Room;
import edu.brown.cs.dnd.Dungeon.Rooms.RoomSize;
import edu.brown.cs.dnd.RandomTools.Randomizer;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * Class representing the dungeon and its utilities.
 */
public class Dungeon implements IDungeon, QueryResult {

  private List<AbsRoom> rooms;
  private AbsRoom[][] occupiedCells;
  private int width;
  private int height;
  private Random rand;

  private static final int TOLERANCE = 200;
  private static final double MAIN_ROOM_FACTOR = 1.25;
  private static final int RAND_ROOM_LEVEL = 50;
  private static final double DUNGEON_GEN_DENSITY = 0.8;
  private static final int ONE_HUNDRED = 100;

  /**
   * A Default Constructor for a Dungeon.
   */
  public Dungeon() { }

  /**
   * A Constructor for a Dungeon.
   * @param width   An int that is the width of the dungeon
   * @param height    An int that is the height of the dungeon
   * @param roomSize  The average roomsize of rooms in this dungeon.
   */
  public Dungeon(int width, int height, RoomSize roomSize) {
    this.width = width;
    this.height = height;
    this.occupiedCells = new AbsRoom[height][width];
    this.rand = new Random();
    this.rooms = new ArrayList<>();
    generateRooms(DUNGEON_GEN_DENSITY, roomSize);
    filterRooms();
    connectRooms();
    fillAllRooms();
  }

  /**
   * Method generates the rooms of the dungeon.
   * @param dungeonDensity    A double that is the density of the dungeon
   * @param averageRoomSize   A RoomSize enum that is the average room size of
   *                          the dungeon
   */
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

  /**
   * Method filters out the smaller rooms of the dungeon, while keeping
   * a fraction of the discarded rooms.
   */
  private void filterRooms() {
    double totalRoomArea = 0;
    List<AbsRoom> untouched = new ArrayList<>();
    for (AbsRoom r : rooms) {
      totalRoomArea += r.getArea();
    }
    double averageRoomSize = totalRoomArea / (double) rooms.size();
    for (AbsRoom r : rooms) {
      if (r.getArea() < averageRoomSize * MAIN_ROOM_FACTOR) {
        if (rand.nextInt(ONE_HUNDRED) >= RAND_ROOM_LEVEL) {
          untouched.add(r);
        }
      }
    }
    this.rooms = untouched;
  }

  /**
   * Method connects the rooms of the dungeon by generating paths between
   * rooms in the mst.
   */
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

  /**
   * Method fills the cells of the 2d grid that the dungeon is overlayed over.
   */
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
    int horizAligned = numHorizAligned(higher, lower);

    // For checking vertical connection
    int a = rightMost.getTopLeft().getX();
    int d = leftMost.getBottomRight().getX();
    int vertAligned = numVertAligned(leftMost, rightMost);

    // Set path size to be random for horizontal and vertical connections
    int horizPathSize = Randomizer.generate(1, horizAligned);
    int vertPathSize = Randomizer.generate(1, vertAligned);

    if (c - b >= horizPathSize - 1) {
      // If not horizontally adjacent, then connect
      if (!adjacentHoriz(leftMost, rightMost)) {
        Location pathTopLeft = new Location(leftMost.getBottomRight().getX()
                + 1, Randomizer.generate(c - horizAligned + horizPathSize, c));

        Path p = new Path(rightMost.getTopLeft().getX()
                - leftMost.getBottomRight().getX() - 1, horizPathSize,
                pathTopLeft);
        result.add(p);
      }
    } else if (d - a >= vertPathSize - 1) {
      // If not vertically adjacent, then connect
      if (!adjacentVert(higher, lower)) {
        Location pathTopLeft = new Location(Randomizer.generate(
                d - vertAligned + 1, d - vertPathSize + 1),
                higher.getBottomRight().getY() - 1);

        Path p = new Path(vertPathSize, higher.getBottomRight().getY()
                - lower.getTopLeft().getY() - 1, pathTopLeft);
        result.add(p);
      }
    } else {
      // Flip a coin to decide which extended path method to use
      final int half = 50;
      int decider = Randomizer.generate(1, ONE_HUNDRED);

      if (decider <= half) {
        result.addAll(extendedVertPath(leftMost, rightMost));
      } else {
        result.addAll(extendedHorizPath(leftMost, rightMost));
      }
    }

    return result;
  }

  /**
   * Method generates a two-segment path that connects two rooms, with the
   * first segment extending horizontally.
   * @param leftMost    An AbsRoom that is the leftmost room
   * @param rightMost   An AbsRoom that is the rightmost room
   * @return    A Set of Paths that is the two-segment connection between
   * the two rooms
   */
  public Set<Path> extendedHorizPath(AbsRoom leftMost,
                                     AbsRoom rightMost) {
    Set<Path> result = new HashSet<>();
    Path p1;
    Path p2;

    int pathSize = Randomizer.generate(1,
            Math.min(leftMost.getHeight(), rightMost.getWidth()));

    // If rightmost room is above the leftmost room
    if (rightMost.getTopLeft().getY() > leftMost.getTopLeft().getY()) {
      Location p1TopLeft;
      // If adjacent, make initial segment approach lower so second segment
      // can extend upwards
      if (adjacentVert(leftMost, rightMost)
              && pathSize != leftMost.getHeight()) {
        p1TopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(
                        leftMost.getBottomRight().getY() + pathSize - 1,
                        leftMost.getTopRight().getY() - 1));
      } else {
        p1TopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(
                        leftMost.getBottomRight().getY() + pathSize - 1,
                        leftMost.getTopRight().getY()));
      }

      p1 = new Path(rightMost.getTopLeft().getX()
              - leftMost.getBottomRight().getX() - 1
              + Randomizer.generate(0, rightMost.getWidth() - pathSize),
              pathSize, p1TopLeft);

      p2 = new Path(pathSize, rightMost.getBottomRight().getY()
              - p1.getBottomRight().getY(),
              new Location(p1.getBottomRight().getX() + 1,
                      rightMost.getBottomRight().getY() - 1));
    } else {
      // If rightmost room is below the leftmost room
      Location p1TopLeft;

      if (adjacentVert(leftMost, rightMost)
              && pathSize != leftMost.getHeight()) {
        p1TopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getBottomRight().getY()
                        + pathSize, leftMost.getTopRight().getY()));
      } else {
        p1TopLeft = new Location(leftMost.getBottomRight().getX() + 1,
                Randomizer.generate(leftMost.getBottomRight().getY()
                        + pathSize - 1, leftMost.getTopRight().getY()));
      }

      p1 = new Path(rightMost.getTopLeft().getX()
              - leftMost.getBottomRight().getX() - 1
              + Randomizer.generate(0, rightMost.getWidth() - pathSize),
              pathSize, p1TopLeft);

      p2 = new Path(pathSize, p1.getTopRight().getY()
              - rightMost.getTopLeft().getY(),
              new Location(p1.getTopRight().getX() + 1,
                      p1.getTopRight().getY()));
    }

    result.add(p1);
    result.add(p2);

    return result;
  }

  /**
   * Method generates a two-segment path that connects two rooms, with the
   * first segment extending vertically.
   * @param leftMost    An AbsRoom that is the leftmost room
   * @param rightMost   An AbsRoom that is the rightmost room
   * @return    A Set of Paths that is the two-segment connection between
   * the two rooms
   */
  public Set<Path> extendedVertPath(AbsRoom leftMost,
                                    AbsRoom rightMost) {
    Set<Path> result = new HashSet<>();
    Path p1;
    Path p2;

    int pathSize = Randomizer.generate(1,
            Math.min(leftMost.getWidth(), rightMost.getHeight()));

    // If rightmost room is above the leftmost room
    if (rightMost.getTopLeft().getY() > leftMost.getTopLeft().getY()) {
      Location p1TopLeft;
      if (adjacentVert(leftMost, rightMost)
              && pathSize != leftMost.getWidth()) {
        p1TopLeft = new Location(Randomizer.generate(
                leftMost.getTopLeft().getX(),
                leftMost.getTopRight().getX() - pathSize),
                Randomizer.generate(
                        rightMost.getBottomLeft().getY() + pathSize - 1,
                        rightMost.getTopLeft().getY()));
      } else {
        p1TopLeft = new Location(Randomizer.generate(
                leftMost.getTopLeft().getX(),
                leftMost.getTopRight().getX() - pathSize + 1),
                Randomizer.generate(
                        rightMost.getBottomLeft().getY() + pathSize - 1,
                        rightMost.getTopLeft().getY()));
      }

      p1 = new Path(pathSize, p1TopLeft.getY()
              - leftMost.getTopRight().getY(), p1TopLeft);

      p2 = new Path(rightMost.getTopLeft().getX()
              - p1.getTopRight().getX() - 1, pathSize,
              new Location(p1.getTopRight().getX() + 1,
                      p1.getTopRight().getY()));
    } else {
      // If rightmost room is below the leftmost room
      Location p1TopLeft;
      if (adjacentVert(leftMost, rightMost)
              && pathSize != leftMost.getWidth()) {
        p1TopLeft = new Location(Randomizer.generate(
                leftMost.getTopLeft().getX(),
                leftMost.getTopRight().getX() - pathSize),
                leftMost.getBottomRight().getY() - 1);
      } else {
        p1TopLeft = new Location(Randomizer.generate(
                leftMost.getTopLeft().getX(),
                leftMost.getTopRight().getX() - pathSize + 1),
                leftMost.getBottomRight().getY() - 1);
      }

      p1 = new Path(pathSize, leftMost.getBottomRight().getY()
              - rightMost.getTopLeft().getY() - 1
              + Randomizer.generate(pathSize, rightMost.getHeight()),
              p1TopLeft);

      p2 = new Path(rightMost.getTopLeft().getX()
              - p1.getBottomRight().getX() - 1, pathSize,
              new Location(p1.getBottomRight().getX() + 1,
                      p1.getBottomRight().getY() + pathSize - 1));
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
  private void fillCells(AbsRoom r) {
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
  private boolean isValidRoom(Room r) {
    int x = r.getBottomLeft().getX();
    int y = r.getBottomLeft().getY();

    if (x < 0 || y < 0) {
      return false;
    }

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

  @Override
  public String prettify() {
    StringBuilder result = new StringBuilder();
    for (int i = this.occupiedCells.length - 1; i >= 0; i--) {
      for (int j = 0; j < this.occupiedCells[i].length; j++) {
        if (this.occupiedCells[i][j] != null) {
          result.append(this.occupiedCells[i][j].getSymbol());
        } else {
          result.append("  ");
        }
      }
      result.append("\n");
    }
    return result.toString();
  }

  @Override
  public String simplify() {
    StringBuilder result = new StringBuilder();
    result.append("Width: ");
    result.append(this.width);
    result.append(" Height: ");
    result.append(this.height);

    return result.toString();
  }
}
