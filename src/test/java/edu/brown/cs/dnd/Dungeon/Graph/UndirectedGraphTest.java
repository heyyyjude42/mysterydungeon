package edu.brown.cs.dnd.Dungeon.Graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;

public class UndirectedGraphTest {

  @Test
  public void testConstruct() {
    UndirectedGraph<Integer> ug = new UndirectedGraph<>();

    assertNotNull(ug);
  }

  @Test
  public void testAddEdge() {
    UndirectedGraph<Integer> ug = new UndirectedGraph<>();

    Set<UndirectedEdge<Integer>> edges = new HashSet<>();

    ug.addEdge(new UndirectedEdge<Integer>(3, 5, 7));
    edges.add(new UndirectedEdge<Integer>(3, 5, 7));
    assertEquals(ug.getEdges(), edges);

    ug.addEdge(10, 11, 12);
    edges.add(new UndirectedEdge<Integer>(10, 11, 12));
    assertEquals(ug.getEdges(), edges);

    ug.addEdge(new UndirectedEdge<>(1, 2, 0));
    edges.add(new UndirectedEdge<>(1,2, 0));
    assertEquals(ug.getEdges(), edges);
  }

  @Test
  public void testGetters() {
    UndirectedGraph<Integer> ug = new UndirectedGraph<>();

    Set<UndirectedEdge<Integer>> edges = new HashSet<>();
    Set<Integer> vertices = new HashSet<>();

    ug.addEdge(3, 5, 7);
    edges.add(new UndirectedEdge<>(3, 5, 7));
    vertices.add(3);
    vertices.add(5);

    assertEquals(ug.getEdges(), edges);
    assertEquals(ug.getVertices(), vertices);

    ug.addEdge(10, 11, 12);
    edges.add(new UndirectedEdge<>(10, 11, 12));
    vertices.add(10);
    vertices.add(11);

    assertEquals(ug.getEdges(), edges);
    assertEquals(ug.getVertices(), vertices);
  }

  @Test
  public void testMST() {
    UndirectedGraph<String> ug = new UndirectedGraph<>();

    ug.addEdge("a", "b", 3);
    ug.addEdge("b", "c", 5);
    ug.addEdge("a", "c", 2);

    Set<UndirectedEdge<String>> resultEdges = new HashSet<>();

    UndirectedGraph mst = ug.mst();
    resultEdges.add(new UndirectedEdge<>("a", "b", 3));
    resultEdges.add(new UndirectedEdge<>("a", "c", 2));
    assertEquals(resultEdges, mst.getEdges());

    ug.addEdge("b", "d", 8);
    ug.addEdge("a", "d", 5);

    UndirectedGraph mst2 = ug.mst();
    resultEdges.add(new UndirectedEdge<>("a", "d", 5));
    assertEquals(mst2.getEdges(), resultEdges);
  }
}
