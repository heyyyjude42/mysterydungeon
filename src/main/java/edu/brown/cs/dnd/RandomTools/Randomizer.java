package edu.brown.cs.dnd.RandomTools;

/**
 * Class representing a utility random number generator within a range.
 */
public final class Randomizer {

  /**
   * A private constructor for a Randomizer.
   */
  private Randomizer() {

  }

  /**
   * Method generates a random number between a specified min and max,
   * inclusive.
   * @param min   An int that is the min of the range, inclusive
   * @param max   An int that is the max of the range, inclusive
   * @return    An int that is the generated random number
   */
  public static int generate(int min, int max) {
    return min + (int) (Math.random() * ((max - min) + 1));
  }
}
