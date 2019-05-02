package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.Dungeon.Rooms.AbsRoom;
import edu.brown.cs.dnd.Dungeon.Dungeon;

import org.junit.Test;
import static org.junit.Assert.*;

public class RoomTest {

  @Test
  public void testGetMidpoint() {
    AbsRoom r = new Room(3, 3, new Location(10, 10));
    AbsRoom r2 = new Room(1, 1, new Location(1, 1));
    AbsRoom r3 = new Room(2, 2, new Location(3, 3));

    assertEquals(r.getMidpoint(), new Location(12, 12));
    assertEquals(r2.getMidpoint(), new Location(2, 2));
    assertEquals(r3.getMidpoint(), new Location(4, 4));
  }

  @Test
  public void testGetArea() {
    AbsRoom r = new Room(3, 3, new Location(10, 10));
    AbsRoom r2 = new Room(100, 100, new Location(3, 3));
    AbsRoom r3 = new Room(5, 7, new Location(8, 8));

    assertEquals(r.getArea(), 9);
    assertEquals(r2.getArea(), 10000);
    assertEquals(r3.getArea(), 35);
  }

  @Test
  public void testCorners() {
    Room r = new Room(3, 3, new Location(10, 10));
    Location bottomRight = new Location(12, 8);
    Location topRight = new Location(12, 10);
    Location bottomLeft = new Location(10, 8);

    Room r2 = new Room(3, 3, new Location(0, 0));
    Location bottomRight2 = new Location(2, -2);
    Location topRight2 = new Location(2, 0);
    Location bottomLeft2 = new Location(0, -2);

    assertEquals(r.getBottomRight(), bottomRight);
    assertEquals(r.getTopRight(), topRight);
    assertEquals(r.getBottomLeft(), bottomLeft);

    assertEquals(r2.getBottomRight(), bottomRight2);
    assertEquals(r2.getTopRight(), topRight2);
    assertEquals(r2.getBottomLeft(), bottomLeft2);
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
    AbsRoom r1 = new Room(3, 3, new Location(10, 10));
    AbsRoom r2 = new Room(3, 3, new Location(14, 9));
    AbsRoom r3 = new Room(3, 5, new Location(14, 10));
    AbsRoom r4 = new Room(3, 5, new Location(14, 11));
    AbsRoom r5 = new Room(7, 3, new Location(9, 6));

    Dungeon d = new Dungeon();

    assertEquals(d.numHorizAligned(r1, r2), 2);
    assertEquals(d.numHorizAligned(r1, r3), 3);
    assertEquals(d.numHorizAligned(r4, r1), 3);
    assertEquals(d.numVertAligned(r1, r5), 3);
  }

}