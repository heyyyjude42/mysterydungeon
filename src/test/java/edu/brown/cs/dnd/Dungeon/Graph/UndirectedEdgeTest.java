package edu.brown.cs.dnd.Dungeon.Graph;

import org.junit.Test;
import static org.junit.Assert.*;

public class UndirectedEdgeTest {

  @Test
  public void testConstruct() {
    UndirectedEdge<Integer> ue = new UndirectedEdge<>(5, 5, 5);

    assertNotNull(ue);
  }

  @Test
  public void testEquals() {
    UndirectedEdge<Integer> ue1 = new UndirectedEdge<>(3, 5, 7);
    UndirectedEdge<Integer> ue2 = new UndirectedEdge<>(3, 5, 7);
    UndirectedEdge<Integer> ue3 = new UndirectedEdge<>(5, 3, 7);
    UndirectedEdge<Integer> ue4 = new UndirectedEdge<>(5, 3, 4);

    assertEquals(ue1, ue2);
    assertEquals(ue1, ue3);
    assertNotEquals(ue1, ue4);
  }

  @Test
  public void testCompareTo() {
    UndirectedEdge<Integer> ue1 = new UndirectedEdge<>(3, 5, 7);
    UndirectedEdge<Integer> ue2 = new UndirectedEdge<>(1, 1, 10);
    UndirectedEdge<Integer> ue3 = new UndirectedEdge<>(2, 2, 7);

    assertEquals(ue1.compareTo(ue2), -1);
    assertEquals(ue1.compareTo(ue3), 0);
    assertEquals(ue2.compareTo(ue1), 1);
  }

  @Test
  public void testToString() {
    UndirectedEdge<Integer> ue1 = new UndirectedEdge<>(3, 5, 7);
    UndirectedEdge<Integer> ue2 = new UndirectedEdge<>(1, 2, 10);
    UndirectedEdge<Integer> ue3 = new UndirectedEdge<>(2, 3, 7);

    assertEquals(ue1.toString(), "Undirected Edge {v1 = 3, v2 = 5, "
            + "weight = 7.0}");
  }

  @Test
  public void testGetters() {
    UndirectedEdge<Integer> ue1 = new UndirectedEdge<>(3, 5, 7);

    assertEquals(ue1.getV1(), 3.0, 0.001);
    assertEquals(ue1.getV2(), 5.0, 0.001);
  }

}
