package edu.brown.cs.dnd.Dungeon.Rooms;

import edu.brown.cs.dnd.RandomTools.NormalDistribution;
import java.util.Objects;

/**
 * A class for representing currency in DnD.
 */
public class Money implements ILootElement {
  // The amount of copper coins in this Money.
  private int copper;
  // The amount of silver coins in this Money.
  private int silver;
  // The amount of gold coins in this Money.
  private int gold;
  // The amount of platinum coins in this Money.
  private int platinum;
  // Parameters for random money generation.
  private static final int RANDOM_MONEY_MEAN = 500;
  private static final int RANDOM_MONEY_STD_DEV = 500;

  // Conversion rates.
  private static final int COPPER_TO_PLAT = 1000;
  private static final int COPPER_TO_GOLD = 100;
  private static final int COPPER_TO_SILVER = 10;

  /**
   * Construct a Money.
   * @param copper - an int, the amount of copper
   * @param silver - an int, the amount of silver
   * @param gold - an int, the amount of gold
   * @param platinum - an int, the amount of platinum
   */
  public Money(int copper, int silver, int gold, int platinum) {
    this.copper = copper;
    this.silver = silver;
    this.gold = gold;
    this.platinum = platinum;
  }

  /**
   *
   * @return - a money.
   */
  public static Money randomMoney() {
    NormalDistribution n = new NormalDistribution(RANDOM_MONEY_MEAN,
            RANDOM_MONEY_STD_DEV);
    int amountCopper = (int) Math.ceil(Math.abs(n.draw()));
    int plat = Math.floorDiv(amountCopper, COPPER_TO_PLAT);
    amountCopper -= plat * COPPER_TO_PLAT;
    int gold = Math.floorDiv(amountCopper, COPPER_TO_GOLD);
    amountCopper -= gold * COPPER_TO_GOLD;
    int silver = Math.floorDiv(amountCopper, COPPER_TO_SILVER);
    amountCopper -= silver * COPPER_TO_SILVER;
    int cop = amountCopper;
    return new Money(cop, silver, gold, plat);
  }

  @Override
  public String toString() {
    return "Money{"
            + "copper=" + copper
            + ", silver=" + silver
            + ", gold=" + gold
            + ", platinum=" + platinum
            + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Money)) {
      return false;
    }
    Money money = (Money) o;
    return copper == money.copper
            && silver == money.silver
            && gold == money.gold
            && platinum == money.platinum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(copper, silver, gold, platinum);
  }

  /**
   * get the copper.
   * @return - the copper
   */
  public int getCopper() {
    return copper;
  }
  /**
   * get the silver.
   * @return - the silver
   */
  public int getSilver() {
    return silver;
  }
  /**
   * get the gold.
   * @return - the gold
   */
  public int getGold() {
    return gold;
  }
  /**
   * get the platinum.
   * @return - the platinum
   */
  public int getPlatinum() {
    return platinum;
  }
}
