package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

public class FeatTest {

  @Test
  public void testConstruct() {
    Feat f = new Feat();
    Feat f2 = new Feat("a", "b");

    assertNotNull(f);
    assertNotNull(f2);
  }

  @Test
  public void testGetters() {
    Feat f2 = new Feat("a", "b");

    assertEquals(f2.getName(), "a");
    assertEquals(f2.getDesc(), "b");
  }
}
