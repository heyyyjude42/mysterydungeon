package edu.brown.cs.dnd.RandomTools;

import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionTest {

  @Test
  public void testConstruct() {
    NormalDistribution nd = new NormalDistribution(5, 0.5);
    assertNotNull(nd);
  }

  @Test
  public void testGetters() {
    NormalDistribution nd = new NormalDistribution(5, 0.5);
    assertEquals(nd.getMean(), 5, 0001);
    assertEquals(nd.getStandardDeviation(), 0.5, 0.001);
  }
}
