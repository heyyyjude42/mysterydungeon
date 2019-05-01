package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocationTest {

  @Test
  public void testConstruct() {
    Location l = new Location(10, 10);
    assertNotNull(l);
  }

  @Test
  public void testGetters() {
    Location l = new Location(5, 10);
    assertEquals(l.getX(), 5);
    assertEquals(l.getY(), 10);

    Location l2 = new Location(1, 1);
    assertEquals(l2.getX(), 1);
    assertEquals(l2.getY(), 1);
  }

  @Test
  public void testRandom() {
    Location l = Location.randLocation(10, 10);
    Location l2 = Location.randLocation(5, 7);

    assertTrue(l.getX() < 10 && l.getY() < 10);
    assertTrue(l2.getX() < 5 && l2.getY() < 7);
  }

  @Test
  public void testDistanceTo() {
    Location l = new Location(10, 10);
    Location l2 = new Location(10, 10);
    Location l3 = new Location(11, 11);
    Location l4 = new Location(0, 0);
    Location l5 = new Location(3, 4);

    assertEquals(l.distanceTo(l2), 0, 0.001);
    assertEquals(l.distanceTo(l3), Math.sqrt(2.0), 0.001);
    assertEquals(l4.distanceTo(l5), 5.0, 0.001);
  }

  @Test
  public void testPlusMinus() {
    Location l = new Location(10, 10);
    Location l2 = new Location(5, 7);

    assertEquals(l.plus(l2), new Location(15, 17));
    assertEquals(l.minus(l2), new Location(5, 3));
    assertEquals(l.addX(5), new Location(15, 10));
    assertEquals(l.minusX(5), new Location(5, 10));
    assertEquals(l.addY(5), new Location(10, 15));
    assertEquals(l.minusY(5), new Location(10, 5));
  }
}
