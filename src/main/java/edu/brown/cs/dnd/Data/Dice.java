package edu.brown.cs.dnd.Data;

import java.util.Random;

public enum Dice {
  D4(4),
  D6(6),
  D8(8),
  D10(10),
  D12(12),
  D20(20);

  private int value;


  Dice(int value) {
    this.value = value;
  }

  public int roll() {
    Random rand = new Random();
    return rand.nextInt(this.value) + 1;
  }

  public static Dice randomDie() {
    Random rand = new Random();
    return values()[rand.nextInt(values().length)];
  }

  @Override
  public String toString() {
    return "d" + this.value;
  }
}
