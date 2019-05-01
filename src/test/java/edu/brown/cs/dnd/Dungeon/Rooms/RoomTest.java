package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;
import edu.brown.cs.dnd.Dungeon.Dungeon;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {

  @Test
  public void testCorners() {
    Room r = new Room(3, 3, new Location(10, 10));
    Location bottomRight = new Location(12, 8);
    Location topRight = new Location(12, 10);
    Location bottomLeft = new Location(10, 8);

    assertEquals(r.getBottomRight(), bottomRight);
    assertEquals(r.getTopRight(), topRight);
    assertEquals(r.getBottomLeft(), bottomLeft);
  }

  @Test
  public void testPlusMinus() {
    Room r = new Room(3, 3, new Location(10, 10));
    Location topLeft = new Location(10, 10);
    Location threeDown = new Location(10, 7);
    Location threeRight = new Location(13, 10);

    assertEquals(topLeft, r.getTopLeft());

    assertEquals(topLeft.minusY(3), threeDown);
    assertEquals(topLeft.addX(3), threeRight);
  }

  @Test
  public void testAlignment() {
    Room r1 = new Room(3, 3, new Location(10, 10));
    Room r2 = new Room(3, 3, new Location(14, 9));
    Room r3 = new Room(3, 5, new Location(14, 10));
    Room r4 = new Room(3, 5, new Location(14, 11));
    Room r5 = new Room(7, 3, new Location(9, 6));

    Dungeon d = new Dungeon();

    assertEquals(d.numHorizAligned(r1, r2), 2);
    assertEquals(d.numHorizAligned(r1, r3), 3);
    assertEquals(d.numHorizAligned(r4, r1), 3);
    assertEquals(d.numVertAligned(r1, r5), 3);
  }

}