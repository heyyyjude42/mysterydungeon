package edu.brown.cs.dnd.RandomTools;

import java.util.Random;

public class NormalDistribution {

  private Random rand;

  private double mean;

  private double standardDeviation;

  public NormalDistribution(double mean, double stdDev) {
    this.mean = mean;
    this.standardDeviation = stdDev;
    this.rand = new Random();
  }

  public double draw() {
    return rand.nextGaussian() * standardDeviation + mean;
  }

  public double getMean() {
    return mean;
  }

  public double getStandardDeviation() {
    return standardDeviation;
  }

  public void setMean(double mean) {
    this.mean = mean;
  }

  public void setStandardDeviation(double standardDeviation) {
    this.standardDeviation = standardDeviation;
  }
}
