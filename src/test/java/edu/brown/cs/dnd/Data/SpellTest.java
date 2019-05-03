package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

public class SpellTest {

  @Test
  public void testConstruct() {
    Spell s = new Spell("a", "b", 1, true,
            "c", "d", true, false,
            true, "e",
            "f", "g", "h");
    assertNotNull(s);
  }

  @Test
  public void testGetters() {
    Spell s = new Spell("a", "b", 1, true,
            "c", "d", true, false,
            true, "e",
            "f", "g", "h");

    assertEquals(s.getCastingTime(), "d");
    assertEquals(s.getClasses(), "h");
    assertEquals(s.getDescription(), "g");
    assertEquals(s.getDuration(), "f");
    assertEquals(s.getLevel(), 1);
    assertEquals(s.getCastingTime(), "d");
    assertEquals(s.getMaterials(), "e");
    assertEquals(s.getName(), "a");
    assertEquals(s.getRange(), "c");
  }
}
