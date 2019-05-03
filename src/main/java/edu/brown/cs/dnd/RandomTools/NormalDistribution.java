package edu.brown.cs.dnd.RandomTools;

import java.util.Random;

/**
 * Class representing utilities for drawing from a Normal Distribution.
 */
public class NormalDistribution {

  private Random rand;
  private double mean;
  private double standardDeviation;

  /**
   * A Constructor for a NormalDistribution.
   * @param mean    A mean that is the mean of the normal distribution
   * @param stdDev    A double that is the standard deviation of the
   *                  normal distribution
   */
  public NormalDistribution(double mean, double stdDev) {
    this.mean = mean;
    this.standardDeviation = stdDev;
    this.rand = new Random();
  }

  /**
   * Method draws a number from the normal distribution.
   * @return    A double that is randomly drawn from the specified normal
   * distribution
   */
  public double draw() {
    return rand.nextGaussian() * standardDeviation + mean;
  }

  /**
   * Method gets the mean of the distribution.
   * @return    A double that is the mean of the distribution
   */
  double getMean() {
    return mean;
  }

  /**
   * Method gets the standard deviation of the distribution.
   * @return    A double that is the standard deviation of the distribution
   */
  double getStandardDeviation() {
    return standardDeviation;
  }

  /**
   * Method sets the mean of the distribution.
   * @param mean    A double that is the mean to set
   */
  public void setMean(double mean) {
    this.mean = mean;
  }

  /**
   * Method sets the standard deviation of the distribution.
   * @param standardDeviation   A double that is the standard deviation of the
   *                            distribution
   */
  public void setStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }
}
