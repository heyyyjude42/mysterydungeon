package edu.brown.cs.dnd.Data;

import org.junit.Test;
import static org.junit.Assert.*;

public class DiceTest {

  @Test
  public void testRandomDie() {
    Dice d = Dice.randomDie();
    Dice d2 = Dice.randomDie();

    assertTrue(d.equals(Dice.D10)
            || d.equals(Dice.D4)
            || d.equals(Dice.D6)
            || d.equals(Dice.D8)
            || d.equals(Dice.D12)
            || d.equals(Dice.D20));

    assertTrue(d2.equals(Dice.D10)
            || d2.equals(Dice.D4)
            || d2.equals(Dice.D6)
            || d2.equals(Dice.D8)
            || d2.equals(Dice.D12)
            || d2.equals(Dice.D20));
  }
}
