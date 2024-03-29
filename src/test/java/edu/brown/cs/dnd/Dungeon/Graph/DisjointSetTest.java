package edu.brown.cs.dnd.Dungeon.Graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

public class DisjointSetTest {

  @Test
  public void testConstruct() {
    List<Integer> l = new ArrayList<>();
    l.add(5);
    l.add(3);

    DisjointSet<Integer> ds = new DisjointSet<>(l);
    assertNotNull(ds);
  }

  @Test
  public void testAdd() {
    List<Integer> l = new ArrayList<>();
    l.add(5);
    l.add(3);
    DisjointSet<Integer> ds = new DisjointSet<>(l);

    ds.add(7);
    assertEquals(ds.getParents().get(7), 7.0, 0.001);
    ds.add(100);
    assertEquals(ds.getParents().get(100), 100.0, 0.001);
    ds.add(0);
    assertEquals(ds.getParents().get(0), 0.0, 0.001);
  }

  @Test
  public void testUnion() {
    List<Integer> l = new ArrayList<>();
    l.add(5);
    l.add(3);
    DisjointSet<Integer> ds = new DisjointSet<>(l);

    ds.union(2, 4);
    assertEquals(ds.getParents().get(2), 4.0, 0.001);
    ds.add(9);
    ds.union(9, 9);
    assertEquals(ds.getParents().get(9), 9, 0.001);

    ds.union(50, 70);
    assertEquals(ds.getParents().get(50), 70.0, 0.001);
  }

  @Test
  public void testFind() {
    List<Integer> l = new ArrayList<>();
    l.add(5);
    l.add(3);
    DisjointSet<Integer> ds = new DisjointSet<>(l);

    assertEquals(ds.find(5), 5, 0.001);

    ds.union(10, 11);
    ds.add(11);
    assertEquals(ds.find(10), 11, 0.001);

    ds.union(20, 21);
    ds.union(21, 22);
    ds.add(22);
    assertEquals(ds.find(20), 22, 0.001);

    ds.union(30, 31);
    ds.union(31, 32);
    ds.union(32, 33);
    ds.add(33);
    assertEquals(ds.find(30), 33, 0.001);
  }

  @Test
  public void testInSameSet() {
    List<Integer> l = new ArrayList<>();
    l.add(5);
    l.add(3);
    DisjointSet<Integer> ds = new DisjointSet<>(l);
    assertFalse(ds.inSameSet(5, 3));

    ds.union(5, 3);
    assertTrue(ds.inSameSet(5, 3));

    ds.union(20, 21);
    ds.union(21, 22);
    ds.add(22);
    assertTrue(ds.inSameSet(21, 22));
  }
}
