package edu.brown.cs.dnd.Dungeon;

import org.junit.Test;
import static org.junit.Assert.*;

public class RandomizerTest {

  @Test
  public void testGenerate() {
    int random = Randomizer.generate(3, 3);
    assertEquals(random, 3);

    random = Randomizer.generate(7, 9);
    assertTrue(random >= 7 && random <= 9);

    random = Randomizer.generate(0, 10);
    assertTrue(random >= 0 && random <= 10);
  }
}
