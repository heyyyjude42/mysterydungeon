package edu.brown.cs.dnd.Dungeon;

/**
 * Class representing a utility random number generator within a range.
 */
public class Randomizer {
  public static int generate(int min,int max)
  {
    return min + (int)(Math.random() * ((max - min) + 1));
  }
}
