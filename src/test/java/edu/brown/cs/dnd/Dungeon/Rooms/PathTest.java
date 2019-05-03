package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Location;

import org.junit.Test;
import static org.junit.Assert.*;

public class PathTest {

  @Test
  public void testConstruct() {
    Path p = new Path(5, 5, new Location(10, 10));
    assertNotNull(p);
  }

  @Test
  public void testGetSymbol() {
    Path p = new Path(5, 5, new Location(10, 10));
    assertEquals(p.getSymbol(), "o ");
  }
}
