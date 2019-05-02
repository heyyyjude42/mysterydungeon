package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Dice;
import edu.brown.cs.dnd.Data.Location;

import org.junit.Test;
import static org.junit.Assert.*;

public class TrapTest {

  @Test
  public void testConstruct() {
    Trap t = new Trap(new Location(3, 3),
            15, 15, 15, 15, Dice.D10);

    assertNotNull(t);
  }

  @Test
  public void testGetters() {
    Trap t = new Trap(new Location(3, 3),
            1, 2, 3, 4, Dice.D10);

    assertEquals(t.getDamage(), "4d10");
    assertEquals(t.getDetectionDC(), 1);
    assertEquals(t.getDisableDC(), 2);
    assertEquals(t.getSaveDC(), 3);
  }
}
