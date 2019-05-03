package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class MonsterTest {

  @Test
  public void testConstruct() {
    Monster m = new Monster();
    Monster m2 = new Monster("a", "b", "c", "d",
            5, 5, "e", "f", 5,
            5, 5, 5, 5, 5, 5.0,
            new HashMap<>(), new HashMap<>(), new HashMap<>());

    assertNotNull(m);
    assertNotNull(m2);
  }

  @Test
  public void testGetters() {
    Monster m = new Monster("a", "b", "c", "d",
            5, 5, "e", "f", 5,
            5, 5, 5, 5, 5, 5.0,
            new HashMap<>(), new HashMap<>(), new HashMap<>());

    assertEquals(m.getAc(), 5);
    assertEquals(m.getActions(), new HashMap<>());
    assertEquals(m.getAlignment(), "d");
    assertEquals(m.getCha(), 5);
    assertEquals(m.getCon(), 5);
    assertEquals(m.getCr(), 5.0, 0.001);
    assertEquals(m.getDex(), 5);
    assertEquals(m.getHp(), 5);
  }
}
