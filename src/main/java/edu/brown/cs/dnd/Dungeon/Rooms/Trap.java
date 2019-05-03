package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.Data.Dice;
import edu.brown.cs.dnd.Data.Location;
import edu.brown.cs.dnd.RandomTools.NormalDistribution;

import java.util.Objects;

/**
 * A class to represent a generic DnD trap.
 */
public class Trap extends RoomElement {
  // The difficulty class of detecting this trap.
  private int detectionDC;
  // The difficulty class of disabling this trap.
  private int disableDC;
  // The difficulty class of the saving throw for this trap.
  private int saveDC;
  // A String representation of the damage that this trap deals.
  private String damage;
  // The location of this Trap with respect to the room it is in.
  private Location position;

  // Some parameters for random trap generation.
  private static final int TRAP_DC_MEAN = 10;
  private static final int TRAP_DC_STD_DEV= 2;
  private static final int TRAP_DAM_MEAN = 1;
  private static final int TRAP_DAM_STD_DEV = 2;

  /**
   * The basic constructor for Traps.
   * @param position - a Location, the position of this Trap in its room.
   * @param detectionDC - an int, the detection dc.
   * @param disableDC - an int, the disable dc.
   * @param saveDC - an int, the save dc.
   * @param numDice - an int, the number of dice to roll for damage.
   * @param damageDie - a Dice, the type of dice to roll for damage.
   */
  Trap(Location position, int detectionDC,
              int disableDC, int saveDC, int numDice, Dice damageDie) {
    this.position = position;
    this.detectionDC = detectionDC;
    this.disableDC = disableDC;
    this.saveDC = saveDC;
    this.damage = numDice + damageDie.toString();
  }

  /**
   * Gets the detectionDC.
   * @return - the detectionDC.
   */
  int getDetectionDC() {
    return detectionDC;
  }

  /**
   * Gets the disableDC.
   * @return - the disableDC.
   */
  int getDisableDC() {
    return disableDC;
  }

  /**
   * Gets the saveDC.
   * @return - the saveDC.
   */
  int getSaveDC() {
    return saveDC;
  }

  /**
   * Gets the damage String.
   * @return - the damage.
   */
  String getDamage() {
    return damage;
  }

  /**
   * Generate a random trap with the provided challenge rating.
   * @param level - The level of the trap.
   * @param position - the position of this trap relative to the room.
   * @return - A random trap with the provided challenge rating.
   */
  static Trap randomTrap(int level, Location position) {
    NormalDistribution nDC = new NormalDistribution(TRAP_DC_MEAN, TRAP_DC_STD_DEV);
    NormalDistribution nDam = new NormalDistribution(TRAP_DAM_MEAN,  TRAP_DAM_STD_DEV);
    int detectionDC = (int) Math.ceil(Math.abs(nDC.draw()));
    int disableDC = (int) Math.ceil(Math.abs(nDC.draw()));
    int saveDC = (int) Math.ceil(Math.abs(nDC.draw()));

    int dam = (int) Math.ceil(Math.abs(nDam.draw())) + TRAP_DAM_MEAN;

    return new Trap(position,
            detectionDC, disableDC,
            saveDC, dam, Dice.randomDie());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Trap)) {
      return false;
    }
    Trap trap = (Trap) o;
    return getDetectionDC() == trap.getDetectionDC()
            && getDisableDC() == trap.getDisableDC()
            && getSaveDC() == trap.getSaveDC()
            && Objects.equals(getDamage(), trap.getDamage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDetectionDC(),
            getDisableDC(), getSaveDC(), getDamage());
  }

  @Override
  public String toString() {
    return "Trap{" +
            "detectionDC=" + detectionDC +
            ", disableDC=" + disableDC +
            ", saveDC=" + saveDC +
            ", damage='" + damage + '\'' +
            ", position=" + position +
            '}';
  }
}
