package edu.brown.cs.dnd.Dungeon;

import edu.brown.cs.dnd.Dungeon.Graph.UndirectedEdge;
import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Rooms.Room;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;
import edu.brown.cs.dnd.Dungeon.Rooms.Path;

import java.util.Collection;

import edu.brown.cs.dnd.Dungeon.Rooms.RoomSize;
import org.junit.Test;
import static org.junit.Assert.*;

public class DungeonTest {

  @Test
  public void testPathMaker() {
    Dungeon d = new Dungeon();

    Room r1 = new Room(3, 3, new Location(10, 10));
    Room r2 = new Room(3, 3, new Location(14, 9));

    UndirectedEdge<AbsRoom> ue = new UndirectedEdge<>(r1, r2, 5);

    Collection<Path> paths = d.getPathFromEdge(ue);

    assertNotNull(paths);

    Room r3 = new Room(3, 3, new Location(12, 3));
    UndirectedEdge<AbsRoom> ue2 = new UndirectedEdge<>(r1, r3, 5);
    Collection<Path> paths2 = d.getPathFromEdge(ue2);
    assertNotNull(paths2);

    Room r4 = new Room(3, 3, new Location(14, 10));
    UndirectedEdge<AbsRoom> ue3 = new UndirectedEdge<>(r3, r4, 5);
    Collection<Path> paths3 = d.getPathFromEdge(ue3);
    assertNotNull(paths3);

    Room r5 = new Room(3, 5, new Location(14, 11));
    UndirectedEdge<AbsRoom> ue4 = new UndirectedEdge<>(r1, r5, 5);
    Collection<Path> paths4 = d.getPathFromEdge(ue4);
    assertNotNull(paths4);

    Room r6 = new Room(7, 3, new Location(9, 6));
    UndirectedEdge<AbsRoom> ue5 = new UndirectedEdge<>(r1, r6, 5);
    Collection<Path> paths5 = d.getPathFromEdge(ue5);
    assertNotNull(paths5);
  }

  @Test
  public void testGetArea() {
    Dungeon d = new Dungeon(10, 15, RoomSize.MEDIUM);
    Dungeon d2 = new Dungeon(4, 8, RoomSize.SMALL);

    assertEquals(d.getArea(), 150);
    assertEquals(d2.getArea(), 32);
  }

}