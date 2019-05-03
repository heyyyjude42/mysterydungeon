package edu.brown.cs.dnd.Data;

import java.util.Random;

/**
 * Enum representing D&D Dice.
 */
public enum Dice {
  D4(4),
  D6(6),
  D8(8),
  D10(10),
  D12(12),
  D20(20);

  private int value;

  /**
   * A Constructor for a Dice.
   * @param value   An int that is the value of the dice
   */
  Dice(int value) {
    this.value = value;
  }

  /**
   * Method rolls the dice.
   * @return    An int that is the result of the roll
   */
  public int roll() {
    Random rand = new Random();
    return rand.nextInt(this.value) + 1;
  }

  /**
   * Method returns a random dice.
   * @return    A Dice that is the randomly generated dice.
   */
  public static Dice randomDie() {
    Random rand = new Random();
    return values()[rand.nextInt(values().length)];
  }

  @Override
  public String toString() {
    return "d" + this.value;
  }
}
